package noise;

import noise.layers.NoiseLayer;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Noise {
    private final List<NoiseLayer> layers;

    public Noise() {
        layers = new ArrayList<>();
    }

    public void addLayer(NoiseLayer layer) {
        layers.add(layer);
    }

    public double[][] evaluate(double width, double height) {
        double[][] values = new double[(int) width][(int) height];

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double value = 0;
                for (NoiseLayer layer : layers) {
                    value += layer.evaluate(x, y);
                }
                values[x][y] = value;

                min = Math.min(min, value);
                max = Math.max(max, value);
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double relativeX = (float) x / width * 2 - 1;
                double relativeY = (float) y / height * 2 - 1;
                double falloff = Math.max(Math.abs(relativeX), Math.abs(relativeY));

                double a = 2;
                double b = 6;
                falloff = Math.pow(falloff, a) / (Math.pow(falloff, a) + Math.pow(b - b * falloff, a));
                values[x][y] = (values[x][y] - min) / (max - min) * (1 - falloff);
            }
        }

        return values;
    }
}

