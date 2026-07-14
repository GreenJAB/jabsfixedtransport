package net.greenjab.jabsfixedtransport.client;

import net.fabricmc.api.ClientModInitializer;
import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.greenjab.jabsfixedtransport.client.map_book.MapBookFilledProperty;
import net.greenjab.jabsfixedtransport.client.models.CustomModelLayers;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;

public class JabsFixedTransportClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientSyncHandler.init();
        ConditionalItemModelProperties.ID_MAPPER.put(JabsFixedTransport.id("map_book/filled"), MapBookFilledProperty.CODEC);
        CustomModelLayers.onRegisterLayers();
    }
}
