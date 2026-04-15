package realcolin.whmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class FlammableRotatedPillarBlock extends RotatedPillarBlock {
    public FlammableRotatedPillarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFlammable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull Direction direction) {
        return 5;
    }

    // encouragement
    @Override
    public int getFireSpreadSpeed(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull Direction direction) {
        return 5;
    }
}
