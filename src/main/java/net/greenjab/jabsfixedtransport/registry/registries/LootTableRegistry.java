package net.greenjab.jabsfixedtransport.registry.registries;

import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

public class LootTableRegistry {

    public static final ResourceKey<LootTable> TRAIL_RUINS_MAP = registerLoot_Table("map/trail_ruins");

    private static ResourceKey<LootTable> registerLoot_Table(String id) {
        return registerLootTable(ResourceKey.create(Registries.LOOT_TABLE, JabsFixedTransport.id(id)));
    }
    private static ResourceKey<LootTable> registerLootTable(ResourceKey<LootTable> key) {
        if (BuiltInLootTables.LOCATIONS.add(key)) {
            return key;
        } else {
            throw new IllegalArgumentException(key.identifier() + " is already a registered built-in loot table");
        }
    }

    public static void registerLootTable() {
        System.out.println("register LootTables");
    }
}
