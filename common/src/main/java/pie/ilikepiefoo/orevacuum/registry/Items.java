package pie.ilikepiefoo.orevacuum.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import pie.ilikepiefoo.orevacuum.OreVacuum;
import pie.ilikepiefoo.orevacuum.item.OreVac;

import static pie.ilikepiefoo.orevacuum.item.OreVac.ORE_VAC_PROPERTIES;

public class Items {
    // Using minecraft architectury, register a new instance of the OreVac item.
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(OreVacuum.MOD_ID, Registries.ITEM);
    public static final RegistrySupplier<OreVac> ORE_VAC_ITEM = ITEMS.register("ore_vacuum", () -> new OreVac(ORE_VAC_PROPERTIES, 10));

    public static void init() {
        ITEMS.register();
    }
}
