package net.greenjab.jabsfixedtransport.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.greenjab.jabsfixedtransport.registry.registries.GameRuleRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.gamerules.GameRules;


public class GameRuleStatus {
    public boolean elytra_drag;
    public int elytra_fly_in_rain;
    public int elytra_deployment_ticks;
    public int elytra_hit_cancel_ticks;

    public static final Codec<GameRuleStatus> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.BOOL.fieldOf("elytra_drag").forGetter(rules -> rules.elytra_drag),
                            Codec.INT.fieldOf("elytra_fly_in_rain").forGetter(rules -> rules.elytra_fly_in_rain),
                            Codec.INT.fieldOf("elytra_deployment_ticks").forGetter(rules -> rules.elytra_deployment_ticks),
                            Codec.INT.fieldOf("elytra_hit_cancel_ticks").forGetter(rules -> rules.elytra_hit_cancel_ticks)
                    )
                    .apply(instance, GameRuleStatus::new)
    );

    public GameRuleStatus(boolean elytra_drag, int elytra_fly_in_rain, int elytra_deployment_ticks, int elytra_hit_cancel_ticks){
        this.elytra_drag = elytra_drag;
        this.elytra_fly_in_rain = elytra_fly_in_rain;
        this.elytra_deployment_ticks = elytra_deployment_ticks;
        this.elytra_hit_cancel_ticks = elytra_hit_cancel_ticks;
    }

    public GameRuleStatus(){
    }

    public void updateRules(GameRules rules) {
        this.elytra_drag = rules.get(GameRuleRegistry.ELYTRA_DRAG);
        this.elytra_fly_in_rain = rules.get(GameRuleRegistry.ELYTRA_FLY_IN_RAIN);
        this.elytra_deployment_ticks = rules.get(GameRuleRegistry.ELYTRA_DEPLOYMENT_TICKS);
        this.elytra_hit_cancel_ticks = rules.get(GameRuleRegistry.ELYTRA_HIT_CANCEL_TICKS);
    }

    void toPacket(FriendlyByteBuf buf) {
        buf.writeBoolean(elytra_drag);
        buf.writeInt(elytra_fly_in_rain);
        buf.writeInt(elytra_deployment_ticks);
        buf.writeInt(elytra_hit_cancel_ticks);
    }

    static GameRuleStatus fromPacket(FriendlyByteBuf buf) {
        GameRuleStatus p = new GameRuleStatus();
        p.elytra_drag = buf.readBoolean();
        p.elytra_fly_in_rain = buf.readInt();
        p.elytra_deployment_ticks = buf.readInt();
        p.elytra_hit_cancel_ticks = buf.readInt();
        return p;
    }

    public static void sendData(MinecraftServer server) {
        JabsFixedTransport.gameRules.updateRules(server.getGameRules());
        GameRulePayload payload = new GameRulePayload(JabsFixedTransport.gameRules);
        server.getPlayerList().getPlayers().forEach(player -> ServerPlayNetworking.send(player, payload));
    }
}
