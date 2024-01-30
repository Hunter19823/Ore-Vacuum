package pie.ilikepiefoo.orevacuum.api.vacuums.impl;

import org.joml.Vector3i;
import pie.ilikepiefoo.orevacuum.api.Shapes;
import pie.ilikepiefoo.orevacuum.api.vacuums.ShapeProvider;

import java.util.List;

public record DefaultShapeProvider(List<Vector3i[]> coordinates) implements ShapeProvider {

    public static DefaultShapeProvider createPyramid(int height) {
        return new DefaultShapeProvider(Shapes.createPyramid(height));
    }

    @Override
    public List<Vector3i[]> getCoordinates() {
        return coordinates();
    }

}
