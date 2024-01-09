package pie.ilikepiefoo.orevacuum.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3i;
import pie.ilikepiefoo.orevacuum.BlockCalculations;
import pie.ilikepiefoo.orevacuum.registry.Tags;

import java.util.List;

public class OreVac extends Item {
    public static final Logger LOG = LogManager.getLogger();
    public static final Properties ORE_VAC_PROPERTIES = new Properties()
        .stacksTo(1)
        .defaultDurability(1000)
        .fireResistant();
    private static final int SNAP_ANGLE = 15;
    public int range;
    private final List<Vector3i[]> pyramidBase;

    public OreVac(Properties properties, int range) {
        super(properties);
        this.range = range;
        this.pyramidBase = BlockCalculations.createPyramid(range);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var player = context.getPlayer();
        if (player == null) {
            return InteractionResult.FAIL;
        }
        Vector3i eyePositionVec3i = new Vector3i((int) player.getX(), (int) player.getEyeY(), (int) player.getZ());
        var lookAngle = player.getRotationVector();

        int pitch = BlockCalculations.snapToAngle(lookAngle.x, SNAP_ANGLE);
        int yaw = BlockCalculations.snapToAngle(lookAngle.y, SNAP_ANGLE);

        for (int i = 0; i < this.pyramidBase.size(); i++) {
            BlockCalculations.projectPoints(pitch, yaw, i, eyePositionVec3i, this.pyramidBase.get(i))
                .forEach((position) -> suckUp(position, context));
        }
        return InteractionResult.SUCCESS;
    }

    public boolean suckUp(Vec3i position, UseOnContext context) {
        if (context.getPlayer() == null) {
            LOG.error("Player is null on Sucking action! This should not happen!");
            return false;
        }
        BlockPos blockPos = new BlockPos(position);
        BlockState blockState = context.getLevel().getBlockState(blockPos);

        // Destroy the ore block
        if (!isOre(blockState)) {
            return false;
        }

        // Destroy the ore block
        context.getLevel().destroyBlock(blockPos, true);

        // Damage the OreVac
        if (!context.getPlayer().isCreative()) {
            context.getItemInHand().hurtAndBreak(1, context.getPlayer(), (entity) -> entity.broadcastBreakEvent(context.getHand()));
        }

        return true;
    }

    public static boolean isOre(BlockState blockState) {
        return blockState.is(Tags.SUCKABLE_ORES);
    }
}
