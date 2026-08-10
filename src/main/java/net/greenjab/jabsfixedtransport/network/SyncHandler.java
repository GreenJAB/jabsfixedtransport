package net.greenjab.jabsfixedtransport.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;

public class SyncHandler {
    public static void init() {
        MapBookOpenPayload.register();
        MapBookSyncPayload.register();
        MapPositionPayload.register();
        MapPositionRequestPayload.register();
        TrainPayload.register();
        HorseDismountPayload.register();
        GameRulePayload.register();

        ServerPlayNetworking.registerGlobalReceiver(HorseDismountPayload.PACKET_ID, SyncHandler::horse_dimount);
    }

    private static void horse_dimount(HorseDismountPayload payload, ServerPlayNetworking.Context context) {
        context.server().execute(()-> {
            Entity entity = context.player().level().getEntity(payload.horse());
            if (entity instanceof AbstractHorse horse) {
                horse.stopRiding();
            }
        });
    }
}
