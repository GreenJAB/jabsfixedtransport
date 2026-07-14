package net.greenjab.jabsfixedtransport.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.greenjab.jabsfixedtransport.CustomData;
import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;tryToStartFallFlying()Z"))
    private boolean failRealTest(LocalPlayer instance, Operation<Boolean> original) {
        if (JabsFixedTransport.gameRules.elytra_deployment_ticks!=0) return false;
        return original.call(instance);
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isFallFlying()Z"))
    private void addMyTest(CallbackInfo ci) {
        LocalPlayer CPE = (LocalPlayer)(Object)this;
        if (CPE.input.keyPresses.jump() && JabsFixedTransport.gameRules.elytra_deployment_ticks!=0) {
            if (!CPE.onClimbable() && !CPE.onGround() && !CPE.isPassenger() && !CPE.hasEffect(MobEffects.LEVITATION) &&
                (JabsFixedTransport.gameRules.elytra_fly_in_rain==0?!CPE.isInWaterOrRain():!CPE.isInWater()) &&
                !CPE.isInLava() &&
                CustomData.getData(CPE, "airTime") > JabsFixedTransport.gameRules.elytra_deployment_ticks) {
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
