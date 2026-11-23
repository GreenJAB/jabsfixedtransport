package net.greenjab.jabsfixedtransport.mixin.transport;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
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
import net.minecraft.world.level.biome.Biome;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Unique private static final float MODIFIER = 0.1F;@Unique private static final float DEG = (float) (Math.PI / 180F);
    @Shadow protected int autoSpinAttackTicks;
    @Shadow protected boolean dead;
    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);
    @Shadow public abstract @Nullable MobEffectInstance getEffect(Holder<MobEffect> effect);
    @Shadow public abstract boolean isFallFlying();

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @ModifyExpressionValue(method = "travelInWater", at =
    @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z"))
    private boolean boostWhenRiptide(boolean original) {
        return original || this.autoSpinAttackTicks>0;
    }

    @Inject(method = "aiStep", at =
    @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;checkAutoSpinAttack(Lnet/minecraft/world/phys/AABB;Lnet/minecraft/world/phys/AABB;)V"))
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

    @WrapOperation(method = "canGlide", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z"))
    private boolean cancelElytraInLiquid(LivingEntity instance, Holder<MobEffect> effect, Operation<Boolean> original) {
        if (instance instanceof Player) {
            return !(!original.call(instance, effect) &&
                    (JabsFixedTransport.gameRules.elytra_fly_in_rain==0?!instance.isInWaterOrRain():!instance.isInWater()) &&
                    !instance.isInLava() &&
                    CustomData.getData(instance, "airTime") > JabsFixedTransport.gameRules.elytra_deployment_ticks);
        } else return !(!original.call(instance, effect) && instance.isInWater() && !instance.isInLava());
    }

    @ModifyConstant(method = "jumpFromGround", constant = @Constant(doubleValue = 0.2))
    private double speedJump(double constant) {
        float i = 0;
        if (this.hasEffect(MobEffects.SPEED)) i += 1+ this.getEffect(MobEffects.SPEED).getAmplifier();
        if (this.hasEffect(MobEffects.JUMP_BOOST)) i +=0.5f*( 1+ this.getEffect(MobEffects.JUMP_BOOST).getAmplifier());
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

    @ModifyConstant(method = "updateFallFlyingMovement", constant = @Constant(doubleValue = 3.2))
    private double elytraUp(double original, @Local(argsOnly = true) Vec3 movement){
        return 3.2-Math.max(movement.y, 0)*0.2*JabsFixedTransport.gameRules.elytra_drag/100.0;
    }

    @WrapOperation(method = "updateFallFlyingMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;multiply(DDD)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 elytraDrag(Vec3 instance, double xScale, double yScale, double zScale, Operation<Vec3> original) {
        if (JabsFixedTransport.gameRules.elytra_drag>0) {
            int weather = (this.isInWaterOrRain() || this.level().precipitationAt(this.blockPosition()) == Biome.Precipitation.SNOW) ? 3 : 1;
            double near = 0;
            double[] lastDrag = getDrag();
            for (int i = 0; i < 8; i++) {
                Vec3 v = new Vec3(15 * Mth.sin(DEG * i * 45), 15 * Mth.cos(DEG * i * 45), 0).xRot(DEG * this.getXRot()).yRot(DEG * this.getYRot());
                double dist = this.level().clip(new ClipContext(this.position(), this.position().add(v),
                        ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, this)).distanceTo(this);
                double d2 = Math.max(1-dist/200.0, Math.max(lastDrag[i] - 0.005, 0));
                near+=d2;
                lastDrag[i]=d2;
            }
            setDrag(lastDrag);
            double drag = 0.00047*(8-near)*(8-near) * weather;
            drag = (drag-0.01)*(JabsFixedTransport.gameRules.elytra_drag/100.0) +0.01;
            return original.call(instance, 1 - drag, yScale, 1 - drag);
        }
        return original.call(instance, xScale, yScale, zScale);
    }

    @Inject(method = "travel", at = @At(value = "HEAD"))
    private void elytraDrag2(CallbackInfo ci) {
        if (!this.isFallFlying()) {
            if (this.level().isClientSide()) CustomData.clientDrag = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0};
            else CustomData.serverDrag.remove(this.uuid);
        }
    }

    @Unique private double[] getDrag() {
        if (this.level().isClientSide()) return CustomData.clientDrag;
        else return CustomData.serverDrag.getOrDefault(this.uuid, new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0});
    }
    @Unique private void setDrag(double[] drag) {
        if (this.level().isClientSide()) CustomData.clientDrag = drag;
        else CustomData.serverDrag.put(this.uuid, drag);
    }
}
