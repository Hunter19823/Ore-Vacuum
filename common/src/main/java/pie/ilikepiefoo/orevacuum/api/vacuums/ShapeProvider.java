package pie.ilikepiefoo.orevacuum.api.vacuums;

import org.joml.Vector3i;

import java.util.List;

@FunctionalInterface
public interface ShapeProvider {
    /**
     * Returns a list of coordinates that represent the shape of the vacuum.
     * Each layer is represented by a different array of coordinates.
     *
     * @return The coordinates of the shape.
     */
    List<Vector3i[]> getCoordinates();
}
