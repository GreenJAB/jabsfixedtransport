package net.greenjab.jabsfixedtransport.mixin.horse;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @ModifyVariable(method = "refreshDimensions", at = @At(value = "STORE"), ordinal = 1)
    private EntityDimensions smallerHorseInBoat(EntityDimensions newDim){
        Entity E = (Entity)(Object)this;
        if (E instanceof AbstractHorse) {
            if (E.isPassenger()) {
                return EntityDimensions.fixed(newDim.width() * 0.9f, newDim.height());
            }
        }
        return newDim;
    }
}
