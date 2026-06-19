package net.greenjab.jabsfixedtransport.registry.registries;

import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.greenjab.jabsfixedtransport.registry.item.map_book.MapBookAdditionsComponent;
import net.greenjab.jabsfixedtransport.registry.item.map_book.MapBookItem;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.level.block.Block;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ItemRegistry {

    public static final Item DRAGON_FIREWORK_ROCKET = register(
            "dragon_firework_rocket", FireworkRocketItem::new, new Item.Properties().useCooldown(1.0F)
                    .component(DataComponents.FIREWORKS, new Fireworks(1, List.of()))
    );
    public static final Item MAP_BOOK = register("map_book", MapBookItem::new, new Item.Properties().stacksTo(16));
    public static final DataComponentType<MapBookAdditionsComponent> MAP_BOOK_ADDITIONS = registerComponent("map_book_additions", (builder) -> builder.persistent(MapBookAdditionsComponent.CODEC)
            .networkSynchronized(MapBookAdditionsComponent.PACKET_CODEC)
            .cacheEncoding());

    public static final Item CHAINMAIL_HORSE_ARMOR = register(
            "chainmail_horse_armor", new Item.Properties().horseArmor(ArmorMaterials.CHAINMAIL));

    public static final Item NAUTILUS_ARMOR = register("nautilus_armor", new Item.Properties().nautilusArmor(ArmorMaterials.ARMADILLO_SCUTE));

    public static final Item COPPER_RAIL = register(BlockRegistry.COPPER_RAIL);
    public static final Item EXPOSED_COPPER_RAIL = register(BlockRegistry.EXPOSED_COPPER_RAIL);
    public static final Item WEATHERED_COPPER_RAIL = register(BlockRegistry.WEATHERED_COPPER_RAIL);
    public static final Item OXIDIZED_COPPER_RAIL = register(BlockRegistry.OXIDIZED_COPPER_RAIL);

    public static final Item WAXED_COPPER_RAIL = register(BlockRegistry.WAXED_COPPER_RAIL);
    public static final Item WAXED_EXPOSED_COPPER_RAIL = register(BlockRegistry.WAXED_EXPOSED_COPPER_RAIL);
    public static final Item WAXED_WEATHERED_COPPER_RAIL = register(BlockRegistry.WAXED_WEATHERED_COPPER_RAIL);
    public static final Item WAXED_OXIDIZED_COPPER_RAIL = register(BlockRegistry.WAXED_OXIDIZED_COPPER_RAIL);

    public static void registerItems() {
        System.out.println("register Items");
    }

    public static Item register(String id, Item.Properties settings) {
        return register(keyOf(id), Item::new, settings);
    }
    public static Item register(String id, Function<Item.Properties, Item> factory, Item.Properties settings) {
        return register(keyOf(id), factory, settings);
    }
    private static ResourceKey<Item> keyOf(String id) {
        return ResourceKey.create(Registries.ITEM, JabsFixedTransport.id(id));
    }
    public static Item register(ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties settings) {
        Item item = factory.apply(settings.setId(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }
    public static Item register(Block block) {
        return register(block, BlockItem::new, new Item.Properties());
    }
    public static Item register(Block block, BiFunction<Block, Item.Properties, Item> factory, Item.Properties settings) {
        return register(
                keyOf(block.builtInRegistryHolder().key()),
                itemSettings -> factory.apply(block, itemSettings),
                settings.useBlockDescriptionPrefix()
        );
    }
    private static ResourceKey<Item> keyOf(ResourceKey<Block> blockKey) {
        return ResourceKey.create(Registries.ITEM, blockKey.identifier());
    }
    private static <T> DataComponentType<T> registerComponent(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, builderOperator.apply(DataComponentType.builder()).build());
    }

    public static final Consumable SWEET_BERRIES_EFFECT = food().onConsume(new ApplyStatusEffectsConsumeEffect(
            new MobEffectInstance(MobEffects.SPEED, 200, 0), 1F)).build();
    public static Consumable.Builder food() {
        return Consumable.builder().consumeSeconds(1.6F).animation(ItemUseAnimation.EAT).sound(SoundEvents.GENERIC_EAT).hasConsumeParticles(true);
    }
}
