package pie.ilikepiefoo.orevacuum.fabric;

import net.fabricmc.api.ModInitializer;
import pie.ilikepiefoo.orevacuum.OreVacuum;

public class OreVacuumFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        OreVacuum.init();
    }
}