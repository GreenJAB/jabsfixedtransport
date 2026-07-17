package net.greenjab.jabsfixedtransport.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.greenjab.jabsfixedtransport.client.screens.MapBookScreen;
import net.greenjab.jabsfixedtransport.network.*;
import net.greenjab.jabsfixedtransport.registry.item.map_book.MapBookState;
import net.greenjab.jabsfixedtransport.registry.item.map_book.MapBookStateManager;
import net.greenjab.jabsfixedtransport.registry.item.map_book.MapStateAccessor;
import net.greenjab.jabsfixedtransport.registry.other.FixedFurnaceMinecartEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import java.util.ArrayList;

/** Credit: Nettakrim, Squeek502, Bawnorton */
public class ClientSyncHandler {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(GameRulePayload.PACKET_ID, ClientSyncHandler::gamerule);
        ClientPlayNetworking.registerGlobalReceiver(MapBookOpenPayload.PACKET_ID, ClientSyncHandler::mapBookOpen);
        ClientPlayNetworking.registerGlobalReceiver(MapBookSyncPayload.PACKET_ID, ClientSyncHandler::mapBookSync);
        ClientPlayNetworking.registerGlobalReceiver(MapPositionPayload.PACKET_ID, ClientSyncHandler::mapPosition);
        ClientPlayNetworking.registerGlobalReceiver(TrainPayload.PACKET_ID, ClientSyncHandler::train);
    }

    private static void gamerule(GameRulePayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(()-> JabsFixedTransport.gameRules = payload.rules());
    }

    private static void mapBookOpen(MapBookOpenPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> context.client().setScreen(new MapBookScreen(payload.itemStack())));
    }

    private static void mapBookSync(MapBookSyncPayload payload, ClientPlayNetworking.Context context) {
        if (payload.mapIDs().length > 0) {
            context.client().execute(() -> {
                ArrayList<Integer> ints = new ArrayList<>();
                for (int i = 0; i < payload.mapIDs().length;i++) {
                    ints.add(payload.mapIDs()[i]);
                }
                MapBookStateManager.INSTANCE.putClientMapBookState(
                        payload.bookID(),
                        new MapBookState(ints, payload.players(), payload.marker())
                );
            });
        }
    }

    private static void mapPosition(MapPositionPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(()-> {
            ClientLevel level = context.client().level;
            if (level != null) {
                MapItemSavedData mapstate = level.getMapData(payload.mapIdComponent());
                if (mapstate != null) {
                    ((MapStateAccessor)mapstate).jabsfixedtransport$setPosition(payload.centerX(), payload.centerZ());
                }
            }
        });
    }

    private static void train(TrainPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(()-> {
            ClientLevel level = context.client().level;
            if (level != null) {
                Entity entity = level.getEntity((payload.train().getFirst()));
                if (entity instanceof FixedFurnaceMinecartEntity furnaceMinecart) {
                    furnaceMinecart.setTrainClient(payload.train());
                }
            }
        });
    }
}
