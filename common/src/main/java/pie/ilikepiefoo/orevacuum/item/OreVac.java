package pie.ilikepiefoo.orevacuum.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3i;
import pie.ilikepiefoo.orevacuum.BlockCalculations;
import pie.ilikepiefoo.orevacuum.api.SuctionEvent;
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
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
        if (livingEntity instanceof Player) {
            var event = SuctionEvent.create(level, (Player) livingEntity, InteractionHand.MAIN_HAND, itemStack);
            activate(event);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var event = SuctionEvent.of(context);

        if (activate(event)) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        var event = SuctionEvent.create(level, player, interactionHand, player.getItemInHand(interactionHand));

        if (activate(event)) {
            return InteractionResultHolder.success(player.getItemInHand(interactionHand));
        }

        return InteractionResultHolder.fail(player.getItemInHand(interactionHand));
    }

    public boolean activate(SuctionEvent event) {
        Vector3i eyePositionVec3i = new Vector3i((int) event.player().getX(), (int) event.player().getEyeY(), (int) event.player().getZ());
        var lookAngle = event.player().getRotationVector();

        int pitch = BlockCalculations.snapToAngle(lookAngle.x, SNAP_ANGLE);
        int yaw = BlockCalculations.snapToAngle(lookAngle.y, SNAP_ANGLE);

        for (int i = 0; i < this.pyramidBase.size(); i++) {
            BlockCalculations.projectPoints(pitch, yaw, i, eyePositionVec3i, this.pyramidBase.get(i))
                .forEach((position) -> suckUp(position, event));
        }

        return true;
    }

    public boolean suckUp(Vec3i position, SuctionEvent context) {
        BlockPos blockPos = new BlockPos(position);
        BlockState blockState = context.level().getBlockState(blockPos);

        // Destroy the ore block
        if (!isOre(blockState)) {
            return false;
        }

        // Destroy the ore block
//        context.level().setBlock(blockPos, Blocks.GLASS.defaultBlockState(), 3);
        context.level().destroyBlock(blockPos, true);

        // Damage the OreVac
        if (!context.player().isCreative()) {
            context.stack().hurtAndBreak(1, context.player(), (entity) -> entity.broadcastBreakEvent(context.hand()));
        }

        return true;
    }

    public static boolean isOre(BlockState blockState) {
        return blockState.is(Tags.SUCKABLE_ORES);
    }
}
