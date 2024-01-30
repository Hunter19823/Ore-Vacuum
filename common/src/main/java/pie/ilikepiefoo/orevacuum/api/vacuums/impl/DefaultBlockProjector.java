package pie.ilikepiefoo.orevacuum.api.vacuums.impl;

import net.minecraft.core.BlockPos;
import org.joml.Matrix3d;
import org.joml.Vector3d;
import org.joml.Vector3i;
import pie.ilikepiefoo.orevacuum.api.vacuums.BlockProjector;

public class DefaultBlockProjector implements BlockProjector {
    private final Matrix3d transformationMatrix;
    private final Vector3i offset;

    public DefaultBlockProjector(int pitch, int yaw, Vector3i offset) {
        this.transformationMatrix = new Matrix3d();
        this.transformationMatrix.rotateX(0);
        this.transformationMatrix.rotateY(Math.toRadians(90 - yaw));
        this.transformationMatrix.rotateZ(Math.toRadians(pitch - 90));
        this.offset = offset;
    }


    @Override
    public BlockPos project(Vector3i pos) {
        var transform = this.transformationMatrix
            .transform(new Vector3d(pos.x, pos.y, pos.z))
            .add(offset.x, offset.y, offset.z)
            .round();
        return new BlockPos((int) transform.x, (int) transform.y, (int) transform.z);
    }
}
