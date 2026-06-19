package net.greenjab.jabsfixedtransport;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import java.util.*;

public class FixedMinecraftEnchantmentHelper {

    public static int enchantLevel(ItemStack stack, String name) {
        int level = 0;
        ItemEnchantments itemEnchantmentsComponent = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        for (Holder<Enchantment> e : stack.getEnchantments().keySet()) {
            if (e.getRegisteredName().toLowerCase().contains(name.toLowerCase())) {
                level += itemEnchantmentsComponent.getLevel(e);
            }
        }
        return level;
    }
}
