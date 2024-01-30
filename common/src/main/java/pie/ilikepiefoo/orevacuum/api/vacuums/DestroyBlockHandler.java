package pie.ilikepiefoo.orevacuum.api.vacuums;

import pie.ilikepiefoo.orevacuum.api.event.SuctionBlockEvent;

@FunctionalInterface
public interface DestroyBlockHandler {
    /**
     * Called when a block is being destroyed.
     * All logic related to what happens to a block upon being
     * vacuumed should be handled here.
     *
     * @param event The block event data.
     */
    void destroy(SuctionBlockEvent event);
}
