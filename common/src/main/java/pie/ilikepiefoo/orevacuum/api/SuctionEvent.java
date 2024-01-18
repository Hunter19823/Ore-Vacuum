package pie.ilikepiefoo.orevacuum.api;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record SuctionEvent(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand, @NotNull ItemStack stack, Vec3 playerEyePosition) {
    public SuctionEvent(Level level, Player player, InteractionHand hand, ItemStack stack) {
        this(level, player, hand, stack, new Vec3(player.getX(), player.getEyeY(), player.getZ()));
    }

    public SuctionEvent(Level level, Player player, InteractionHand hand) {
        this(level, player, hand, player.getItemInHand(hand), new Vec3(player.getX(), player.getEyeY(), player.getZ()));
    }

    public static SuctionEvent of(UseOnContext context) {
        return create(context.getLevel(), context.getPlayer(), context.getHand(), context.getItemInHand());
    }

    public static SuctionEvent create(Level level, Player player, InteractionHand hand, ItemStack stack) {
        return new SuctionEvent(level, player, hand, stack);
    }
}
