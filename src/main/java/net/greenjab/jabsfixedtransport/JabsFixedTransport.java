package net.greenjab.jabsfixedtransport;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.greenjab.jabsfixedtransport.network.SyncHandler;
import net.greenjab.jabsfixedtransport.registry.item.map_book.MapBookState;
import net.greenjab.jabsfixedtransport.registry.item.map_book.MapBookStateManager;
import net.greenjab.jabsfixedtransport.registry.registries.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JabsFixedTransport implements ModInitializer {
    public static final String NAMESPACE = "jabsfixedtransport";
    public static final String MOD_NAME = "Jabs Fixed Transport";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    public static MinecraftServer SERVER = null;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing " + MOD_NAME);

        SyncHandler.init();

        BlockRegistry.registerBlocks();
        ItemRegistry.registerItems();
        ItemGroupRegistry.register();
        GameRuleRegistry.registerGameRules();
        MapDecorationRegistry.registerMapDecorations();
        LootTableAdditions.registerLootTableAdds();

        CommandRegistrationCallback.EVENT.register((dispatcher, _, _) ->
                dispatcher.register(Commands.literal("mapBookMarker")
                        .then(Commands.argument("id", IntegerArgumentType.integer())
                                .then(Commands.argument("x", StringArgumentType.string())
                                        .then(Commands.argument("z", StringArgumentType.string())
                                                .then(Commands.argument("dim", StringArgumentType.string())
                                                        .executes(JabsFixedTransport::executeMapBookMarker)))))));
    }

    private static int executeMapBookMarker(CommandContext<CommandSourceStack> context) {
        int id = IntegerArgumentType.getInteger(context, "id");
        double x = Double.parseDouble(StringArgumentType.getString(context, "x"));
        double z = Double.parseDouble(StringArgumentType.getString(context, "z"));
        String dim = StringArgumentType.getString(context, "dim");
        MapBookState mapBookState = MapBookStateManager.INSTANCE.getMapBookState(SERVER, id);
        if (mapBookState != null) mapBookState.setMarker(x, z, dim);
        return 1;
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(NAMESPACE, path);
    }
}