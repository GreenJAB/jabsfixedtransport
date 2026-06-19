package net.greenjab.jabsfixedtransport.registry.registries;

import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.greenjab.jabsfixedtransport.registry.other.ExplorationCompassLootFunction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class LootTableRegistry {


    public static void registerLootTable() {
        System.out.println("register LootTables");
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, JabsFixedTransport.id("exploration_compass"), ExplorationCompassLootFunction.CODEC);
    }
}
