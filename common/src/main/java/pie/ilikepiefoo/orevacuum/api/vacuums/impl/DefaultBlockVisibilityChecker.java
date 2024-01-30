package pie.ilikepiefoo.orevacuum.api.vacuums.impl;

import net.minecraft.world.level.ClipContext;
import pie.ilikepiefoo.orevacuum.BlockCalculations;
import pie.ilikepiefoo.orevacuum.api.event.SuctionBlockEvent;
import pie.ilikepiefoo.orevacuum.api.vacuums.BlockVisibilityChecker;

public class DefaultBlockVisibilityChecker implements BlockVisibilityChecker {

    @Override
    public boolean canSeeBlock(SuctionBlockEvent event) {
        var context = event.suctionEvent();
        if (event.blockPos().equals(context.playerEyePositionBlockPos())) {
            return true;
        }
        if ((int) event.getDistanceFromEyeToBlock() == 1) {
            return true;
        }
        var clipResult = context.level().clip(
            new ClipContext(
                context.playerEyePositionVec3(),
                BlockCalculations.findClosestCorner(context.playerEyePositionVec3(), event.blockPos()),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                context.player()
            )
        );
        return clipResult.getLocation().distanceTo(event.blockPos().getCenter()) < 1.5;
    }
}
