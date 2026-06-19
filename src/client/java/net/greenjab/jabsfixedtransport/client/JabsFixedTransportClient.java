package net.greenjab.jabsfixedtransport.client;

import net.fabricmc.api.ClientModInitializer;
import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.greenjab.jabsfixedtransport.client.map_book.MapBookFilledProperty;
import net.greenjab.jabsfixedtransport.client.models.CustomModelLayers;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;

public class JabsFixedTransportClient implements ClientModInitializer {

    public static EquipmentClientInfo chainmailModel = createHumanoidAndHorseModel("chainmail");
    public static EquipmentClientInfo scuteNautilusArmor = EquipmentClientInfo.builder()
            .addLayers(EquipmentClientInfo.LayerType.NAUTILUS_BODY, EquipmentClientInfo.Layer.onlyIfDyed(Identifier.withDefaultNamespace("armadillo_scute"), false))
            .addLayers(EquipmentClientInfo.LayerType.NAUTILUS_BODY, EquipmentClientInfo.Layer.onlyIfDyed(Identifier.withDefaultNamespace("armadillo_scute_overlay"), true))
            .build();

    @Override
    public void onInitializeClient() {
        ClientSyncHandler.init();
        ConditionalItemModelProperties.ID_MAPPER.put(JabsFixedTransport.id("map_book/filled"), MapBookFilledProperty.CODEC);
        CustomModelLayers.onRegisterLayers();
    }

    private static EquipmentClientInfo createHumanoidAndHorseModel(String id) {
        return EquipmentClientInfo.builder()
                .addHumanoidLayers(Identifier.withDefaultNamespace(id))
                .addLayers(EquipmentClientInfo.LayerType.HORSE_BODY, EquipmentClientInfo.Layer.leatherDyeable(Identifier.withDefaultNamespace(id), false))
                .build();
    }
}
