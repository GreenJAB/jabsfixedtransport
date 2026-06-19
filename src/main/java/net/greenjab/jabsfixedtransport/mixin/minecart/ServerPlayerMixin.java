package net.greenjab.jabsfixedtransport.mixin.minecart;

import net.greenjab.jabsfixedtransport.CustomData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "disconnect", at = @At("HEAD"))
    private void dismountTrain(CallbackInfo ci){
        ServerPlayer SPE = (ServerPlayer)(Object)this;
        Entity vehicle = SPE.getVehicle();
        if (vehicle instanceof AbstractMinecart minecart) {
            if (minecart.entityTags().contains("train"))  {
                SPE.stopRiding();
            }
        }
    }

    @Inject(method = "doTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;tick(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void airTime(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        int airTime = CustomData.getData(player, "airTime");// player.getEntityWorld().getScoreboard().getOrCreateScore(player, player.getEntityWorld().getScoreboard().getNullableObjective("airTime")).getScore();//.getNullableObjective("airTime").getScoreboard().get
        if (player.onGround()|| player.isPassenger() || player.onClimbable() || player.isInWater()) airTime=0;
        else if (player.getAbilities().flying) airTime = 10;
        else airTime++;
        if (player.isAutoSpinAttack()) airTime =20;
        CustomData.setData(player, "airTime", airTime);
    }
}
