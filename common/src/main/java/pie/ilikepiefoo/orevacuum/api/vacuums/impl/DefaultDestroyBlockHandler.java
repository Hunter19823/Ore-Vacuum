package pie.ilikepiefoo.orevacuum.api.vacuums.impl;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import pie.ilikepiefoo.orevacuum.api.event.SuctionBlockEvent;
import pie.ilikepiefoo.orevacuum.api.event.SuctionEvent;
import pie.ilikepiefoo.orevacuum.api.vacuums.DestroyBlockHandler;

public class DefaultDestroyBlockHandler implements DestroyBlockHandler {

    @Override
    public void destroy(SuctionBlockEvent event) {
        SuctionEvent context = event.suctionEvent();
        BlockState blockState = context.level().getBlockState(event.blockPos());

        // Destroy the ore block
        BlockEntity blockEntity = blockState.hasBlockEntity() ? context.level().getBlockEntity(event.blockPos()) : null;

        Block.getDrops(blockState, context.level(), event.blockPos(), blockEntity, context.player(), context.stack())
            .forEach((itemStack) -> {
                if (context.player().getInventory().add(itemStack)) {
                    return;
                }
                Block.popResource(context.level(), context.player().blockPosition(), itemStack);
            });

        context.level().setBlockAndUpdate(event.blockPos(), Blocks.AIR.defaultBlockState());

        // Damage the OreVac
        if (!context.player().isCreative()) {
            context.stack().hurtAndBreak(1, context.player(), (entity) -> entity.broadcastBreakEvent(context.hand()));
        }
    }
}
