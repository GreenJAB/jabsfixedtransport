package net.greenjab.jabsfixedtransport.mixin.horse;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @ModifyVariable(method = "refreshDimensions", at = @At(value = "STORE"), ordinal = 1)
    private EntityDimensions smallerHorseInBoat(EntityDimensions newDim){
        Entity E = (Entity)(Object)this;
        if (E instanceof AbstractHorse) {
            if (E.isPassenger()) return EntityDimensions.fixed(newDim.width() * 0.9f, newDim.height());
        }
        return newDim;
    }

    @Inject(method = "interact", at =
    @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;", ordinal = 0), cancellable = true)
    private void exitBoat(Player player, InteractionHand hand, Vec3 location, CallbackInfoReturnable<InteractionResult> cir) {
        Entity E = (Entity)(Object)this;
        if (E.getVehicle()!=null && E.getVehicle() instanceof VehicleEntity) {
            if (player.isCrouching()) {
                E.stopRiding();
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
}
