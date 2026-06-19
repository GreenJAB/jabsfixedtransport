package net.greenjab.jabsfixedtransport.mixin.other;

import net.greenjab.jabsfixedtransport.registry.registries.ItemRegistry;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Items.class)
public abstract class ItemsMixin {

    @Redirect(method="<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;food(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0 ), slice = @Slice(from =
    @At(value = "CONSTANT", args = "stringValue=sweet_berries"), to =
    @At(value = "FIELD",target = "Lnet/minecraft/world/item/Items;SWEET_BERRIES:Lnet/minecraft/world/item/Item;", opcode = Opcodes.PUTSTATIC)))
    private static Item.Properties speedSweetBerries(Item.Properties instance, FoodProperties foodProperties) {
        return instance.food(Foods.SWEET_BERRIES, ItemRegistry.SWEET_BERRIES_EFFECT);}

    @ModifyArg(method="<clinit>", at = @At( value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;", ordinal = 0), slice = @Slice(from =
    @At(value = "CONSTANT", args = "stringValue=saddle"), to =
    @At(value = "FIELD",target = "Lnet/minecraft/world/item/Items;SADDLE:Lnet/minecraft/world/item/Item;", opcode = Opcodes.PUTSTATIC)))
    private static int stackedSaddles(int max) {
        return 16;}

    @ModifyArg(method="<clinit>", at = @At( value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0), slice = @Slice(from =
    @At(value = "CONSTANT", args = "stringValue=firework_rocket"), to =
    @At(value = "FIELD",target = "Lnet/minecraft/world/item/Items;FIREWORK_ROCKET:Lnet/minecraft/world/item/Item;", opcode = Opcodes.PUTSTATIC)), index = 2)
    private static Item.Properties fireWorkCooldown(Item.Properties properties) {
        return properties.useCooldown(5);}

}
