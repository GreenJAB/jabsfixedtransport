package net.greenjab.jabsfixedtransport.registry.registries;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemGroupRegistry {

    public static final CreativeModeTab JABS_FIXED_TRANSPORT = FabricCreativeModeTab.builder().title(Component.translatable("itemgroup.jabsfixedtransport"))
            .icon( () -> new ItemStack(ItemRegistry.COPPER_RAIL))
            .displayItems(
                     (_, entries) -> {

                         entries.accept(ItemRegistry.DRAGON_FIREWORK_ROCKET);
                         entries.accept(ItemRegistry.MAP_BOOK);
                         entries.accept(ItemRegistry.CHAINMAIL_HORSE_ARMOR);
                         entries.accept(ItemRegistry.NAUTILUS_ARMOR);

                         entries.accept(ItemRegistry.COPPER_RAIL);
                         entries.accept(ItemRegistry.EXPOSED_COPPER_RAIL);
                         entries.accept(ItemRegistry.WEATHERED_COPPER_RAIL);
                         entries.accept(ItemRegistry.OXIDIZED_COPPER_RAIL);
                         entries.accept(ItemRegistry.WAXED_COPPER_RAIL);
                         entries.accept(ItemRegistry.WAXED_EXPOSED_COPPER_RAIL);
                         entries.accept(ItemRegistry.WAXED_WEATHERED_COPPER_RAIL);
                         entries.accept(ItemRegistry.WAXED_OXIDIZED_COPPER_RAIL);
                    }).build();


    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, "jabsfixedtransport", JABS_FIXED_TRANSPORT);
    }
}
