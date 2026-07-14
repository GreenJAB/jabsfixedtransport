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

    public static GameRule<Boolean> ICE_MELT_IN_NETHER;

    public static GameRule<Boolean> ELYTRA_DRAG;
    public static GameRule<Integer> ELYTRA_FLY_IN_RAIN;
    public static GameRule<Integer> ELYTRA_DEPLOYMENT_TICKS;
    public static GameRule<Integer> ELYTRA_HIT_CANCEL_TICKS;

    public static void registerGameRules() {
        System.out.println("register GameRules");
        ICE_MELT_IN_NETHER = registerBooleanRule("ice_melt_in_nether", true);

        ELYTRA_DRAG = registerBooleanRule("elytra_drag", false);
        ELYTRA_FLY_IN_RAIN = registerInteger("elytra_fly_in_rain", 0, 0, 2);
        ELYTRA_DEPLOYMENT_TICKS = registerInteger("elytra_deployment_ticks", 15, 0, Integer.MAX_VALUE);
        ELYTRA_HIT_CANCEL_TICKS = registerInteger("elytra_hit_cancel_ticks", 40, 0, Integer.MAX_VALUE);
    }

    private static GameRule<Boolean> registerBooleanRule(String name, boolean defaultValue) {
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
