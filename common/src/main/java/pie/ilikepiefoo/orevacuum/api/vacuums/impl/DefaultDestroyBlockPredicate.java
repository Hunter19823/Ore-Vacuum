package pie.ilikepiefoo.orevacuum.api.vacuums.impl;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import pie.ilikepiefoo.orevacuum.api.event.SuctionBlockEvent;
import pie.ilikepiefoo.orevacuum.api.vacuums.DestroyBlockPredicate;

public record DefaultDestroyBlockPredicate(TagKey<Block> allowedBlocks) implements DestroyBlockPredicate {

    @Override
    public boolean canSuckBlock(SuctionBlockEvent event) {
        return event.blockState().is(allowedBlocks);
    }
}
