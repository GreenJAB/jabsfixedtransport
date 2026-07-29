package net.greenjab.jabsfixedtransport.registry.registries;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.greenjab.jabsfixedtransport.registry.other.ExplorationCompassLootFunction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jspecify.annotations.NonNull;

public class LootTableAdditions {

    public static void registerLootTableAdds() {
        System.out.println("register LootTableAdds");
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, JabsFixedTransport.id("exploration_compass"), ExplorationCompassLootFunction.CODEC);

        LootTableEvents.MODIFY.register((key, tableBuilder, source, holder) -> {
            if (key==BuiltInLootTables.SIMPLE_DUNGEON) {
                tableBuilder.pool(LootPool.lootPool().add(LootItem.lootTableItem(Items.AIR).setWeight(2))
                        .add(LootItem.lootTableItem(Items.MAP)
                                .apply(new ExplorationMapFunction.Builder().setDestination(StructureTags.ON_TRIAL_CHAMBERS_MAPS)
                                        .setMapDecoration(MapDecorationTypes.TRIAL_CHAMBERS).setSkipKnownStructures(false).setZoom((byte)2))
                                .apply(SetNameFunction.setName(Component.translatable("filled_map.trial_chambers"), SetNameFunction.Target.ITEM_NAME))).build());
            } else if (key==BuiltInLootTables.PILLAGER_OUTPOST) {
                tableBuilder.pool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.MAP).setWeight(5)
                                .apply(new ExplorationMapFunction.Builder().setDestination(StructureTags.ON_WOODLAND_EXPLORER_MAPS)
                                        .setMapDecoration(MapDecorationTypes.WOODLAND_MANSION).setSkipKnownStructures(false).setZoom((byte)2))
                                .apply(SetNameFunction.setName(Component.translatable("filled_map.mansion"), SetNameFunction.Target.ITEM_NAME)))
                        .add(LootItem.lootTableItem(Items.MAP)
                                .apply(new ExplorationMapFunction.Builder().setDestination(StructureTags.ON_DESERT_VILLAGE_MAPS)
                                        .setMapDecoration(MapDecorationTypes.DESERT_VILLAGE).setSkipKnownStructures(false).setZoom((byte)2))
                                .apply(SetNameFunction.setName(Component.translatable("filled_map.village_desert"), SetNameFunction.Target.ITEM_NAME)))
                        .add(LootItem.lootTableItem(Items.MAP)
                                .apply(new ExplorationMapFunction.Builder().setDestination(StructureTags.ON_PLAINS_VILLAGE_MAPS)
                                        .setMapDecoration(MapDecorationTypes.PLAINS_VILLAGE).setSkipKnownStructures(false).setZoom((byte)2))
                                .apply(SetNameFunction.setName(Component.translatable("filled_map.village_plains"), SetNameFunction.Target.ITEM_NAME)))
                        .add(LootItem.lootTableItem(Items.MAP)
                                .apply(new ExplorationMapFunction.Builder().setDestination(StructureTags.ON_SAVANNA_VILLAGE_MAPS)
                                        .setMapDecoration(MapDecorationTypes.SAVANNA_VILLAGE).setSkipKnownStructures(false).setZoom((byte)2))
                                .apply(SetNameFunction.setName(Component.translatable("filled_map.village_savanna"), SetNameFunction.Target.ITEM_NAME)))
                        .add(LootItem.lootTableItem(Items.MAP)
                                .apply(new ExplorationMapFunction.Builder().setDestination(StructureTags.ON_SNOWY_VILLAGE_MAPS)
                                        .setMapDecoration(MapDecorationTypes.SNOWY_VILLAGE).setSkipKnownStructures(false).setZoom((byte)2))
                                .apply(SetNameFunction.setName(Component.translatable("filled_map.village_snowy"), SetNameFunction.Target.ITEM_NAME)))
                        .add(LootItem.lootTableItem(Items.MAP)
                                .apply(new ExplorationMapFunction.Builder().setDestination(StructureTags.ON_TAIGA_VILLAGE_MAPS)
                                        .setMapDecoration(MapDecorationTypes.TAIGA_VILLAGE).setSkipKnownStructures(false).setZoom((byte)2))
                                .apply(SetNameFunction.setName(Component.translatable("filled_map.village_taiga"), SetNameFunction.Target.ITEM_NAME))).build());
            } else if (key==BuiltInLootTables.BURIED_TREASURE) {
                tableBuilder.pool(LootPool.lootPool().add(LootItem.lootTableItem(Items.MAP)
                                .apply(new ExplorationMapFunction.Builder().setDestination(StructureTags.ON_OCEAN_EXPLORER_MAPS)
                                        .setMapDecoration(MapDecorationTypes.OCEAN_MONUMENT).setSkipKnownStructures(false).setZoom((byte)2))
                                .apply(SetNameFunction.setName(Component.translatable("filled_map.monument"), SetNameFunction.Target.ITEM_NAME))).build());
            } else if (key==BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE) {
                tableBuilder.modifyPools(builder ->
                        builder.add(LootItem.lootTableItem(Items.COMPASS).apply(new ExplorationCompassLootFunction.Builder())));
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

    private static LootPoolSingletonContainer.@NonNull Builder<?> enchantedArmor(HolderLookup.RegistryLookup<Enchantment> enchantments, Item armor, int level, int weight) {
        return LootItem.lootTableItem(armor).setWeight(weight)
                .apply(new EnchantWithLevelsFunction.Builder(ConstantValue.exactly(level))
                        .withOptions(enchantments.get(EnchantmentTags.ON_RANDOM_LOOT).map(named -> named)));
    }
}
