package net.greenjab.jabsfixedtransport.registry.registries;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
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
    public static GameRule<Boolean> ICE_MELT_IN_NETHER;

    public static void registerGameRules() {
        System.out.println("register GameRules");
        ICE_MELT_IN_NETHER = registerBooleanRule("ice_melt_in_nether", GameRuleCategory.UPDATES, true);
    }

    private static GameRule<Boolean> registerBooleanRule(String name, GameRuleCategory category, boolean defaultValue) {
        return register(name, category, GameRuleType.BOOL, BoolArgumentType.bool(), Codec.BOOL, defaultValue,
                FeatureFlagSet.of(), GameRuleTypeVisitor::visitBoolean, value -> value ? 1 : 0
        );
    }

    private static <T> GameRule<T> register( String name, GameRuleCategory category, GameRuleType type,
            ArgumentType<T> argumentType, Codec<T> codec, T defaultValue,  FeatureFlagSet requiredFeatures,
            GameRules.VisitorCaller<T> acceptor, ToIntFunction<T> commandResultSupplier
    ) {
        return Registry.register(
                BuiltInRegistries.GAME_RULE, JabsFixedTransport.id(name), new GameRule<>(category, type, argumentType, acceptor, codec, commandResultSupplier, defaultValue, requiredFeatures)
        );
    }
}
