package net.greenjab.jabsfixedtransport.client.mixin;

import net.greenjab.jabsfixedtransport.client.DonkeyArmorRenderStateAccess;
import net.minecraft.client.renderer.entity.state.DonkeyRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DonkeyRenderState.class)
public abstract class DonkeyRenderStateMixin implements DonkeyArmorRenderStateAccess {
    @Unique
    private ItemStack armor = ItemStack.EMPTY;

    @Override
    public ItemStack jabsfixedtransport$getArmor() {
        return this.armor;
    }

    @Override
    public void jabsfixedtransport$setArmor(ItemStack spotted) {
        this.armor = spotted;
    }
}