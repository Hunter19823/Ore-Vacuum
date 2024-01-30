package pie.ilikepiefoo.orevacuum.api;

import org.joml.Vector3i;
import pie.ilikepiefoo.orevacuum.BlockCalculations;
import pie.ilikepiefoo.orevacuum.api.event.SuctionBlockEvent;
import pie.ilikepiefoo.orevacuum.api.event.SuctionEvent;
import pie.ilikepiefoo.orevacuum.api.vacuums.BlockProjector;
import pie.ilikepiefoo.orevacuum.api.vacuums.BlockTraversalHandler;
import pie.ilikepiefoo.orevacuum.api.vacuums.ShapeProvider;
import pie.ilikepiefoo.orevacuum.api.vacuums.impl.DefaultBlockProjector;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class VacuumLogic {
    private final int SNAP_ANGLE = 15;
    private final BlockTraversalHandler traversalHandler;
    private final ShapeProvider shapeProvider;

    public VacuumLogic(ShapeProvider shapeProvider, BlockTraversalHandler traversalHandler) {
        this.traversalHandler = traversalHandler;
        this.shapeProvider = shapeProvider;
    }

    public void activateVacuum(SuctionEvent event) {
        var lookAngle = event.player().getRotationVector();
        var offset = new Vector3i((int) event.player().getX(), (int) event.player().getEyeY(), (int) event.player().getZ());
        int pitch = BlockCalculations.snapToAngle(lookAngle.x, SNAP_ANGLE);
        int yaw = BlockCalculations.snapToAngle(lookAngle.y, SNAP_ANGLE);
        var pointProjector = new DefaultBlockProjector(pitch, yaw, offset);

        traverseAllBlocks(event, pointProjector);
    }

    public void traverseAllBlocks(SuctionEvent event, BlockProjector pointProjector) {
        for (int i = 0; i < this.shapeProvider.getCoordinates().size(); i++) {
            AtomicBoolean continueTraversal = new AtomicBoolean(true);
            Stream.of(this.shapeProvider.getCoordinates().get(i))
                .map(pointProjector::project)
                .map((point) -> new SuctionBlockEvent(event, point))
                .sorted((a, b) -> (int) (a.getDistanceFromEyeToBlock() - b.getDistanceFromEyeToBlock()))
                .forEach((blockEvent) -> {
                    if (continueTraversal.get()) {
                        continueTraversal.set(this.traversalHandler.handleTraversal(blockEvent));
                    }
                });
            // Stop early if we can't continue.
            if (!continueTraversal.get()) {
                break;
            }
        }
    }

    public static class Builder {
        private ShapeProvider shapeProvider;
        private BlockTraversalHandler traversalHandler;


        public Builder setShapeProvider(ShapeProvider shapeProvider) {
            this.shapeProvider = shapeProvider;
            return this;
        }

        public Builder setTraversalHandler(BlockTraversalHandler traversalHandler) {
            this.traversalHandler = traversalHandler;
            return this;
        }

        public VacuumLogic build() {
            return new VacuumLogic(shapeProvider, traversalHandler);
        }
    }
}
