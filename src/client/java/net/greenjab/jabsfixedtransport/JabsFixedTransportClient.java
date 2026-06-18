package net.greenjab.jabsfixedtransport;

import net.fabricmc.api.ClientModInitializer;
import net.greenjab.jabsfixedtransport.map_book.MapBookFilledProperty;
import net.greenjab.jabsfixedtransport.models.CustomModelLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;

public class JabsFixedTransportClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientSyncHandler.init();
        ConditionalItemModelProperties.ID_MAPPER.put(JabsFixedTransport.id("map_book/filled"), MapBookFilledProperty.CODEC);
        CustomModelLayers.onRegisterLayers();
    }

    public static boolean usingCustomContainers() {
        return Minecraft.getInstance().getResourcePackRepository().getSelectedPacks().stream()
                .anyMatch(pack -> pack.location().id().toLowerCase().contains("recolourful_containers"));
    }
}
