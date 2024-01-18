package pie.ilikepiefoo.orevacuum.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
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
        BlockPos eyePosition = BlockCalculations.blockPosFrom(eyePositionVec3i);
        var lookAngle = event.player().getRotationVector();

        int pitch = BlockCalculations.snapToAngle(lookAngle.x, SNAP_ANGLE);
        int yaw = BlockCalculations.snapToAngle(lookAngle.y, SNAP_ANGLE);

        if (event.player().isCrouching()) {
            for (int i = this.pyramidBase.size() - 1; i >= 0; i--) {
                BlockCalculations.projectPoints(pitch, yaw, i, eyePositionVec3i, this.pyramidBase.get(i))
                    .sorted((a, b) -> (int) (b.distSqr(eyePosition) - a.distSqr(eyePosition)))
                    .forEach((position) -> extract(event, position, eyePosition));
            }
            event.player().getCooldowns().addCooldown(this, 10);
        } else {
            for (int i = 0; i < this.pyramidBase.size(); i++) {
                BlockCalculations.projectPoints(pitch, yaw, i, eyePositionVec3i, this.pyramidBase.get(i))
                    .sorted((a, b) -> (int) (a.distSqr(eyePosition) - b.distSqr(eyePosition)))
                    .forEach((position) -> extract(event, position, eyePosition));
            }
            event.player().getCooldowns().addCooldown(this, 2);
        }


        return true;
    }

    private void extract(SuctionEvent event, BlockPos position, BlockPos eyePosition) {
        if (event.level().getBlockState(position).isAir()) {
            return;
        }
        if (canExtract(event, position, eyePosition)) {
            suckUp(position, event);
        }
    }

    private boolean canExtract(SuctionEvent event, BlockPos position, BlockPos eyePosition) {
        if (position.equals(eyePosition)) {
            return true;
        }
        if ((int) position.distSqr(eyePosition) == 1) {
            return true;
        }
        var clipResult = event.level().clip(
            new ClipContext(
                event.playerEyePosition(),
                BlockCalculations.findClosestCorner(event.playerEyePosition(), position),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                event.player()
            )
        );
        return clipResult.getLocation().distanceTo(position.getCenter()) < 1.5;
    }

    public boolean suckUp(Vec3i position, SuctionEvent context) {
        BlockPos blockPos = new BlockPos(position);
        BlockState blockState = context.level().getBlockState(blockPos);


        if (context.player().isCrouching()) {
            context.level().setBlock(blockPos, Blocks.RED_STAINED_GLASS.defaultBlockState(), 3);
            return true;
        }

        // Destroy the ore block
        if (!isOre(blockState)) {
            return false;
        }

        // Destroy the ore block
        if (context.level().isClientSide()) {
            return true;
        }
        BlockEntity blockEntity = blockState.hasBlockEntity() ? context.level().getBlockEntity(blockPos) : null;

        Block.getDrops(blockState, (ServerLevel) context.level(), blockPos, blockEntity, context.player(), context.stack())
            .forEach((itemStack) -> {
                if (context.player().getInventory().add(itemStack)) {
                    return;
                }
                Block.popResource(context.level(), context.player().blockPosition(), itemStack);
            });

        context.level().setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());

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
