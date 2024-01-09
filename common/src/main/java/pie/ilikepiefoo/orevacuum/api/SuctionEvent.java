package pie.ilikepiefoo.orevacuum.api;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record SuctionEvent(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand, @NotNull ItemStack stack) {

    public static SuctionEvent of(UseOnContext context) {
        return create(context.getLevel(), context.getPlayer(), context.getHand(), context.getItemInHand());
    }

    public static SuctionEvent create(Level level, Player player, InteractionHand hand, ItemStack stack) {
        return new SuctionEvent(level, player, hand, stack);
    }
}
