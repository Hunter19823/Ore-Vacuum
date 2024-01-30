package pie.ilikepiefoo.orevacuum.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import pie.ilikepiefoo.orevacuum.OreVacuum;
import pie.ilikepiefoo.orevacuum.api.vacuums.impl.DefaultBlockTraversalHandler;
import pie.ilikepiefoo.orevacuum.item.VacuumItem;
import pie.ilikepiefoo.orevacuum.item.VacuumItemPyramidShaped;
import pie.ilikepiefoo.orevacuum.item.VacuumItemRectangleShaped;

public class Items {
    public static final Item.Properties DEFAULT_ORE_VAC_PROPERTIES = new Item.Properties()
        .stacksTo(1)
        .defaultDurability(1000)
        .fireResistant();
    // Using minecraft architectury, register a new instance of the OreVac item.
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(OreVacuum.MOD_ID, Registries.ITEM);
    public static final RegistrySupplier<VacuumItem> ORE_VAC_ITEM = ITEMS.register("ore_vacuum", () -> new VacuumItemPyramidShaped(DEFAULT_ORE_VAC_PROPERTIES, 10, new DefaultBlockTraversalHandler.Builder().destroyBlockPredicate((event) -> event.blockState().is(Tags.SUCKABLE_ORES))));
    public static final RegistrySupplier<VacuumItem> TREE_VAC_ITEM = ITEMS.register("tree_vacuum", () -> new VacuumItemPyramidShaped(DEFAULT_ORE_VAC_PROPERTIES, 10, new DefaultBlockTraversalHandler.Builder().destroyBlockPredicate((event) -> event.blockState().is(Tags.SUCKABLE_TREES))));
    public static final RegistrySupplier<VacuumItem> PLANT_VAC_ITEM = ITEMS.register("plant_vacuum", () -> new VacuumItemPyramidShaped(DEFAULT_ORE_VAC_PROPERTIES, 10, new DefaultBlockTraversalHandler.Builder().destroyBlockPredicate((event) -> event.blockState().is(Tags.SUCKABLE_PLANTS))));
    public static final RegistrySupplier<VacuumItem> DEBREE_VAC_ITEM = ITEMS.register("debree_vacuum", () -> new VacuumItemRectangleShaped(DEFAULT_ORE_VAC_PROPERTIES, 3, 3, 10, new DefaultBlockTraversalHandler.Builder().destroyBlockPredicate((event) -> event.blockState().is(Tags.SUCKABLE_DEBREE))));
    public static final RegistrySupplier<VacuumItem> STONE_VAC_ITEM = ITEMS.register("stone_vacuum", () -> new VacuumItemRectangleShaped(DEFAULT_ORE_VAC_PROPERTIES, 3, 3, 10, new DefaultBlockTraversalHandler.Builder().destroyBlockPredicate((event) -> event.blockState().is(Tags.SUCKABLE_STONE))));
    public static final RegistrySupplier<VacuumItem> FLUID_VAC_ITEM = ITEMS.register("fluid_vacuum", () -> new VacuumItemRectangleShaped(DEFAULT_ORE_VAC_PROPERTIES, 3, 3, 10, new DefaultBlockTraversalHandler.Builder().destroyBlockPredicate((event) -> event.blockState().is(Tags.SUCKABLE_FLUID))));

    public static void init() {
        ITEMS.register();
    }
}
