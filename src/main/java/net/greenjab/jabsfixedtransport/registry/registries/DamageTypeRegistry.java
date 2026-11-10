package net.greenjab.jabsfixedtransport.registry.registries;

import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

public class DamageTypeRegistry {

    public static final ResourceKey<DamageType> TRAIN = ResourceKey.create(Registries.DAMAGE_TYPE, JabsFixedTransport.id("train"));

    public static void registerDamageTypes() {
        System.out.println("register DamageTypes");
    }
}
