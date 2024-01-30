package pie.ilikepiefoo.orevacuum.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public record SuctionBlockEvent(SuctionEvent suctionEvent, BlockPos blockPos, BlockState blockState) {
    public SuctionBlockEvent(SuctionEvent suctionEvent, BlockPos blockPos) {
        this(suctionEvent, blockPos, suctionEvent.level().getBlockState(blockPos));
    }

    public double getDistanceFromEyeToBlock() {
        return blockPos().distSqr(suctionEvent.playerEyePositionBlockPos());
    }
}
