package pie.ilikepiefoo.orevacuum;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix3d;
import org.joml.Vector3d;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BlockCalculations {
    private static final Logger LOG = LogManager.getLogger();

    public static List<Vector3i[]> createPyramid(int height) {
        ArrayList<Vector3i[]> pyramid = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            pyramid.add(calculatePyramidBase(i));
        }
        pyramid.trimToSize();
        return pyramid;
    }

    public static Vector3i[] calculatePyramidBase(int range) {
        var positions = new ArrayList<Vector3i>();
        int index = 0;
        positions.add(new Vector3i(0, 0, 0));
        for (int x = 1; x < range; x++) {
            positions.add(new Vector3i(x, 0, 0));
            positions.add(new Vector3i(-x, 0, 0));
        }
        for (int z = 1; z < range; z++) {
            positions.add(new Vector3i(0, 0, z));
            positions.add(new Vector3i(0, 0, -z));
        }
        for (int x = 1; x < range; x++) {
            for (int z = 1; z < range; z++) {
                positions.add(new Vector3i(x, 0, z));
                positions.add(new Vector3i(-x, 0, z));
                positions.add(new Vector3i(x, 0, -z));
                positions.add(new Vector3i(-x, 0, -z));
            }
        }
        var output = positions.toArray(new Vector3i[0]);
        positions.clear();
        return output;
    }

    public static Stream<BlockPos> projectPoints(int pitch, int yaw, int distanceFromOffset, Vector3i offset, Vector3i[] points) {
        var rotationMatrix = new Matrix3d();
        rotationMatrix.rotateX(0);
        rotationMatrix.rotateY(Math.toRadians(90 - yaw));
        rotationMatrix.rotateZ(Math.toRadians(pitch - 90));

        return Stream.of(points).map(vec3i -> {
            var transform = rotationMatrix
                .transform(new Vector3d(vec3i.x, -distanceFromOffset, vec3i.z))
                .add(offset.x, offset.y, offset.z)
                .round();
            return new BlockPos((int) transform.x, (int) transform.y, (int) transform.z);
        });
    }

    public static Stream<Vec3i> calculatePyramidLayer(Vec3 eyePosition, Vec3 directionVector, int distance, int radius) {
        Stream.Builder<Vec3i> positions = Stream.builder();

        // Create a pyramid layer based on the width of the pyramid.
        // The pyramid layer will be a square based pyramid.
        // The pyramid layer will be rotated based on the look angle.
        // The pyramid layer will be centered on the eye position.
        // The pyramid layer will be distance blocks away from the eye position that the pyramid layer should be placed at.
        // The pyramid layer will be (2*radius+1) blocks wide.
        // The pyramid layer will be (2*radius+1) blocks long.

        // Start by creating a base layer that is at 0,0,0 that is (2*radius+1) blocks wide and long.
        for (int x = 0; x <= radius; x++) {
            for (int z = 0; z <= radius; z++) {
                if (x == 0 && z == 0) {
                    positions.add(new Vec3i(0, 0, 0));
                    continue;
                }
                if (x == 0) {
                    positions.add(new Vec3i(0, 0, z));
                    positions.add(new Vec3i(0, 0, -z));
                    continue;
                }
                if (z == 0) {
                    positions.add(new Vec3i(x, 0, 0));
                    positions.add(new Vec3i(-x, 0, 0));
                    continue;
                }
                // Add the 4 corners of the layer
                positions.add(new Vec3i(x, 0, z));
                positions.add(new Vec3i(-x, 0, z));
                positions.add(new Vec3i(x, 0, -z));
                positions.add(new Vec3i(-x, 0, -z));
            }
        }

        // Calculate the rotation matrix
        var rotationMatrix = new Matrix3d();
        rotationMatrix.rotateX(0);
        rotationMatrix.rotateY(Math.toRadians(90 - directionVector.y));
        rotationMatrix.rotateZ(Math.toRadians(directionVector.x - 90));

        // Apply rotation to each point in the pyramid layer
        return positions.build().map(vec3i -> {
            var transform = rotationMatrix
                .transform(new Vector3d(vec3i.getX(), -distance, vec3i.getZ()));
            var newTransform = transform
                .add(eyePosition.x, eyePosition.y, eyePosition.z);
            return new Vec3i((int) Math.round(newTransform.x), (int) Math.round(newTransform.y), (int) Math.round(newTransform.z));
        }).distinct();
    }

    public static int snapToAngle(double angle, double snapAngle) {
        // Ensure the snapAngle is between 0 and 360 degrees
        snapAngle = snapAngle % 360;

        // Calculate the remainder when dividing the angle by the snapAngle
        double remainder = angle % snapAngle;

        // Adjust the angle to snap to the nearest multiple of snapAngle
        if (remainder < snapAngle / 2) {
            return (int) (angle - remainder);
        } else {
            return (int) (angle + (snapAngle - remainder));
        }
    }

}
