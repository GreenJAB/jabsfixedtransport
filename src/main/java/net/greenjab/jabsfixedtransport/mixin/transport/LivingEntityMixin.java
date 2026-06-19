package net.greenjab.jabsfixedtransport.mixin.transport;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.greenjab.jabsfixedtransport.CustomData;
import net.greenjab.jabsfixedtransport.ModTags;
import net.greenjab.jabsfixedtransport.registry.registries.MapDecorationRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Unique
    private static final float MODIFIER = 0.1F;
    @Unique
    private static final float DEG = (float) (Math.PI / 180F);

    @Shadow
    protected int autoSpinAttackTicks;

    @Shadow
    protected boolean dead;

    @Shadow
    public abstract boolean hasEffect(Holder<MobEffect> effect);

    @Shadow
    public abstract @Nullable MobEffectInstance getEffect(Holder<MobEffect> effect);

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    /**
     * Reduces water drag when using riptide.
     */
    @ModifyExpressionValue(
            method = "travelInWater", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z"
    )
    )
    private boolean boostWhenRiptide(boolean original) {
        return original || this.autoSpinAttackTicks>0;
    }

    /**
     * Applies constant acceleration when using riptide and touching water.
     */
    @Inject(
            method = "aiStep", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;checkAutoSpinAttack(Lnet/minecraft/world/phys/AABB;Lnet/minecraft/world/phys/AABB;)V"
    )
    )
    private void accelerateWhenRiptide(CallbackInfo ci) {
        if (!isInWater()) return;
        float f = getYRot();
        float g = getXRot();
        float h = -Mth.sin(f * DEG) * Mth.cos(g * DEG);
        float k = -Mth.sin(g * DEG);
        float l = Mth.cos(f * DEG) * Mth.cos(g * DEG);
        float m = Mth.sqrt(h * h + k * k + l * l);
        h *= MODIFIER / m;
        k *= MODIFIER / m;
        l *= MODIFIER / m;
        push(h, k, l);
    }

    @Redirect(method = "canGlide", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z"))
    private boolean cancelElytraInLiquid(LivingEntity instance, Holder<MobEffect> effect) {
        if (instance instanceof Player) {
            return !(!instance.hasEffect(effect) &&
                     (instance.level().getDifficulty().getId()>1?!instance.isInWaterOrRain():!instance.isInWater()) &&
                     !instance.isInLava() &&
                     CustomData.getData(instance, "airTime") > 15);
        } else {
            return !(!instance.hasEffect(effect) &&
                     (instance.level().getDifficulty().getId()>1?!instance.isInWaterOrRain():!instance.isInWater()) &&
                     !instance.isInLava());
        }
    }

    @ModifyConstant(method = "jumpFromGround", constant = @Constant(doubleValue = 0.2))
    private double speedJump(double constant) {
        float i = 0;
        if (this.hasEffect(MobEffects.SPEED)) {
            i += 1+ this.getEffect(MobEffects.SPEED).getAmplifier();
        }
        if (this.hasEffect(MobEffects.JUMP_BOOST)) {
            i +=0.5f*( 1+ this.getEffect(MobEffects.JUMP_BOOST).getAmplifier());
        }
        return constant+0.05F*i;
    }

    @Inject(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V"))
    private void cancelElytraOnHit(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir){
        LivingEntity LE = (LivingEntity)(Object)this;
        if (LE instanceof Player) {
            if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                CustomData.setData(LE, "airTime", -25);
            }
        }
    }

    @ModifyConstant(method = "getJumpBoostPower", constant = @Constant(floatValue = 1.0f))
    private float betterJumpBoost(float original){
        return 2.0f;
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void dropOutpostMap(DamageSource source, CallbackInfo ci){

        LivingEntity LE = ((LivingEntity) (Object) this);
        if (!LE.isRemoved() && !this.dead) {
            if (LE instanceof Pillager PE) {
                if (PE.entityTags().contains("map")) {
                    ServerLevel serverWorld = (ServerLevel) PE.level();
                    BlockPos blockPos = serverWorld.findNearestMapStructure(ModTags.ON_OUTPOST_MAPS, PE.blockPosition(), 50, true);
                    if (blockPos != null) {
                        ItemStack itemStack = MapItem.create(serverWorld, blockPos.getX(), blockPos.getZ(), (byte)2, true, true);
                        MapItem.renderBiomePreviewMap(serverWorld, itemStack);
                        MapItemSavedData.addTargetDecoration(itemStack, blockPos, "+", MapDecorationRegistry.PILLAGER_OUTPOST);
                        itemStack.set(DataComponents.ITEM_NAME, Component.translatable("filled_map.outpost"));
                        PE.drop(itemStack, true, false);
                    }
                }
            }
        }
    }

}
