package net.greenjab.jabsfixedtransport.mixin.transport;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.greenjab.jabsfixedtransport.CustomData;
import net.greenjab.jabsfixedtransport.JabsFixedTransport;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;
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
                    (JabsFixedTransport.gameRules.elytra_fly_in_rain==0?!instance.isInWaterOrRain():!instance.isInWater()) &&
                    !instance.isInLava() &&
                    CustomData.getData(instance, "airTime") > JabsFixedTransport.gameRules.elytra_deployment_ticks);
        } else {
            return !(!instance.hasEffect(effect) && instance.isInWater() && !instance.isInLava());
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
        if (LE instanceof Player && JabsFixedTransport.gameRules.elytra_hit_cancel_ticks!=0) {
            if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                CustomData.setData(LE, "airTime", JabsFixedTransport.gameRules.elytra_deployment_ticks-JabsFixedTransport.gameRules.elytra_hit_cancel_ticks);
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

    @WrapOperation(method = "updateFallFlyingMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;multiply(DDD)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 elytraDrag(Vec3 instance, double xScale, double yScale, double zScale, Operation<Vec3> original) {
        if (JabsFixedTransport.gameRules.elytra_drag) {
            double drag;
            int near = 0;
            for (int i = 0; i < 8; i++) {
                Vec3 v = new Vec3(11 * Mth.sin(DEG * i * 45), 11 * Mth.cos(DEG * i * 45), 0).yRot(DEG * this.getYRot());
                if (this.level().clip(new ClipContext(this.position(), this.position().add(v),
                        ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, this)).distanceTo(this) < 100) near++;
            }
            drag = 0.00025 * (8 - near) * (8 - near) * (this.isInWaterOrRain() ? 2 : 1);
            return original.call(instance, 1 - drag, yScale, 1 - drag);
        }
        return original.call(instance, xScale, yScale, zScale);
    }
}
