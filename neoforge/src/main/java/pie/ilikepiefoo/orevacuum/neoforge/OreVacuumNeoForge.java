package pie.ilikepiefoo.orevacuum.neoforge;

import net.neoforged.fml.common.Mod;
import pie.ilikepiefoo.orevacuum.OreVacuum;

@Mod(OreVacuum.MOD_ID)
public class OreVacuumNeoForge {
    public OreVacuumNeoForge() {
        // Register this mod instance with Neoforge's event bus.
        //NeoForge.EVENT_BUS.register(this);
        OreVacuum.init();
    }
}