package net.greenjab.jabsfixedtransport.mixin.other;

import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.greenjab.jabsfixedtransport.network.GameRuleStatus;
import net.greenjab.jabsfixedtransport.network.Networking;
import net.greenjab.jabsfixedtransport.registry.item.map_book.MapBookState;
import net.greenjab.jabsfixedtransport.registry.item.map_book.MapBookStateManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "tickServer", at = @At("RETURN"))
    private void loadWorld(CallbackInfo ci) {
        MinecraftServer SW = (MinecraftServer)(Object) this;
        synchronized (Networking.SERVER_LOCK) {
            JabsFixedTransport.SERVER = SW;
            Networking.SERVER_LOCK.notifyAll();
        }
        if (SW.getTickCount()%99==0) GameRuleStatus.sendData(SW);
        for (int id : MapBookStateManager.INSTANCE.currentBooks) {
            MapBookState state = MapBookStateManager.INSTANCE.getMapBookState(SW, id);
            if (state != null) state.sendData(SW, id);
        }
        MapBookStateManager.INSTANCE.currentBooks = (new ArrayList<>());
    }
}
