package net.greenjab.jabsfixedtransport.registry.registries;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.greenjab.jabsfixedtransport.registry.other.ExplorationCompassLootFunction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;

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
    }
}
