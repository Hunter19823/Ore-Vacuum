package pie.ilikepiefoo.orevacuum.api.vacuums.impl;

import pie.ilikepiefoo.orevacuum.api.event.SuctionBlockEvent;
import pie.ilikepiefoo.orevacuum.api.vacuums.BlockTraversalHandler;
import pie.ilikepiefoo.orevacuum.api.vacuums.BlockVisibilityChecker;
import pie.ilikepiefoo.orevacuum.api.vacuums.DestroyBlockHandler;
import pie.ilikepiefoo.orevacuum.api.vacuums.DestroyBlockPredicate;
import pie.ilikepiefoo.orevacuum.registry.Tags;

public class DefaultBlockTraversalHandler implements BlockTraversalHandler {
    private final BlockVisibilityChecker visibilityChecker;
    private final DestroyBlockPredicate destroyBlockPredicate;
    private final DestroyBlockHandler destroyBlockHandler;

    public DefaultBlockTraversalHandler(BlockVisibilityChecker visibilityChecker, DestroyBlockPredicate destroyBlockPredicate, DestroyBlockHandler destroyBlockHandler) {
        this.visibilityChecker = visibilityChecker;
        this.destroyBlockPredicate = destroyBlockPredicate;
        this.destroyBlockHandler = destroyBlockHandler;
    }

    @Override
    public boolean handleTraversal(SuctionBlockEvent event) {
        if (event.blockState().isAir()) {
            return true;
        }
        if (!this.destroyBlockPredicate.canSuckBlock(event)) {
            return true;
        }
        if (!this.visibilityChecker.canSeeBlock(event)) {
            return true;
        }

        this.destroyBlockHandler.destroy(event);

        if (event.suctionEvent().player().isCreative()) {
            return true;
        }

        return event.suctionEvent().stack().getUseDuration() > 0;
    }

    public static class Builder {
        private BlockVisibilityChecker visibilityChecker = new DefaultBlockVisibilityChecker();
        private DestroyBlockPredicate destroyBlockPredicate = new DefaultDestroyBlockPredicate(Tags.SUCKABLE_ORES);
        private DestroyBlockHandler destroyBlockHandler = new DefaultDestroyBlockHandler();

        public Builder visibilityChecker(BlockVisibilityChecker visibilityChecker) {
            this.visibilityChecker = visibilityChecker;
            return this;
        }

        public Builder destroyBlockPredicate(DestroyBlockPredicate destroyBlockPredicate) {
            this.destroyBlockPredicate = destroyBlockPredicate;
            return this;
        }

        public Builder destroyBlockHandler(DestroyBlockHandler destroyBlockHandler) {
            this.destroyBlockHandler = destroyBlockHandler;
            return this;
        }

        public DefaultBlockTraversalHandler build() {
            return new DefaultBlockTraversalHandler(visibilityChecker, destroyBlockPredicate, destroyBlockHandler);
        }
    }

}
