package realcolin.whmod.worldgen.map;

import ij.plugin.filter.EDM;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import org.apache.batik.gvt.GraphicsNode;
import realcolin.whmod.Constants;
import realcolin.whmod.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;

public class Cell {

    private final BufferedImage bufferedRegionMap;

    private final HashMap<Integer, double[][]> distanceTransforms;

    public Cell(GraphicsNode node, int resolution, Pair cellPos) {
        BufferedImage regionMap = new BufferedImage(Constants.CELL_SIZE, Constants.CELL_SIZE, BufferedImage.TYPE_INT_RGB);
        var g2d = regionMap.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        int svgX = cellPos.a() * Constants.CELL_SIZE;
        int svgY = cellPos.b() * Constants.CELL_SIZE;
        g2d.translate(-svgX, -svgY);
        g2d.scale((double) resolution / 96.0, (double)resolution / 96.0);
        node.paint(g2d);

        bufferedRegionMap = new BufferedImage(Constants.CELL_SIZE + Constants.CELL_BUFFER, Constants.CELL_SIZE + Constants.CELL_BUFFER, BufferedImage.TYPE_INT_RGB);
        var g2dd = bufferedRegionMap.createGraphics();

        g2dd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2dd.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        svgX = cellPos.a() * Constants.CELL_SIZE - (Constants.CELL_BUFFER / 2);
        svgY = cellPos.b() * Constants.CELL_SIZE - (Constants.CELL_BUFFER / 2);

        g2dd.translate(-svgX, -svgY);
        g2dd.scale((double) resolution / 96.0, (double) resolution / 96.0);
        node.paint(g2dd);

        distanceTransforms = new HashMap<>();
        createDistTransforms();
    }

    public int getColorAt(int x, int z) {
        return bufferedRegionMap.getRGB(Math.floorMod(x, Constants.CELL_SIZE) + (Constants.CELL_BUFFER / 2), Math.floorMod(z, Constants.CELL_SIZE) + (Constants.CELL_BUFFER / 2));
    }

    private void createDistTransforms() {
        // get every unique color in the buffered map
        var colors = new HashSet<Integer>();
        for (int x = 0; x < bufferedRegionMap.getWidth(); x++) {
            for (int y = 0; y < bufferedRegionMap.getHeight(); y++) {
                var color = bufferedRegionMap.getRGB(x, y);
                colors.add(color);
            }
        }

        for (var color : colors) {
            var mask = new ByteProcessor(bufferedRegionMap.getWidth(), bufferedRegionMap.getHeight());
            for (int x = 0; x < bufferedRegionMap.getWidth(); x++) {
                for (int y = 0; y < bufferedRegionMap.getHeight(); y++) {
                    mask.set(x, y, bufferedRegionMap.getRGB(x, y) == color ? 0 : 255);
                }
            }

            EDM edm = new EDM();
            FloatProcessor edt = edm.makeFloatEDM(mask, 0, false);

            int width = edt.getWidth();
            int height = edt.getHeight();
            double[][] dists = new double[width][height];

            double maxDist = 0;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    dists[x][y] = edt.getf(x, y);
                    maxDist = Math.max(maxDist, dists[x][y]);
                }
            }

            distanceTransforms.put(color, dists);
        }
    }

    public HashMap<Integer, double[][]> getDistTransforms() {
        return distanceTransforms;
    }

    public int getClosestColorWithinBlendRange(int x, int z) {
        int ox = Math.floorMod(x, Constants.CELL_SIZE) + (Constants.CELL_BUFFER / 2);
        int oz = Math.floorMod(z, Constants.CELL_SIZE) + (Constants.CELL_BUFFER / 2);

        var dist = Constants.BLEND_RANGE;
        var color = bufferedRegionMap.getRGB(ox, oz);

        for (var c : distanceTransforms.keySet()) {
            var arr = distanceTransforms.get(c);
            var val = arr[ox][oz];

            if (val < dist) {
                dist = val;
                color = c;
            }
        }

        return color;
    }
}
