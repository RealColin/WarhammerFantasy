package realcolin.whmod.worldgen.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.biome.Biome;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;
import realcolin.whmod.Constants;
import realcolin.whmod.WHRegistries;
import realcolin.whmod.WHMod;
import realcolin.whmod.util.Pair;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WorldMap {

    public static final Codec<WorldMap> DIRECT_CODEC =
            RecordCodecBuilder.create(inst -> inst.group(
                    Identifier.CODEC.fieldOf("image").forGetter(src -> src.imageLoc),
                    Biome.CODEC.fieldOf("default_biome").forGetter(src -> src.defaultBiome),
                    Terrain.CODEC.fieldOf("default_terrain").forGetter(src -> src.defaultTerrain),
                    Region.CODEC.listOf().fieldOf("regions").forGetter(src -> src.entries)
            ).apply(inst, WorldMap::new));

    public static final Codec<WorldMap> NETWORK_CODEC =
            RecordCodecBuilder.create(inst -> inst.group(
                    Identifier.CODEC.fieldOf("image").forGetter(src -> src.imageLoc),
                    Region.CODEC.listOf().fieldOf("regions").forGetter(src -> src.entries)
            ).apply(inst, WorldMap::clientOnly));

    public static final Codec<Holder<WorldMap>> CODEC = RegistryFileCodec.create(WHRegistries.MAP, DIRECT_CODEC);

    private final Identifier imageLoc;
    private final Holder<Biome> defaultBiome;
    private final Holder<Terrain> defaultTerrain;
    private final List<Holder<Region>> entries;


    private final Set<Holder<Terrain>> terrains;
    private final HashMap<Integer, Region> colorRegionMap;
    private final float svgWidth;
    private final float svgHeight;
    private final int width;
    private final int height;
    private final GraphicsNode node;


    private final ConcurrentHashMap<Pair, Cell> cellCache;

    public WorldMap(Identifier imageLoc, Holder<Biome> defaultBiome, Holder<Terrain> defaultTerrain, List<Holder<Region>> entries) {
        this.imageLoc = imageLoc;
        this.defaultBiome = defaultBiome;
        this.defaultTerrain = defaultTerrain;
        this.entries = entries;
        this.cellCache = new ConcurrentHashMap<>();

        String PATH = "assets/%s/map/%s".formatted(imageLoc.getNamespace(), imageLoc.getPath());

        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
            InputStream svgFile = WorldMap.class.getResourceAsStream("/" + PATH);
            SVGDocument svgDocument = factory.createSVGDocument(null, svgFile);

            GVTBuilder builder = new GVTBuilder();
            BridgeContext ctx = new BridgeContext(new UserAgentAdapter());
            node = builder.build(ctx, svgDocument);

            // this is a mess bro :sob:
            this.svgWidth = svgDocument.getRootElement().getWidth().getBaseVal().getValueInSpecifiedUnits();
            this.svgHeight = svgDocument.getRootElement().getHeight().getBaseVal().getValueInSpecifiedUnits();
            this.width = Math.round((svgDocument.getRootElement().getWidth().getBaseVal().getValue() / 96) * Constants.BLOCKS_PER_INCH);
            this.height = Math.round((svgDocument.getRootElement().getHeight().getBaseVal().getValue() / 96) * Constants.BLOCKS_PER_INCH);

        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        terrains = new HashSet<>();
        colorRegionMap = new HashMap<>();

        // no entries when doing client side, gotta clean this up a bit make it more robust
        if (entries == null)
            return;

        for (var e : entries) {
            terrains.add(e.value().terrain());

            var c = new Color(e.value().color());

            if (colorRegionMap.containsKey(c.getRGB())) {
                var existingEntry = colorRegionMap.get(c.getRGB());
                WHMod.LOGGER.error("Entry {} uses color already used by {}", e.value().region(), existingEntry.region());
                throw new RuntimeException("Bad WorldMap");
            }

            colorRegionMap.put(c.getRGB(), e.value());
        }
        terrains.add(defaultTerrain);

        WHMod.LOGGER.info("Successfully Initialized a WorldMap instance");
    }

    public static WorldMap clientOnly(Identifier imageLoc, List<Holder<Region>> entries) {
        return new WorldMap(imageLoc, null, null, entries);
    }

    public Holder<Biome> getDefaultBiome() {
        return defaultBiome;
    }

    public Set<Holder<Biome>> getAllBiomes() {
        var set = new HashSet<Holder<Biome>>();
        set.add(defaultBiome);

        for (var e : entries) {
            List<Holder<Biome>> list = e.value().biomes().values().stream().map(com.mojang.datafixers.util.Pair::getSecond).toList();
            set.addAll(list);
        }

        return set;
    }

    public Region getEntryAt(int x, int z) {
        var color = getColorAtPixel(x, z);

        if (color != -1 && colorRegionMap.containsKey(color))
            return colorRegionMap.get(color);

        return null;
    }

    public Set<Holder<Terrain>> getTerrains() {
        return terrains;
    }

    public Cell getCellAt(int x, int z) {
        var cellPos = new Pair(Math.floorDiv(x, Constants.CELL_SIZE), Math.floorDiv(z, Constants.CELL_SIZE));

        if (cellCache.containsKey(cellPos))
            return cellCache.get(cellPos);
        else {
            var cell = new Cell(this.node, Constants.BLOCKS_PER_INCH, cellPos);
            cellCache.put(cellPos, cell);
            return cell;
        }
    }

    public Terrain getTerrainFromColor(int color) {
        if (color != -1 && colorRegionMap.containsKey(color)) {
            return colorRegionMap.get(color).terrain().value();
        }

        return defaultTerrain.value();
    }

    public Terrain getTerrain(int x, int z) {
        var color = getColorAtPixel(x, z);

        if (color != -1 && colorRegionMap.containsKey(color))
            return colorRegionMap.get(color).terrain().value();

        return defaultTerrain.value();
    }

    @SuppressWarnings("unused")
    private int getColorAtPixel(int x, int y) {
        if (outsideRange(x, y))
            return -1;

        var cellPos = new Pair(Math.floorDiv(x, Constants.CELL_SIZE), Math.floorDiv(y, Constants.CELL_SIZE));
        Cell cell;

        if (cellCache.containsKey(cellPos))
            cell = cellCache.get(cellPos);
        else {
            var start = System.nanoTime();
            cell = new Cell(this.node, Constants.BLOCKS_PER_INCH, cellPos);
            var elapsed = System.nanoTime() - start;
//            System.out.println("Cell generated in " + elapsed + " nanoseconds.");

            cellCache.put(cellPos, cell);
        }

        if (cellCache.size() > 128) {
            cellCache.clear();
        }

        return cell.getColorAt(x, y);
    }

    private boolean outsideRange(int x, int y) {
        return x < 0 || x >= width || y < 0 || y >= height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float getSvgWidth() {
        return this.svgWidth;
    }

    public float getSvgHeight() {
        return this.svgHeight;
    }

    public GraphicsNode getNode() {
        return this.node;
    }
}
