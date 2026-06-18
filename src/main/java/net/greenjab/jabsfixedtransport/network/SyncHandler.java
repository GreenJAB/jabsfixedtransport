package net.greenjab.jabsfixedtransport.network;

public class SyncHandler {
    public static void init() {
        MapBookOpenPayload.register();
        MapBookSyncPayload.register();
        MapPositionPayload.register();
        MapPositionRequestPayload.register();
        TrainPayload.register();
    }
}
