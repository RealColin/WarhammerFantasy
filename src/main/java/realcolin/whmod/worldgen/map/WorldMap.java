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
                    Codec.INT.fieldOf("resolution").forGetter(src -> src.resolution),
                    Biome.CODEC.fieldOf("default_biome").forGetter(src -> src.defaultBiome),
                    Terrain.CODEC.fieldOf("default_terrain").forGetter(src -> src.defaultTerrain),
                    MapEntry.ENTRIES_CODEC.fieldOf("entries").forGetter(src -> src.entries)
            ).apply(inst, WorldMap::new));

    public static final Codec<Holder<WorldMap>> CODEC = RegistryFileCodec.create(WHRegistries.MAP, DIRECT_CODEC);

    private final Identifier imageLoc;
    private final int resolution; // blocks per inch
    private final Holder<Biome> defaultBiome;
    private final Holder<Terrain> defaultTerrain;
    private final List<MapEntry> entries;

    private final Set<Holder<Terrain>> terrains;
    private final HashMap<Integer, MapEntry> colorRegionMap;
    private final int width;
    private final int height;
    private final GraphicsNode node;

    private final ConcurrentHashMap<Pair, Cell> cellCache;

    public WorldMap(Identifier imageLoc, int resolution, Holder<Biome> defaultBiome, Holder<Terrain> defaultTerrain, List<MapEntry> entries) {
        this.imageLoc = imageLoc;
        this.resolution = resolution;
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

            this.width = Math.round((svgDocument.getRootElement().getWidth().getBaseVal().getValue() / 96) * resolution);
            this.height = Math.round((svgDocument.getRootElement().getHeight().getBaseVal().getValue() / 96) * resolution);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        terrains = new HashSet<>();
        colorRegionMap = new HashMap<>();
        for (var e : entries) {
            terrains.add(e.terrain());

            var c = new Color(e.color());
            colorRegionMap.put(c.getRGB(), e);
        }
        terrains.add(defaultTerrain);

        WHMod.LOGGER.info("Successfully Initialized a WorldMap instance");
    }

    public Holder<Biome> getDefaultBiome() {
        return defaultBiome;
    }

    public Set<Holder<Biome>> getAllBiomes() {
        var set = new HashSet<Holder<Biome>>();
        set.add(defaultBiome);

        for (var e : entries) {
            List<Holder<Biome>> list = e.biomes().values().stream().map(com.mojang.datafixers.util.Pair::getSecond).toList();
            set.addAll(list);
        }

        return set;
    }

    public MapEntry getEntryAt(int x, int z) {
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
            var cell = new Cell(this.node, this.resolution, cellPos);
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
            cell = new Cell(this.node, this.resolution, cellPos);
            var elapsed = System.nanoTime() - start;
//            System.out.println("Cell generated in " + elapsed + " nanoseconds.");

            cellCache.put(cellPos, cell);
        }

        return cell.getColorAt(x, y);
    }

    private boolean outsideRange(int x, int y) {
        return x < 0 || x >= width || y < 0 || y >= height;
    }
}
