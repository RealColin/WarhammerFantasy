package realcolin.whmod.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public record Noise(NoiseHolder noise, double xScale, double yScale, double zScale) implements DensityFunction {
    public static final MapCodec<Noise> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            NoiseHolder.CODEC.fieldOf("noise").forGetter(Noise::noise),
            Codec.DOUBLE.fieldOf("x_scale").forGetter(Noise::xScale),
            Codec.DOUBLE.fieldOf("y_scale").forGetter(Noise::yScale),
            Codec.DOUBLE.fieldOf("z_scale").forGetter(Noise::zScale)
    ).apply(instance, Noise::new));

    @Override
    public double compute(FunctionContext functionContext) {
        return noise.getValue(
                xScale * (double)functionContext.blockX(),
                yScale * (double)functionContext.blockY(),
                zScale * (double)functionContext.blockZ());
    }

    @Override
    public void fillArray(double @NotNull [] array, ContextProvider contextProvider) {
        contextProvider.fillAllDirectly(array, this);
    }

    @Override
    public @NonNull DensityFunction mapChildren(Visitor visitor) {
        return new Noise(visitor.visitNoise(noise), xScale, yScale, zScale);
    }

    @Override
    public double minValue() {
        return -maxValue();
    }

    @Override
    public double maxValue() {
        return this.noise.maxValue();
    }

    @Override
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return new KeyDispatchDataCodec<>(CODEC);
    }
}
