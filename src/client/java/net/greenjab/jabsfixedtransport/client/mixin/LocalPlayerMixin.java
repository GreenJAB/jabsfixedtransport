package net.greenjab.jabsfixedtransport.client.mixin;

import net.greenjab.jabsfixedtransport.CustomData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;tryToStartFallFlying()Z"))
    private boolean failRealTest(LocalPlayer instance) {
        return false;
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isFallFlying()Z"))
    private void addMyTest(CallbackInfo ci) {
        LocalPlayer CPE = (LocalPlayer)(Object)this;
        if (CPE.input.keyPresses.jump()) {
            if (!CPE.onClimbable() && !CPE.onGround() && !CPE.isPassenger() && !CPE.hasEffect(MobEffects.LEVITATION) &&
                (CPE.level().getDifficulty().getId()>1?!CPE.isInWaterOrRain():!CPE.isInWater()) &&
                !CPE.isInLava() &&
                CustomData.getData(CPE, "airTime") > 15) {
                if (CPE.tryToStartFallFlying()) {
                    CPE.connection.send(new ServerboundPlayerCommandPacket(CPE, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
                }
            }
        }
    }
    @Inject(method = "vehicleCanSprint", at = @At("HEAD"), cancellable = true)
    private void horsesCanSprint(Entity vehicle, CallbackInfoReturnable<Boolean> cir){
        if (vehicle instanceof AbstractHorse) {
            cir.setReturnValue(true);
        }
    }
}
