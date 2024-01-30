package pie.ilikepiefoo.orevacuum.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import pie.ilikepiefoo.orevacuum.OreVacuum;

public class Tags {
    public static ResourceLocation SUCKABLE_ORES_TAG_ID = OreVacuum.id("suckable_ores");
    public static ResourceLocation SUCKABLE_TREES_TAG_ID = OreVacuum.id("suckable_trees");
    public static ResourceLocation SUCKABLE_PLANTS_TAG_ID = OreVacuum.id("suckable_plants");
    public static TagKey<Block> SUCKABLE_ORES = TagKey.create(Registries.BLOCK, SUCKABLE_ORES_TAG_ID);
    public static TagKey<Block> SUCKABLE_TREES = TagKey.create(Registries.BLOCK, SUCKABLE_TREES_TAG_ID);
    public static TagKey<Block> SUCKABLE_PLANTS = TagKey.create(Registries.BLOCK, SUCKABLE_PLANTS_TAG_ID);
}
