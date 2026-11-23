package net.greenjab.jabsfixedtransport.mixin.transport;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.PotentSulfurBlockEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PotentSulfurBlockEntity.class)
public abstract class PotentSulfurBlockEntityMixin {

    @ModifyExpressionValue(method = "lambda$static$5", at = @At(value = "FIELD", target = "Lnet/minecraft/world/phys/Vec3;y:D", opcode = Opcodes.GETFIELD))
    private static double geyserElytraLauncher(double original,
                                               @Local Entity entityToBeLaunched) {
        if (entityToBeLaunched instanceof LivingEntity livingEntity && livingEntity.isFallFlying()) return original/2f;
        return original;
    }
}
