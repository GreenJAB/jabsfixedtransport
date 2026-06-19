package net.greenjab.jabsfixedtransport.client.models;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.minecraft.client.model.animal.equine.AbstractEquineModel;
import net.minecraft.client.model.animal.equine.DonkeyModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;

public class CustomModelLayers {
    public static ModelLayerLocation MULE_ARMOR = register("mule_armor", "main");

    private static ModelLayerLocation register(String path, String layer) {
        return new ModelLayerLocation(JabsFixedTransport.id(path), layer);
    }

    public static void onRegisterLayers() {
        ModelLayerRegistry.registerModelLayer(MULE_ARMOR, CustomModelLayers::createMuleArmorModel);
    }

    private static LayerDefinition createMuleArmorModel() {
        return LayerDefinition.create(AbstractEquineModel.createBodyMesh(new CubeDeformation(0.1F)), 64, 64)
                .apply(DonkeyModel.DONKEY_TRANSFORMER)
                .apply(MeshTransformer.scaling(0.92F));
    }
}
