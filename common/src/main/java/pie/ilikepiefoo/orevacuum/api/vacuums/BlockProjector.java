package pie.ilikepiefoo.orevacuum.api.vacuums;

import net.minecraft.core.BlockPos;
import org.joml.Vector3i;

public interface BlockProjector {
    /**
     * Projects a Vector3i such that it is in front of the player.
     *
     * @param pos The position to project.
     * @return The projected position.
     */
    BlockPos project(Vector3i pos);
}
