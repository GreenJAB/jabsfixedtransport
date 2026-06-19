package net.greenjab.jabsfixedtransport.mixin.horse;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @Inject(method = {"isPrimaryItem", "canEnchant", "isSupportedItem"}, at = @At(value = "HEAD"), cancellable = true)
    private void otherChecks(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Enchantment enchantment = (Enchantment)(Object)this;
        if (stack.getComponents().has(DataComponents.EQUIPPABLE)) {
            if (stack.getComponents().get(DataComponents.EQUIPPABLE).equipSound() == SoundEvents.HORSE_ARMOR) {
                cir.setReturnValue(enchantment.canEnchant(Items.DIAMOND_BOOTS.getDefaultInstance()) && !enchantment.canEnchant(Items.FLINT_AND_STEEL.getDefaultInstance()));
                cir.cancel();
            }
        }
    }

    @ModifyVariable(method = "matchingSlot", at = @At(value = "HEAD"), argsOnly = true, ordinal = 0)
    private EquipmentSlot feetEnchantsOnHorse(EquipmentSlot slot){
        if (slot==EquipmentSlot.BODY) {
            return EquipmentSlot.FEET;
        }
        return slot;
    }
}
