package pie.ilikepiefoo.orevacuum.api.vacuums;

import pie.ilikepiefoo.orevacuum.api.event.SuctionBlockEvent;

@FunctionalInterface
public interface BlockTraversalHandler {

    /**
     * Called when a block is being traversed for the purpose of being destroyed.
     *
     * @param event The block event data.
     * @return Whether to continue traversal.
     */
    boolean handleTraversal(SuctionBlockEvent event);

}
