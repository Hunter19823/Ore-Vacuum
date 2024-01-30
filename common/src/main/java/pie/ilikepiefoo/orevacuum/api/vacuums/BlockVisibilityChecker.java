package pie.ilikepiefoo.orevacuum.api.vacuums;

import pie.ilikepiefoo.orevacuum.api.event.SuctionBlockEvent;

@FunctionalInterface
public interface BlockVisibilityChecker {

    /**
     * Checks if a block can be seen by the person using the vacuum.
     *
     * @param event The block event data.
     * @return Whether the block can be seen.
     */
    boolean canSeeBlock(SuctionBlockEvent event);
}
