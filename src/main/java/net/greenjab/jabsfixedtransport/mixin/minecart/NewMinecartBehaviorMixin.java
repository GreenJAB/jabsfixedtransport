package net.greenjab.jabsfixedtransport.mixin.minecart;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import net.greenjab.jabsfixedtransport.registry.other.FixedFurnaceMinecartEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
import net.minecraft.world.entity.vehicle.minecart.MinecartFurnace;
import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import net.greenjab.jabsfixedtransport.registry.block.CopperRailBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NewMinecartBehavior.class)
public abstract class NewMinecartBehaviorMixin extends MinecartBehavior {
    private static final double MIN_RAIL_STEP = 1.0E-5;

    protected NewMinecartBehaviorMixin(AbstractMinecart minecart) {
        super(minecart);
    }

    @Inject(method = "getMaxSpeed", at = @At("HEAD"),cancellable = true)
    private void copperSpeed(ServerLevel level, CallbackInfoReturnable<Double> cir) {
        BlockState state = level.getBlockState(this.minecart.blockPosition());
        Vec3 velocity = this.minecart.getDeltaMovement();
        double u =40;
        if (state.getBlock() instanceof BaseRailBlock) {
            u = 8.0;
            if (state.getBlock() instanceof CopperRailBlock) u = CopperRailBlock.getMaxVelocity(state);
        }
        if (this.minecart.isInWater()) u /= 2.0;
        u /= 20.0;
        u = Math.max(u, velocity.horizontalDistance()*0.9);
        if (state.getBlock() instanceof PoweredRailBlock && !(this.minecart instanceof MinecartFurnace)) u = 8.0/20.0;
        cir.setReturnValue(u);
        cir.cancel();
    }

    @Inject(method = "calculateSlopeSpeed", at = @At("HEAD"), cancellable = true)
    private void lessSlowDown(Vec3 deltaMovement, RailShape shape, CallbackInfoReturnable<Vec3> cir) {
        if (this.minecart instanceof MinecartFurnace) {
            cir.setReturnValue(deltaMovement);
            cir.cancel();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void noTrainPartUpdate(CallbackInfo ci) {
        for (Entity entity :this.minecart.getPassengers()) {
            entity.fallDistance = 0;
            this.minecart.fallDistance = 0;
        }
        if (this.minecart.tickCount>200) {
            this.minecart.removeTag("train");
            this.minecart.removeTag("trainMove");
            this.minecart.removeTag("trainTP");
        }
        if (this.minecart.entityTags().contains("trainMove")) ci.cancel();
    }

    @ModifyExpressionValue(method = "moveAlongTrack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior;calculateTrackSpeed(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$TrackIteration;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/properties/RailShape;)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 skipPowerRailSlowdown(Vec3 original) {
        if (this.minecart.noPhysics || this.minecart.entityTags().contains("train")) return this.getDeltaMovement().horizontal();
        return original;
    }

    @Inject(method = "stepAlongTrack", at = @At("HEAD"), cancellable = true)
    private void stableTrainPrediction(BlockPos pos, RailShape shape, double movementLeft, CallbackInfoReturnable<Double> cir) {
        if (!this.minecart.noPhysics || !this.minecart.entityTags().contains("train")) return;

        if (movementLeft < MIN_RAIL_STEP) {
            cir.setReturnValue(0.0);
            return;
        }

        Vec3 movement = this.getDeltaMovement().horizontal();
        if (movement.lengthSqr() < MIN_RAIL_STEP * MIN_RAIL_STEP) {
            this.setDeltaMovement(Vec3.ZERO);
            cir.setReturnValue(0.0);
            return;
        }

        Pair<Vec3i, Vec3i> exits = AbstractMinecart.exits(shape);
        boolean sloped = exits.getFirst().getY() != exits.getSecond().getY();
        Vec3 firstExit = new Vec3(exits.getFirst()).scale(0.5).horizontal();
        Vec3 secondExit = new Vec3(exits.getSecond()).scale(0.5).horizontal();
        Vec3 exit = movement.dot(firstExit) < movement.dot(secondExit) ? secondExit : firstExit;
        Vec3 exitDirection = exit.normalize();
        boolean descending = isDescending(movement, shape);

        Vec3 exitPosition = pos.getBottomCenter()
                .add(exit)
                .add(0.0, 0.1, 0.0)
                .add(exitDirection.scale(MIN_RAIL_STEP));
        if (sloped && !descending) exitPosition = exitPosition.add(0.0, 1.0, 0.0);

        Vec3 oldPosition = this.position();
        Vec3 horizontalToExit = exitPosition.subtract(oldPosition).horizontal();
        double distanceToExit = horizontalToExit.length();
        Vec3 travelDirection = distanceToExit > MIN_RAIL_STEP
                ? horizontalToExit.scale(1.0 / distanceToExit)
                : exitDirection;
        double distanceMoved = Math.min(movementLeft, distanceToExit);
        double distanceRemainingOnRail = Math.max(0.0, distanceToExit - distanceMoved);

        Vec3 newPosition;
        if (distanceMoved >= distanceToExit) {
            newPosition = exitPosition;
        } else {
            Vec3 horizontalPosition = oldPosition.add(travelDirection.scale(distanceMoved));
            double y = sloped
                    ? exitPosition.y + (descending ? distanceRemainingOnRail : -distanceRemainingOnRail)
                    : exitPosition.y;
            newPosition = new Vec3(horizontalPosition.x, y, horizontalPosition.z);
        }

        this.minecart.move(MoverType.SELF, newPosition.subtract(oldPosition));
        double speed = movement.length();
        this.setDeltaMovement(
                travelDirection.x * speed,
                sloped ? (descending ? -speed : speed) : 0.0,
                travelDirection.z * speed
        );
        cir.setReturnValue(Math.max(0.0, movementLeft - distanceMoved));
    }

    private static boolean isDescending(Vec3 movement, RailShape shape) {
        return switch (shape) {
            case ASCENDING_EAST -> movement.x < 0.0;
            case ASCENDING_WEST -> movement.x > 0.0;
            case ASCENDING_NORTH -> movement.z > 0.0;
            case ASCENDING_SOUTH -> movement.z < 0.0;
            default -> false;
        };
    }

    @Inject(method = "moveAlongTrack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    private void powerRailFurnaceMinecart(ServerLevel level, CallbackInfo ci, @Local BlockState currentState) {
        if (this.minecart instanceof FixedFurnaceMinecartEntity fixedFurnaceMinecartEntity) {
            if (currentState.is(Blocks.POWERED_RAIL))
                fixedFurnaceMinecartEntity.powerRailSetLit = currentState.getValue(PoweredRailBlock.POWERED)?1:-1;
        }
    }
    @Inject(method = "getSlowdownFactor", at = @At("HEAD"), cancellable = true)
    private void consistentSpeeds(CallbackInfoReturnable<Double> cir){
        if (this.minecart.tickCount<0 || this.minecart.entityTags().contains("train") || this.minecart.entityTags().contains("trainTP")) {
            cir.setReturnValue(1.0);
            cir.cancel();
        }
    }
    @ModifyExpressionValue(method = "pushEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;isVehicle()Z"))
    private boolean dontPush(boolean original){
        if (this.minecart.entityTags().contains("train")) return false;
        return original;
    }
}
