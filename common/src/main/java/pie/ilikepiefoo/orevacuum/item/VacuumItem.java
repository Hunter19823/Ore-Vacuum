package pie.ilikepiefoo.orevacuum.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pie.ilikepiefoo.orevacuum.api.VacuumLogic;
import pie.ilikepiefoo.orevacuum.api.event.SuctionEvent;

public abstract class VacuumItem extends Item {
    private final VacuumLogic logic;

    public VacuumItem(Properties properties, VacuumLogic logic) {
        super(properties);
        this.logic = logic;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (!(livingEntity instanceof Player player)) {
            return;
        }
        var event = new SuctionEvent(serverLevel, player, InteractionHand.MAIN_HAND, itemStack);

        logic.activateVacuum(event);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (!(context.getLevel() instanceof ServerLevel)) {
            return InteractionResult.PASS;
        }
        var event = new SuctionEvent(context);

        logic.activateVacuum(event);

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResultHolder.pass(player.getItemInHand(interactionHand));
        }
        var event = new SuctionEvent(serverLevel, player, interactionHand);

        logic.activateVacuum(event);

        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
    }
}
