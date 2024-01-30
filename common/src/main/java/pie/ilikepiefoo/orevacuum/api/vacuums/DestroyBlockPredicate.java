package pie.ilikepiefoo.orevacuum.api.vacuums;

import pie.ilikepiefoo.orevacuum.api.event.SuctionBlockEvent;

@FunctionalInterface
public interface DestroyBlockPredicate {
    /**
     * Called when a block is being checked for whether it can be destroyed by the vacuum.
     *
     * @param event The block event data.
     * @return Whether the block can be destroyed.
     */
    boolean canSuckBlock(SuctionBlockEvent event);
}
