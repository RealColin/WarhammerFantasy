package realcolin.whmod.client.screen.menu.map;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import realcolin.whmod.WHMod;
import realcolin.whmod.worldgen.map.WorldMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class MapTileCache {
    private static final int MAX_TILES = 128;
    private final Map<MapTileKey, MapTile> tiles = new LinkedHashMap<>(16, 0.75f, true);

    private final WorldMap clientMap;
    private final int tileSize;
    private final double[] pixelsPerMmByZoom;

    public MapTileCache(WorldMap clientMap, int tileSize, double[] pixelsPerMmByZoom) {
        this.clientMap = clientMap;
        this.tileSize = tileSize;
        this.pixelsPerMmByZoom = pixelsPerMmByZoom;
    }

    public MapTile getOrCreate(MapTileKey key) {
        MapTile existing = tiles.get(key);

        if (existing != null) {
            return existing;
        }

        MapTile created = generateTile(key);
        tiles.put(key, created);

        evictIfNeeded();

        return created;
    }

    private MapTile generateTile(MapTileKey key) {
        int zoomLevel = key.zoomLevel();
        double ppm = pixelsPerMm(zoomLevel);

        int tilePixelX = key.tileX() * tileSize;
        int tilePixelY = key.tileY() * tileSize;

        BufferedImage bufferedImage = new BufferedImage(
                tileSize,
                tileSize,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2d = bufferedImage.createGraphics();

        try {
            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF
            );

            g2d.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_SPEED
            );

            g2d.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
            );

            double scale = (ppm * 25.4) / 96.0;

            g2d.translate(-tilePixelX, -tilePixelY);
            g2d.scale(scale, scale);

            clientMap.getNode().paint(g2d);
        } finally {
            g2d.dispose();
        }

        NativeImage nativeImage = bufferedImageToNativeImage(bufferedImage);

        DynamicTexture texture = new DynamicTexture(
                () -> "warhammer_map_tile_" + key.zoomLevel() + "_" + key.tileX() + "_" + key.tileY(),
                nativeImage
        );

        Identifier identifier = Identifier.fromNamespaceAndPath(
                WHMod.MOD_ID,
                "dynamic_map_" + key.zoomLevel() + "/" + key.tileX() + "_" + key.tileY()
        );

        Minecraft.getInstance().getTextureManager().register(identifier, texture);

        return new MapTile(identifier, texture);
    }

    private static NativeImage bufferedImageToNativeImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        NativeImage nativeImage = new NativeImage(width, height, true);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = bufferedImage.getRGB(x, y);
                nativeImage.setPixelABGR(x, y, argbToAbgr(argb));
            }
        }

        return nativeImage;
    }

    private static int argbToAbgr(int argb) {
        int a = (argb >>> 24) & 0xFF;
        int r = (argb >>> 16) & 0xFF;
        int g = (argb >>> 8) & 0xFF;
        int b = argb & 0xFF;

        return (a << 24) | (b << 16) | (g << 8) | r;
    }

    private void evictIfNeeded() {
        while (tiles.size() > MAX_TILES) {
            Iterator<Map.Entry<MapTileKey, MapTile>> iterator = tiles.entrySet().iterator();

            if (!iterator.hasNext()) {
                return;
            }

            Map.Entry<MapTileKey, MapTile> eldest = iterator.next();

            eldest.getValue().close();
            iterator.remove();
        }
    }

    private double pixelsPerMm(int zoomLevel) {
        return pixelsPerMmByZoom[zoomLevel];
    }
}
