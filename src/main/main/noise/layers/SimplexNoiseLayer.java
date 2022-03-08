package noise.layers;

import noise.OpenSimplexNoise;

public class SimplexNoiseLayer implements NoiseLayer {

    private long seed;

    private final double scale;
    private final int octaves;
    private final double persistence;
    private final double lacunarity;

    private final OpenSimplexNoise[] noise;

    public SimplexNoiseLayer(double scale, int octaves, double persistence, double lacunarity) {
        this.scale = scale;
        this.octaves = octaves;
        this.persistence = persistence;
        this.lacunarity = lacunarity;

        this.noise = new OpenSimplexNoise[octaves];
        for (int i = 0; i < octaves; i++) {
            noise[i] = new OpenSimplexNoise(seed + i);
        }
    }

    public SimplexNoiseLayer setSeed(long seed) {
        this.seed = seed;
        return this;
    }

    @Override
    public double evaluate(double x, double y) {
        double amplitude = 1;
        double frequency = 1;

        double noiseValue = 0;

        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;

        for (int i = 0; i < octaves; i++)  {
            noiseValue += noise[i].eval(x / scale * frequency, y / scale * frequency) * amplitude;
            amplitude *= persistence;
            frequency *= lacunarity;
        }

        // todo: normalize noise values??

        return noiseValue;
    }
}

