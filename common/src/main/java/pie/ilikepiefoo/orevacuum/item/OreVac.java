package pie.ilikepiefoo.orevacuum.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.stream.IntStreams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pie.ilikepiefoo.orevacuum.BlockCalculations;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class OreVac extends Item {
    private static final Logger LOG = LogManager.getLogger();
    public static final Properties ORE_VAC_PROPERTIES = new Properties()
        .stacksTo(1)
        .defaultDurability(1000)
        .fireResistant();
    public int range;

    public OreVac(Properties properties, int range) {
        super(properties);
        this.range = range;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        // The way this orevac will work, is that a cone will be used to determine the area of effect.
        // It will traverse each layer of the cone based on the current level tick and the range of the orevac.
        // It will then check each block in the layer of the cone and if it is an ore, it will be destroyed.
        // The orevac will then be damaged based on the number of blocks destroyed.


        if(level.getGameTime() % 4 != 0)
            return InteractionResultHolder.pass(player.getItemInHand(interactionHand));

        if(level.isClientSide()){
            return InteractionResultHolder.pass(player.getItemInHand(interactionHand));
        }

        var eyePosition = player.getEyePosition();
        var lookAngle = player.getRotationVector();

        var correctedLookAngle = new Vec3(BlockCalculations.snapToAngle(lookAngle.x, 15), BlockCalculations.snapToAngle(lookAngle.y, 30),0);

        // Create an integer stream from 1 to the range of the orevac.
        // Then map each integer to a pyramid layer.
        // Then for each position in the pyramid layer, attempt to suck up the block.
        for (int i = 1; i < range; i++) {
            BlockCalculations.calculatePyramidLayer(eyePosition, correctedLookAngle, i, i+3)
                .forEach((position) -> suckUp(position, level, player, interactionHand));
        }

        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
    }

    public static boolean isOre(BlockState blockState) {
        return blockState.is(BlockTags.MINEABLE_WITH_PICKAXE) &&
            (
                blockState.is(BlockTags.NEEDS_STONE_TOOL) ||
                blockState.is(BlockTags.NEEDS_IRON_TOOL) ||
                blockState.is(BlockTags.NEEDS_DIAMOND_TOOL)
            ) &&
            !blockState.is(BlockTags.REPLACEABLE);
    }

    public boolean suckUp(Vec3i position, Level level, Player player, InteractionHand interactionHand) {
        BlockPos blockPos = new BlockPos(position);
        BlockState blockState = level.getBlockState(blockPos);

        // Destroy the ore block
        if (!isOre(blockState)) {
            return false;
        }

        // Destroy the ore block
        level.destroyBlock(blockPos, true);

        // Damage the OreVac
        if (!player.isCreative())
            player.getItemInHand(interactionHand).hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(interactionHand));

        return true;
    }
}