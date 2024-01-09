package pie.ilikepiefoo.orevacuum;

import net.minecraft.resources.ResourceLocation;
import pie.ilikepiefoo.orevacuum.registry.Items;

public class OreVacuum {
    public static final String MOD_ID = "orevacuum";
    public static final String MOD_NAME = "Ore Vacuum";

    public static void init() {
        Items.init();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
