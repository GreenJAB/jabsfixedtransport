package net.greenjab.jabsfixedtransport.mixin.horse;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

@Mixin(ArmorMaterial.class)
public abstract class ArmorMaterialMixin {

    @Unique
    private static final Map<ResourceKey<EquipmentAsset>, Integer> values = Map.of(
            EquipmentAssets.CHAINMAIL, 5,
            EquipmentAssets.GOLD, 7,
            EquipmentAssets.IRON, 9,
            EquipmentAssets.NETHERITE, 15);

    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static Map<ArmorType, Integer> adjustedHorseDefence(Map<ArmorType, Integer> defense,
                                                                    @Local(argsOnly = true) ResourceKey<EquipmentAsset> assetId) {
        if (values.containsKey(assetId)) {
            defense.put(ArmorType.BODY, values.get(assetId));
        }
        return defense;
    }
}
