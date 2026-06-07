package realcolin.whmod.client.screen.menu;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.entity.player.Player;
import realcolin.whmod.Constants;
import realcolin.whmod.client.screen.menu.map.MapTile;
import realcolin.whmod.client.screen.menu.map.MapTileCache;
import realcolin.whmod.client.screen.menu.map.MapTileKey;
import realcolin.whmod.worldgen.map.WorldMap;

public class MapSubScreen implements MenuSubScreen {
    private static final double MM_PER_INCH = 25.4;
    private static final int TILE_SIZE = 256;
    private static final double[] PIXELS_PER_MM_BY_ZOOM = {
            0.0625,
            0.125,
            0.25,
            0.5,
            1.0,
            2.0,
            4.0
    };

    private int zoomLevel = 3;

    private final WorldMap clientMap;
    private final Player player;

    private final MapTileCache tileCache;

    private double centerU;
    private double centerV;

    private int viewportWidth;
    private int viewportHeight;
    private boolean dragging = false;

    /* Constructors */

    public MapSubScreen(WorldMap clientMap, Player player) {
        this.clientMap = clientMap;
        this.player = player;

        this.tileCache = new MapTileCache(clientMap, TILE_SIZE, PIXELS_PER_MM_BY_ZOOM);

        centerOnPlayer();
    }

    /* Override methods */

    @Override
    public void render(GuiGraphicsExtractor g, int px, int py, int pw, int ph, int mouseX, int mouseY, Font font) {
        this.viewportWidth = pw;
        this.viewportHeight = ph;
        clampCenter();

        g.fill(px, py, px + pw, py + ph, 0xFF101010);
        g.enableScissor(px, py, px + pw, py + ph);

        renderVisibleTiles(g, px, py, pw, ph);
        renderPlayerMarker(g, px, py, pw, ph);

        g.disableScissor();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return false;
        }

        dragging = true;
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return false;
        }

        dragging = false;
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!dragging || button != 0) {
            return false;
        }

        centerU -= dragX / virtualMapWidth();
        centerV -= dragY / virtualMapHeight();

        clampCenter();

        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int oldZoomLevel = zoomLevel;

        if (scrollY > 0 && canZoomIn()) {
            zoomLevel++;
        } else if (scrollY < 0 && canZoomOut()) {
            zoomLevel--;
        }

        if (zoomLevel == oldZoomLevel) {
            return false;
        }

        clampCenter();

        return true;
    }

    @Override
    public String name() {
        return "Map";
    }

    /* Methods */
    private void centerOnPlayer() {
        if (player == null) {
            centerU = 0.5;
            centerV = 0.5;
            return;
        }

        double playerSvgX = blockXToSvgMm(player.getX());
        double playerSvgY = blockZToSvgMm(player.getZ());

        centerU = Math.clamp(playerSvgX / clientMap.getSvgWidth(), 0.0, 1.0);
        centerV = Math.clamp(playerSvgY / clientMap.getSvgHeight(), 0.0, 1.0);
    }

    private void clampCenter() {
        if (viewportWidth <= 0 || viewportHeight <= 0) {
            centerU = Math.clamp(centerU, 0.0, 1.0);
            centerV = Math.clamp(centerV, 0.0, 1.0);
            return;
        }

        double halfU = (viewportWidth / 2.0) / virtualMapWidth();
        double halfV = (viewportHeight / 2.0) / virtualMapHeight();

        if (halfU >= 0.5) {
            centerU = 0.5;
        } else {
            centerU = Math.clamp(centerU, halfU, 1.0 - halfU);
        }

        if (halfV >= 0.5) {
            centerV = 0.5;
        } else {
            centerV = Math.clamp(centerV, halfV, 1.0 - halfV);
        }
    }

    private void renderVisibleTiles(GuiGraphicsExtractor g, int px, int py, int pw, int ph) {
        var viewX = getViewX(pw);
        var viewY = getViewY(ph);

        int minTileX = (int) Math.floor(viewX / TILE_SIZE);
        int minTileY = (int) Math.floor(viewY / TILE_SIZE);

        int maxTileX = (int) Math.floor((viewX + pw) / TILE_SIZE);
        int maxTileY = (int) Math.floor((viewY + ph) / TILE_SIZE);

        int tilesX = (int) Math.ceil((double) virtualMapWidth() / TILE_SIZE);
        int tilesY = (int) Math.ceil((double) virtualMapHeight() / TILE_SIZE);

        minTileX = Math.clamp(minTileX, 0, tilesX - 1);
        minTileY = Math.clamp(minTileY, 0, tilesY - 1);
        maxTileX = Math.clamp(maxTileX, 0, tilesX - 1);
        maxTileY = Math.clamp(maxTileY, 0, tilesY - 1);

        for (int tileY = minTileY; tileY <= maxTileY; tileY++) {
            for (int tileX = minTileX; tileX <= maxTileX; tileX++) {
                int tileMapX = tileX * TILE_SIZE;
                int tileMapY = tileY * TILE_SIZE;

                int screenX = px + (int) Math.round(tileMapX - viewX);
                int screenY = py + (int) Math.round(tileMapY - viewY);

                drawMapTile(g, tileX, tileY, screenX, screenY);
            }
        }
        
    }

    private void drawMapTile(GuiGraphicsExtractor g , int tileX, int tileY, int screenX, int screenY) {
        MapTileKey key = new MapTileKey(zoomLevel, tileX, tileY);
        MapTile tile = tileCache.getOrCreate(key);

        g.blit(RenderPipelines.GUI_TEXTURED, tile.identifier(), screenX, screenY, 0, 0, TILE_SIZE, TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private void renderPlayerMarker(GuiGraphicsExtractor g, int px, int py, int pw, int ph) {
        if (player == null) {
            return;
        }

        double playerMapX = blockXToSvgMm(player.getX()) * pixelsPerMm();
        double playerMapY = blockZToSvgMm(player.getZ()) * pixelsPerMm();

        if (
                playerMapX < 0.0 ||
                        playerMapX > virtualMapWidth() ||
                        playerMapY < 0.0 ||
                        playerMapY > virtualMapHeight()
        ) {
            return;
        }

        double viewX = getViewX(pw);
        double viewY = getViewY(ph);

        int markerX = px + (int) Math.round(playerMapX - viewX);
        int markerY = py + (int) Math.round(playerMapY - viewY);

        // Only draw if marker is within the visible panel.
        if (markerX < px || markerX >= px + pw || markerY < py || markerY >= py + ph) {
            return;
        }

        // Simple red square marker.
        g.fill(markerX - 3, markerY - 3, markerX + 4, markerY + 4, 0xFFFF3333);

        // Tiny white center dot.
        g.fill(markerX, markerY, markerX + 1, markerY + 1, 0xFFFFFFFF);
    }

    /* Helper methods */

    private double pixelsPerMm() {
        return PIXELS_PER_MM_BY_ZOOM[zoomLevel];
    }

    private int virtualMapWidth() {
        return (int) Math.ceil(clientMap.getSvgWidth() * pixelsPerMm());
    }

    private int virtualMapHeight() {
        return (int) Math.ceil(clientMap.getSvgHeight() * pixelsPerMm());
    }

    private double blockXToSvgMm(double blockX) {
        int minBlockX = 0;
        return (blockX - minBlockX) / Constants.BLOCKS_PER_INCH * MM_PER_INCH;
    }

    private double blockZToSvgMm(double blockZ) {
        int minBlockZ = 0;
        return (blockZ - minBlockZ) / Constants.BLOCKS_PER_INCH * MM_PER_INCH;
    }

    private double getViewX(int pw) {
        return centerU * virtualMapWidth() - pw / 2.0;
    }

    private double getViewY(int ph) {
        return centerV * virtualMapHeight() - ph / 2.0;
    }

    private boolean canZoomIn() {
        return zoomLevel < PIXELS_PER_MM_BY_ZOOM.length - 1;
    }

    private boolean canZoomOut() {
        return zoomLevel > 0;
    }
}
