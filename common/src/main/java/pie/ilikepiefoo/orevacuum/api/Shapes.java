package pie.ilikepiefoo.orevacuum.api;

import org.joml.Vector3i;
import pie.ilikepiefoo.orevacuum.BlockCalculations;

import java.util.ArrayList;
import java.util.List;

public class Shapes {

    public static List<Vector3i[]> createPyramid(int height) {
        ArrayList<Vector3i[]> layers = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            layers.add(BlockCalculations.calculatePyramidBase(i));
        }
        layers.trimToSize();
        return layers;
    }
}
