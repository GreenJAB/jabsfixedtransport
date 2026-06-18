package net.greenjab.jabsfixedtransport.registry.registries;

import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.greenjab.jabsfixedtransport.registry.block.CopperRailBlock;
import net.greenjab.jabsfixedtransport.registry.block.OxidizableRailBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class BlockRegistry {

    public static final Block COPPER_RAIL = register("copper_rail", settings -> new OxidizableRailBlock(WeatheringCopper.WeatherState.UNAFFECTED, settings),
            BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL));
    public static final Block EXPOSED_COPPER_RAIL = register("exposed_copper_rail", settings -> new OxidizableRailBlock(WeatheringCopper.WeatherState.EXPOSED, settings),
            BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL));
    public static final Block WEATHERED_COPPER_RAIL = register("weathered_copper_rail", settings -> new OxidizableRailBlock(WeatheringCopper.WeatherState.WEATHERED, settings),
            BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL));
    public static final Block OXIDIZED_COPPER_RAIL = register("oxidized_copper_rail", settings -> new OxidizableRailBlock(WeatheringCopper.WeatherState.OXIDIZED, settings),
            BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL));

    public static final Block WAXED_COPPER_RAIL = register("waxed_copper_rail", settings -> new CopperRailBlock(WeatheringCopper.WeatherState.UNAFFECTED, settings),
            BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL));
    public static final Block WAXED_EXPOSED_COPPER_RAIL = register("waxed_exposed_copper_rail", settings -> new CopperRailBlock(WeatheringCopper.WeatherState.EXPOSED, settings),
            BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL));
    public static final Block WAXED_WEATHERED_COPPER_RAIL = register("waxed_weathered_copper_rail", settings -> new CopperRailBlock(WeatheringCopper.WeatherState.WEATHERED, settings),
            BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL));
    public static final Block WAXED_OXIDIZED_COPPER_RAIL = register("waxed_oxidized_copper_rail", settings -> new CopperRailBlock(WeatheringCopper.WeatherState.OXIDIZED, settings),
            BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL));

    public static void registerBlocks() {
        System.out.println("register Blocks");
    }

    private static Block register(String id, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
        return register(keyOf(id), factory, settings);
    }
    private static ResourceKey<Block> keyOf(String id) {
        return ResourceKey.create(Registries.BLOCK, JabsFixedTransport.id(id));
    }
    public static Block register(ResourceKey<Block> key, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
        Block block = factory.apply(settings.setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }
}
