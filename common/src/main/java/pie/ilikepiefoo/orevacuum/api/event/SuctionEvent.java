package pie.ilikepiefoo.orevacuum.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record SuctionEvent(@NotNull ServerLevel level, @NotNull Player player, @NotNull InteractionHand hand, @NotNull ItemStack stack, Vec3 playerEyePositionVec3, BlockPos playerEyePositionBlockPos) {
    public SuctionEvent(ServerLevel level, Player player, InteractionHand hand) {
        this(level, player, hand, player.getItemInHand(hand), getEyePositionVec3(player), getEyePositionBlockPos(player));
    }

    public static Vec3 getEyePositionVec3(Player player) {
        return new Vec3(player.getX(), player.getEyeY(), player.getZ());
    }

    public static BlockPos getEyePositionBlockPos(Player player) {
        return new BlockPos((int) player.getX(), (int) player.getEyeY(), (int) player.getZ());
    }

    public SuctionEvent(UseOnContext context) {
        this((ServerLevel) context.getLevel(), context.getPlayer(), context.getHand(), context.getItemInHand());
    }

    public SuctionEvent(ServerLevel level, Player player, InteractionHand hand, ItemStack stack) {
        this(level, player, hand, stack, getEyePositionVec3(player), getEyePositionBlockPos(player));
    }
}
