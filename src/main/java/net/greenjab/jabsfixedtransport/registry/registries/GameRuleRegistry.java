package net.greenjab.jabsfixedtransport.registry.registries;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import net.minecraft.world.level.gamerules.GameRules;
import java.util.function.ToIntFunction;

public class GameRuleRegistry {
    public static final GameRuleCategory JABSFIXEDTRANSPORT = GameRuleCategory.register(JabsFixedTransport.id("aab_jabsfixedtransport"));

    public static GameRule<Integer> TRAIN_MAX_LENGTH;
    public static GameRule<Boolean> TRAIN_CHUNK_LOADING;

    public static GameRule<Boolean> ELYTRA_DRAG;
    public static GameRule<Integer> ELYTRA_FLY_IN_RAIN;
    public static GameRule<Integer> ELYTRA_FIREWORK_NERF;
    public static GameRule<Integer> ELYTRA_DEPLOYMENT_TICKS;
    public static GameRule<Integer> ELYTRA_HIT_CANCEL_TICKS;

    public static GameRule<Boolean> ICE_MELT_IN_NETHER;
    public static GameRule<Boolean> GLOBAL_PLAYER_LOCATOR_BAR;
    public static GameRule<Boolean> REMOVE_VANILLA_NAUTILUS_ARMOUR;

    public static void registerGameRules() {
        System.out.println("register GameRules");

        TRAIN_MAX_LENGTH = registerInteger("train_max_length", 100, 1, 100);
        TRAIN_CHUNK_LOADING = registerBoolean("train_chunk_loading", true);

        ELYTRA_DRAG = registerBoolean("elytra_drag", false);
        ELYTRA_FLY_IN_RAIN = registerInteger("elytra_fly_in_rain", 0, 0, 2);
        ELYTRA_FIREWORK_NERF = registerInteger("elytra_firework_nerf", 1, 0, 2);
        ELYTRA_DEPLOYMENT_TICKS = registerInteger("elytra_deployment_ticks", 15, 0, Integer.MAX_VALUE);
        ELYTRA_HIT_CANCEL_TICKS = registerInteger("elytra_hit_cancel_ticks", 40, 0, Integer.MAX_VALUE);

        ICE_MELT_IN_NETHER = registerBoolean("ice_melt_in_nether", true);
        GLOBAL_PLAYER_LOCATOR_BAR = registerBoolean("global_player_locator_bar", false);
        REMOVE_VANILLA_NAUTILUS_ARMOUR = registerBoolean("remove_vanilla_nautilus_armour", true);
    }

    private static GameRule<Boolean> registerBoolean(String name, boolean defaultValue) {
        return register(name, GameRuleType.BOOL, BoolArgumentType.bool(), Codec.BOOL, defaultValue,
                FeatureFlagSet.of(), GameRuleTypeVisitor::visitBoolean, value -> value ? 1 : 0);
    }

    private static GameRule<Integer> registerInteger(
            final String id, final int defaultValue, final int min, final int max) {
        return register(id, GameRuleType.INT, IntegerArgumentType.integer(min, max), Codec.intRange(min, max),
                defaultValue, FeatureFlagSet.of(), GameRuleTypeVisitor::visitInteger, i -> i);
    }

    private static <T> GameRule<T> register(String name, GameRuleType type,
                                            ArgumentType<T> argumentType, Codec<T> codec, T defaultValue, FeatureFlagSet requiredFeatures,
                                            GameRules.VisitorCaller<T> acceptor, ToIntFunction<T> commandResultSupplier) {
        return Registry.register(BuiltInRegistries.GAME_RULE, JabsFixedTransport.id(name),
                new GameRule<>(GameRuleRegistry.JABSFIXEDTRANSPORT, type, argumentType, acceptor, codec, commandResultSupplier, defaultValue, requiredFeatures));
    }
}
