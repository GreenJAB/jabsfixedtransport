package net.greenjab.jabsfixedtransport.registry.registries;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.greenjab.jabsfixedtransport.registry.other.ExplorationCompassLootFunction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.entries.UniformContainerBase;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class LootTableAdditions {

    public static void registerLootTableAdds() {
        System.out.println("register LootTableAdds");
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, JabsFixedTransport.id("exploration_compass"), ExplorationCompassLootFunction.CODEC);

        LootTableEvents.MODIFY.register((key, tableBuilder, _, holder) -> {
            HolderLookup.RegistryLookup<Structure> structures = holder.lookupOrThrow(Registries.STRUCTURE);
            HolderLookup.RegistryLookup<LootTable> lootTables = holder.lookupOrThrow(Registries.LOOT_TABLE);
            if (key==BuiltInLootTables.SIMPLE_DUNGEON) {
                tableBuilder.pool(LootPool.lootPool().add(LootItem.lootTableItem(Items.AIR).setWeight(3))
                        .add(LootItem.lootTableItem(Items.BURIED_TRIAL_CHAMBERS_MAP)
                                .apply(ExplorationMapFunction.makeExplorationMap(structures.getOrThrow(StructureTags.ON_BURIED_TRIAL_CHAMBERS_MAPS))
                                        .setMapDecoration(MapDecorationTypes.TRIAL_CHAMBERS).setSearchRadius(100).setSkipKnownStructures(true)))
                        .add(LootItem.lootTableItem(Items.BURIED_MINESHAFT_MAP)
                                .apply(ExplorationMapFunction.makeExplorationMap(structures.getOrThrow(StructureTags.ON_MINESHAFT_MAPS))
                                        .setMapDecoration(MapDecorationTypes.MINESHAFT).setSearchRadius(100).setSkipKnownStructures(true))).build());
            } else if (key==BuiltInLootTables.PILLAGER_OUTPOST) {
                tableBuilder.pool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.WOODLAND_MANSION_MAP).setWeight(5)
                                .apply(ExplorationMapFunction.makeExplorationMap(structures.getOrThrow(StructureTags.ON_WOODLAND_MANSION_MAPS))
                                        .setMapDecoration(MapDecorationTypes.WOODLAND_MANSION).setSearchRadius(100).setSkipKnownStructures(true)))
                        .add(LootItem.lootTableItem(Items.DESERT_VILLAGE_MAP).setWeight(5)
                                .apply(ExplorationMapFunction.makeExplorationMap(structures.getOrThrow(StructureTags.ON_DESERT_VILLAGE_MAPS))
                                        .setMapDecoration(MapDecorationTypes.DESERT_VILLAGE).setSearchRadius(100).setSkipKnownStructures(true)))
                        .add(LootItem.lootTableItem(Items.PLAINS_VILLAGE_MAP).setWeight(5)
                                .apply(ExplorationMapFunction.makeExplorationMap(structures.getOrThrow(StructureTags.ON_PLAINS_VILLAGE_MAPS))
                                        .setMapDecoration(MapDecorationTypes.PLAINS_VILLAGE).setSearchRadius(100).setSkipKnownStructures(true)))
                        .add(LootItem.lootTableItem(Items.SAVANNA_VILLAGE_MAP).setWeight(5)
                                .apply(ExplorationMapFunction.makeExplorationMap(structures.getOrThrow(StructureTags.ON_SAVANNA_VILLAGE_MAPS))
                                        .setMapDecoration(MapDecorationTypes.SAVANNA_VILLAGE).setSearchRadius(100).setSkipKnownStructures(true)))
                        .add(LootItem.lootTableItem(Items.SNOWY_VILLAGE_MAP).setWeight(5)
                                .apply(ExplorationMapFunction.makeExplorationMap(structures.getOrThrow(StructureTags.ON_SNOWY_VILLAGE_MAPS))
                                        .setMapDecoration(MapDecorationTypes.SNOWY_VILLAGE).setSearchRadius(100).setSkipKnownStructures(true)))
                        .add(LootItem.lootTableItem(Items.TAIGA_VILLAGE_MAP).setWeight(5)
                                .apply(ExplorationMapFunction.makeExplorationMap(structures.getOrThrow(StructureTags.ON_TAIGA_VILLAGE_MAPS))
                                        .setMapDecoration(MapDecorationTypes.TAIGA_VILLAGE).setSearchRadius(100).setSkipKnownStructures(true))).build());
            } else if (key==BuiltInLootTables.BURIED_TREASURE) {
                tableBuilder.pool(LootPool.lootPool().add(LootItem.lootTableItem(Items.OCEAN_MONUMENT_MAP)
                        .apply(ExplorationMapFunction.makeExplorationMap(structures.getOrThrow(StructureTags.ON_OCEAN_MONUMENT_MAPS))
                                .setMapDecoration(MapDecorationTypes.OCEAN_MONUMENT).setSearchRadius(100).setSkipKnownStructures(true))).build());
            } else if (key==BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE) {
                tableBuilder.modifyPools(builder ->
                        builder.add(LootItem.lootTableItem(Items.COMPASS).apply(new ExplorationCompassLootFunction.Builder())));
            } else if (key==BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY||key==BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY||key==BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY) {
                tableBuilder.modifyPools(builder ->
                        builder.add(NestedLootTable.lootTableReference(lootTables.getOrThrow(LootTableRegistry.TRAIL_RUINS_MAP))).build());
            } else if (key==BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY) {
                tableBuilder.pool(LootPool.lootPool().add(LootItem.lootTableItem(Items.AIR).setWeight(3))
                        .add(LootItem.lootTableItem(Items.DESERT_PYRAMID_MAP)
                                .apply(ExplorationMapFunction.makeExplorationMap(structures.getOrThrow(StructureTags.ON_DESERT_PYRAMID_MAPS))
                                        .setMapDecoration(MapDecorationTypes.DESERT_PYRAMID).setSearchRadius(100).setSkipKnownStructures(true)))
                        .add(LootItem.lootTableItem(Items.JUNGLE_PYRAMID_MAP)
                                .apply(ExplorationMapFunction.makeExplorationMap(structures.getOrThrow(StructureTags.ON_JUNGLE_PYRAMID_MAPS))
                                        .setMapDecoration(MapDecorationTypes.JUNGLE_TEMPLE).setSearchRadius(100).setSkipKnownStructures(true))).build());
            } else if (key==BuiltInLootTables.SHIPWRECK_MAP) {
                tableBuilder.pool(LootPool.lootPool().add(LootItem.lootTableItem(Items.AIR).setWeight(2))
                        .add(LootItem.lootTableItem(Items.WARM_OCEAN_RUINS_MAP)
                                .apply(ExplorationMapFunction.makeExplorationMap(structures.getOrThrow(StructureTags.ON_OCEAN_RUIN_WARM_MAPS))
                                        .setMapDecoration(MapDecorationTypes.OCEAN_RUIN_WARM).setSearchRadius(100).setSkipKnownStructures(true))).build());
            } else if (key==BuiltInLootTables.VILLAGE_CARTOGRAPHER) {
                tableBuilder.pool(LootPool.lootPool().add(LootItem.lootTableItem(Items.AIR).setWeight(2))
                        .add(LootItem.lootTableItem(Items.ABANDONED_CAMP_MAP)
                                .apply(ExplorationMapFunction.makeExplorationMap(structures.getOrThrow(StructureTags.ABANDONED_CAMP))
                                        .setMapDecoration(MapDecorationTypes.ABANDONED_CAMP).setSearchRadius(100).setSkipKnownStructures(true))).build());
            }
	    });

        LootTableEvents.MODIFY.register((key, tableBuilder, source, holder) -> {
            HolderLookup.RegistryLookup<Enchantment> enchantments = holder.lookupOrThrow(Registries.ENCHANTMENT);
            if (key==BuiltInLootTables.SIMPLE_DUNGEON) {
                tableBuilder.pool(LootPool.lootPool().add(LootItem.lootTableItem(Items.AIR).setWeight(16))
                        .add(enchantedArmor(enchantments, Items.LEATHER_HORSE_ARMOR, 20, 3))
                        .add(enchantedArmor(enchantments, ItemRegistry.CHAINMAIL_HORSE_ARMOR, 20, 3))
                        .add(enchantedArmor(enchantments, Items.COPPER_HORSE_ARMOR, 20, 3))
                        .add(enchantedArmor(enchantments, Items.IRON_HORSE_ARMOR, 20, 2))
                        .add(enchantedArmor(enchantments, Items.GOLDEN_HORSE_ARMOR, 20, 2))
                        .add(enchantedArmor(enchantments, Items.DIAMOND_HORSE_ARMOR, 20, 1))
                        .build());
            } else if (key==BuiltInLootTables.DESERT_PYRAMID) {
                tableBuilder.pool(LootPool.lootPool().add(LootItem.lootTableItem(Items.AIR).setWeight(25))
                        .add(enchantedArmor(enchantments, Items.LEATHER_HORSE_ARMOR, 10, 3))
                        .add(enchantedArmor(enchantments, ItemRegistry.CHAINMAIL_HORSE_ARMOR, 10, 3))
                        .add(enchantedArmor(enchantments, Items.COPPER_HORSE_ARMOR, 10, 3))
                        .add(enchantedArmor(enchantments, Items.IRON_HORSE_ARMOR, 10, 2))
                        .add(enchantedArmor(enchantments, Items.GOLDEN_HORSE_ARMOR, 10, 2))
                        .add(enchantedArmor(enchantments, Items.DIAMOND_HORSE_ARMOR, 10, 1))
                        .build());
            } else if (key==BuiltInLootTables.END_CITY_TREASURE) {
                tableBuilder.pool(LootPool.lootPool().add(LootItem.lootTableItem(Items.AIR).setWeight(8))
                        .add(enchantedArmor(enchantments, Items.IRON_HORSE_ARMOR, 30, 1))
                        .add(enchantedArmor(enchantments, Items.GOLDEN_HORSE_ARMOR, 30, 1))
                        .add(enchantedArmor(enchantments, Items.DIAMOND_HORSE_ARMOR, 30, 2))
                        .build());
            } else if (key==BuiltInLootTables.ANCIENT_CITY) {
                tableBuilder.pool(LootPool.lootPool().add(LootItem.lootTableItem(Items.AIR).setWeight(4))
                        .add(enchantedArmor(enchantments, Items.DIAMOND_HORSE_ARMOR, 30, 1))
                        .build());
            }
        });
    }

    private static UniformContainerBase.Builder<?> enchantedArmor(HolderLookup.RegistryLookup<Enchantment> enchantments, Item armor, int level, int weight) {
        return LootItem.lootTableItem(armor).setWeight(weight)
                .apply(new EnchantWithLevelsFunction.Builder(ContextIntProviders.exactly(level))
                        .withOptions(enchantments.get(EnchantmentTags.ON_RANDOM_LOOT).map(named -> named)));
    }
}
