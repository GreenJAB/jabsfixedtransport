# jabsfixedtransport — extraction manifest

Verbatim extraction of the Transportation category from `GreenJAB/fixed-minecraft` into this mod. Source files were copied unmodified, keeping their original `net.greenjab.fixedminecraft` package paths. Nothing was repackaged, rewired, or refactored. The Java port (repackage to `net.greenjab.jabsfixedtransport`, trim shared registries, wire mixin configs and entrypoints) is the next step and is not done here.

Scope source: the wiki "Transportation" category (Horses, Minecarts, Elytra, Mapbook, Other). Total extracted: 242 files (69 Java, 173 resources).

## Wiki coverage

| Status | Feature group | Implemented by |
|---|---|---|
| covered | Horses: leaves slow / boat riding / trot-sprint, ender pearl teleports horse, enchantable  | mixin/horse/* (13 files: AbstractBoat, AbstractChestedHorse, AbstractHorse, ArmorMaterial, Entity, ItemProperties, LeavesBlock, LivingEntity, ServerGamePacketListenerImpl, SkeletonHorse, ThrownEnderPearl, VillageSiege, ZombieHorse) + client DonkeyRendererMixin + AbstractMountInventoryScreenMixin; re... |
| covered | Minecarts: experimental changes forced, copper rails w/ oxidation max speed + self-propel, | mixin/minecart/* (AbstractMinecart, EntityType, GameRules, HoneycombItem, MinecartDispenseItemBehavior, MinecartFurnace, NewMinecartBehavior, Player, ServerPlayer, WeatheringCopper) + registry/block/{CopperRailBlock,OxidizableRailBlock} + registry/other/FixedFurnaceMinecartEntity + network/{TrainNet... |
| covered | Elytra: 0.75s air / riptide activation, hold-space deploy, damage/water/rain/lava disable, | mixin/transport/{FireworkRocketItem,Level,LivingEntity,TridentItem} + client particle/FireworkParticlesMixin; resources: dragon_firework_rocket item/model/texture/recipe + end/dragon_fireworks adv + recipes/transportation/dragon_rocket + end/elytra adv override. Riptide handled by TridentItemMixin (... |
| covered | Mapbook: craft/cartography combine, GUI stitch add/remove/clone, banners+treasure icons, n | mixin/map_book/* (Cartography, MapDecoration, MapId, MapItem, MapItemSavedData [full], ServerWaypoint) + client map/* (7) + registry/item/map_book/* (8) + registry/registries/MapDecorationRegistry + network mapbook payloads + screens/MapBookScreen + client map_book/{MapTile,MapBookFilledProperty}; r... |
| partial | Other: packed+blue ice melt in nether (gamerule) + no melt on crying obsidian, ghast harne | Ice melt: mixin/other/IceBlockMixin + registry/block/{NewBlueIceBlock,NewPackedIceBlock} + GameRuleRegistry(shared). Ghast harness: 15 *_harness.json recipes (red owned by mobsandblocks) + place_dried_ghast_in_water/spyglass_at_ghast advs + green_tweaks harness retextures (shared_adapt). Armadillo-s... |

## Extracted files by feature

### Horses (22)
Java:
- `src/client/java/net/greenjab/fixedminecraft/mixin/client/AbstractMountInventoryScreenMixin.java`
- `src/client/java/net/greenjab/fixedminecraft/mixin/client/DonkeyRendererMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/horse/AbstractBoatMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/horse/AbstractChestedHorseMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/horse/AbstractHorseMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/horse/ArmorMaterialMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/horse/EntityMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/horse/ItemPropertiesMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/horse/LeavesBlockMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/horse/LivingEntityMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/horse/ServerGamePacketListenerImplMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/horse/SkeletonHorseMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/horse/ThrownEnderPearlMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/horse/VillageSiegeMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/horse/ZombieHorseMixin.java`
Resources (7):
- `src/main/resources/assets/fixedminecraft/items/` (1)
- `src/main/resources/assets/fixedminecraft/models/item/` (1)
- `src/main/resources/assets/fixedminecraft/textures/item/` (1)
- `src/main/resources/assets/minecraft/waypoint_style/` (1)
- `src/main/resources/data/fixedminecraft/recipe/` (1)
- `src/main/resources/data/minecraft/advancement/recipes/tools/` (1)
- `src/main/resources/data/minecraft/tags/entity_type/` (1)

### Minecarts (90)
Java:
- `src/main/java/net/greenjab/fixedminecraft/mixin/minecart/AbstractMinecartMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/minecart/EntityTypeMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/minecart/GameRulesMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/minecart/HoneycombItemMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/minecart/MinecartDispenseItemBehaviorMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/minecart/MinecartFurnaceMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/minecart/NewMinecartBehaviorMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/minecart/PlayerMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/minecart/ServerPlayerMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/minecart/WeatheringCopperMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/network/TrainNetwork.java`
- `src/main/java/net/greenjab/fixedminecraft/network/TrainPayload.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/other/FixedFurnaceMinecartEntity.java`
Resources (77):
- `src/main/resources/assets/fixedminecraft/blockstates/` (8)
- `src/main/resources/assets/fixedminecraft/items/` (8)
- `src/main/resources/assets/fixedminecraft/models/block/copper_rail/` (24)
- `src/main/resources/assets/fixedminecraft/models/item/` (8)
- `src/main/resources/assets/fixedminecraft/textures/block/` (4)
- `src/main/resources/assets/fixedminecraft/textures/item/` (4)
- `src/main/resources/data/fixedminecraft/loot_table/blocks/` (8)
- `src/main/resources/data/fixedminecraft/recipe/` (5)
- `src/main/resources/data/minecraft/advancement/adventure/` (1)
- `src/main/resources/data/minecraft/advancement/recipes/transportation/` (5)
- `src/main/resources/data/minecraft/tags/block/` (1)
- `src/main/resources/data/minecraft/tags/item/` (1)

### Elytra/Fireworks (11)
Java:
- `src/client/java/net/greenjab/fixedminecraft/mixin/client/particle/FireworkParticlesMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/transport/FireworkRocketItemMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/transport/LevelMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/transport/LivingEntityMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/transport/TridentItemMixin.java`
Resources (6):
- `src/main/resources/assets/fixedminecraft/items/` (1)
- `src/main/resources/assets/fixedminecraft/models/item/` (1)
- `src/main/resources/assets/fixedminecraft/textures/item/` (1)
- `src/main/resources/data/fixedminecraft/recipe/` (1)
- `src/main/resources/data/minecraft/advancement/end/` (2)

### Mapbook (78)
Java:
- `src/client/java/net/greenjab/fixedminecraft/map_book/MapBookFilledProperty.java`
- `src/client/java/net/greenjab/fixedminecraft/map_book/MapTile.java`
- `src/client/java/net/greenjab/fixedminecraft/mixin/client/map/CartographyTableScreenMixin.java`
- `src/client/java/net/greenjab/fixedminecraft/mixin/client/map/ClientPacketListenerMixin.java`
- `src/client/java/net/greenjab/fixedminecraft/mixin/client/map/GuiMixin.java`
- `src/client/java/net/greenjab/fixedminecraft/mixin/client/map/ItemFrameRendererMixin.java`
- `src/client/java/net/greenjab/fixedminecraft/mixin/client/map/ItemInHandRendererMixin.java`
- `src/client/java/net/greenjab/fixedminecraft/mixin/client/map/LocatorBarRendererMixin.java`
- `src/client/java/net/greenjab/fixedminecraft/mixin/client/map/MapRendererMixin.java`
- `src/client/java/net/greenjab/fixedminecraft/screens/MapBookScreen.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/map_book/CartographyTableMenuMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/map_book/MapDecorationMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/map_book/MapIdMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/map_book/MapItemMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/map_book/MapItemSavedDataMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/mixin/map_book/ServerWaypointManagerMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/network/MapBookOpenPayload.java`
- `src/main/java/net/greenjab/fixedminecraft/network/MapBookPlayer.java`
- `src/main/java/net/greenjab/fixedminecraft/network/MapBookPlayerNetwork.java`
- `src/main/java/net/greenjab/fixedminecraft/network/MapBookSyncPayload.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/item/map_book/MapBookAdditionsComponent.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/item/map_book/MapBookIdCountsState.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/item/map_book/MapBookItem.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/item/map_book/MapBookState.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/item/map_book/MapBookStateManager.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/item/map_book/MapStateAccessor.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/item/map_book/MapStateData.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/registries/MapDecorationRegistry.java`
Resources (50):
- `src/main/resources/assets/fixedminecraft/items/` (1)
- `src/main/resources/assets/fixedminecraft/models/item/` (1)
- `src/main/resources/assets/fixedminecraft/textures/item/` (1)
- `src/main/resources/assets/minecraft/textures/gui/sprites/hud/locator_bar_dot/map_decorations/` (38)
- `src/main/resources/assets/minecraft/textures/map/decorations/` (3)
- `src/main/resources/data/fixedminecraft/recipe/` (1)
- `src/main/resources/data/fixedminecraft/tags/worldgen/structure/` (3)
- `src/main/resources/data/minecraft/advancement/recipes/transportation/` (1)
- `src/main/resources/resourcepacks/green_tweaks/assets/fixedminecraft/models/item/` (1)

### Other (transport) (29)
Java:
- `src/main/java/net/greenjab/fixedminecraft/mixin/other/IceBlockMixin.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/block/NewBlueIceBlock.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/block/NewPackedIceBlock.java`
Resources (26):
- `src/main/resources/assets/fixedminecraft/items/` (1)
- `src/main/resources/assets/fixedminecraft/models/item/` (2)
- `src/main/resources/assets/fixedminecraft/textures/item/` (2)
- `src/main/resources/assets/minecraft/textures/entity/equipment/nautilus_body/` (2)
- `src/main/resources/data/fixedminecraft/recipe/` (1)
- `src/main/resources/data/minecraft/advancement/adventure/` (1)
- `src/main/resources/data/minecraft/advancement/recipes/tools/` (1)
- `src/main/resources/data/minecraft/recipe/` (15)
- `src/main/resources/data/minecraft/tags/item/` (1)

### Other (12)
Java:
- `src/main/java/net/greenjab/fixedminecraft/network/IntArray.java`
- `src/main/java/net/greenjab/fixedminecraft/network/MapPositionPayload.java`
- `src/main/java/net/greenjab/fixedminecraft/network/MapPositionRequestPayload.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/block/CopperRailBlock.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/block/OxidizableRailBlock.java`
Resources (7):
- `src/main/resources/assets/minecraft/textures/entity/equipment/horse_body/` (1)
- `src/main/resources/data/fixedminecraft/tags/worldgen/structure/` (1)
- `src/main/resources/data/minecraft/advancement/adventure/` (1)
- `src/main/resources/data/minecraft/advancement/husbandry/` (1)
- `src/main/resources/data/minecraft/advancement/recipes/transportation/` (1)
- `src/main/resources/data/minecraft/tags/item/` (1)
- `src/main/resources/data/minecraft/tags/item/enchantable/` (1)

## Shared infrastructure to rebuild (NOT copied)

Transport code references these mod-wide classes and config files. They belong to the whole mod, not the transport feature, so they were not copied. The port must recreate trimmed transport-only versions.

- `src/main/java/net/greenjab/fixedminecraft/FixedMinecraft.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/registries/ItemRegistry.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/registries/GameRuleRegistry.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/registries/BlockRegistry.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/registries/MapDecorationRegistry.java (NOTE: classified include since it is mapbook-specific and not in any split; listed here only as a reminder it is referenced by the shared registry-wiring layer)`
- `src/client/java/net/greenjab/fixedminecraft/FixedMinecraftClient.java`
- `src/main/java/net/greenjab/fixedminecraft/FixedMinecraftEnchantmentHelper.java`
- `src/client/java/net/greenjab/fixedminecraft/models/CustomModelLayers.java`
- `src/client/java/net/greenjab/fixedminecraft/util/CustomContainerTextureHolder.java`
- `src/main/java/net/greenjab/fixedminecraft/registry/other/DispencerMinecartEntity.java`
- `src/main/resources/fixedminecraft.mixins.json`
- `src/client/resources/fixedminecraft.client.mixins.json`
- `src/main/resources/data/fixedminecraft/tags/entity_type/vehicles.json`
- `src/main/resources/assets/fixedminecraft/lang/en_us.json`
- `src/main/resources/assets/fixedminecraft/lang/en_gb.json`
- `src/main/resources/assets/fixedminecraft/lang/fr_fr.json`
- `src/main/resources/assets/fixedminecraft/lang/de_de.json`
- `src/main/resources/assets/fixedminecraft/lang/cs_cz.json`
- `src/main/resources/assets/fixedminecraft/lang/enp.json`
- `src/main/resources/assets/fixedminecraft/lang/he_il.json`
- `src/main/resources/assets/fixedminecraft/lang/it_it.json`
- `src/main/resources/assets/fixedminecraft/lang/ja_jp.json`
- `src/main/resources/assets/fixedminecraft/lang/nn_no.json`
- `src/main/resources/assets/fixedminecraft/lang/no_no.json`
- `src/main/resources/assets/fixedminecraft/lang/pl_pl.json`
- `src/main/resources/assets/fixedminecraft/lang/pt_br.json`
- `src/main/resources/assets/fixedminecraft/lang/ru_ru.json`
- `src/main/resources/assets/fixedminecraft/lang/uk_ua.json`
- `src/main/resources/assets/fixedminecraft/lang/zh_cn.json`
- `src/main/resources/assets/fixedminecraft/lang/zh_tw.json`
- `src/main/resources/data/minecraft/tags/worldgen/structure/on_desert_village_maps.json`
- `src/main/resources/data/minecraft/tags/worldgen/structure/on_plains_village_maps.json`
- `src/main/resources/data/minecraft/tags/worldgen/structure/on_savanna_village_maps.json`
- `src/main/resources/data/minecraft/tags/worldgen/structure/on_snowy_village_maps.json`
- `src/main/resources/data/minecraft/tags/worldgen/structure/on_taiga_village_maps.json`
- `src/main/resources/data/minecraft/tags/worldgen/structure/on_jungle_explorer_maps.json`
- `src/main/resources/data/minecraft/tags/worldgen/structure/on_swamp_explorer_maps.json`
- `src/main/resources/resourcepacks/green_tweaks/assets/fixedminecraft/models/item/dragon_firework_rocket.json`
- `src/main/resources/resourcepacks/green_tweaks/assets/minecraft/textures/entity/equipment/happy_ghast_body/*_harness.png (16 dye-colour harness retextures)`

## Deliberate exclusions

Files that look transport-adjacent but were left out, with the reason. Each was confirmed already owned by another split or out of the transport scope.

- `src/main/java/net/greenjab/fixedminecraft/mixin/minecart/AxeItemMixin.java`
  - Byte-identical to jabsfixedmobsandblocks/.../mixin/other/AxeItemMixin.java (copper-scrape loot). Already owned by mobsandblocks; confirmed present.
- `src/main/java/net/greenjab/fixedminecraft/mixin/map_book/MapBannerMixin.java`
  - Byte-identical to jabsfixedmobsandblocks/.../mixin/other/MapBannerMixin.java (fake banner markers). Already in mobsandblocks; confirmed present.
- `src/main/java/net/greenjab/fixedminecraft/mixin/transport/LlamaFollowCaravanGoalMixin.java`
  - Byte-identical to jabsfixedmobsandblocks/.../mixin/mobs/LlamaFollowCaravanGoalMixin.java. Wiki lists llamas under Mobs; already in mobsandblocks; confirmed present.
- `src/main/java/net/greenjab/fixedminecraft/registry/other/DispencerMinecartEntity.java`
  - Redstone dispenser minecart, already in jabsfixedmobsandblocks/.../registry/other/DispencerMinecartEntity.java. FixedFurnaceMinecartEntity references it via same-package instanceof; that cross-mod ref must be adapted, but the class is not copied here.
- `src/main/resources/assets/fixedminecraft/items/dispenser_minecart.json`
  - Dispenser minecart (redstone) already extracted to jabsfixedmobsandblocks/assets/.../items/dispenser_minecart.json. Confirmed present.
- `src/main/resources/assets/fixedminecraft/models/item/dispenser_minecart.json`
  - Dispenser minecart model already in jabsfixedmobsandblocks/assets/.../models/item/dispenser_minecart.json. Confirmed present.
- `src/main/resources/assets/fixedminecraft/textures/item/dispenser_minecart.png`
  - Dispenser minecart texture already in jabsfixedmobsandblocks/assets/.../textures/item/dispenser_minecart.png. Confirmed present.
- `src/main/resources/data/fixedminecraft/recipe/dispenser_minecart.json`
  - Dispenser minecart recipe; the item is owned by mobsandblocks (redstone). Belongs with that mod, not transport.
- `src/main/resources/data/minecraft/advancement/recipes/transportation/dispenser_minecart.json`
  - Recipe-unlock advancement for dispenser_minecart, which lives in mobsandblocks. Goes with that mod.
- `src/main/resources/data/minecraft/recipe/rail.json`
  - Vanilla rail-yield buff recipe (not copper rail) already extracted to jabsfixedmobsandblocks/.../recipe/rail.json. Confirmed present; mobsandblocks owns it.
- `src/main/resources/data/fixedminecraft/recipe/leather_horse_armor.json`
  - Already in jabsfixedmobsandblocks/.../recipe/leather_horse_armor.json. Confirmed present.
- `src/main/resources/data/minecraft/villager_trade/leatherworker/3/emerald_dyed_leather_horse_armor.json`
  - Leatherworker dyed-leather-horse-armor trade already extracted into mobsandblocks. Confirmed by classifier; same path.
- `src/main/resources/data/minecraft/villager_trade/leatherworker/5/emerald_enchanted_dyed_leather_horse_armor.json`
  - Enchanted dyed-leather-horse-armor trade already in mobsandblocks.
- `src/main/resources/assets/fixedminecraft/items/azalea_boat.json (+ azalea_chest_boat item/model/texture/entity-texture/recipe/recipe-adv/boats+chest_boats item tags)`
  - Entire azalea boat + chest boat vehicle set already extracted into jabsfixedmobsandblocks (confirmed: items, models, textures, recipes, advancement/recipe/blocks/azalea_*.json present). Conceptually transport but already owned by mobsandblocks per the hard exclude rule; ownership conflict flagged in gaps.
- `src/main/resources/data/minecraft/recipe/red_harness.json`
  - One of the 16 ghast-harness recipes; already extracted into jabsfixedmobsandblocks/.../recipe/red_harness.json. Excluded to avoid duplication. The other 15 harness recipes are unclaimed and INCLUDED in transport. Split inconsistency flagged in gaps.
- `src/main/resources/data/minecraft/loot_table/entities/nautilus.json`
  - Nautilus MOB loot (drops nautilus_shell), not the armadillo-scute armour. Mobs scope, not transport.
- `src/main/resources/data/minecraft/loot_table/entities/zombie_nautilus.json`
  - Zombie nautilus MOB loot. Mobs scope, not transport.
- `src/main/java/net/greenjab/fixedminecraft/mixin/other/DyeColorMixin.java`
  - 'brighterColours' dye-brightening visual tweak, NOT the dyeable-compass mapbook feature. Belongs to worldandui/visuals.
- `src/main/java/net/greenjab/fixedminecraft/mixin/other/DyedItemColorMixin.java`
  - 'brighterColours2' dye-mixing visual tweak, not dyeable compass. Belongs to worldandui/visuals.
- `src/main/resources/data/fixedminecraft/worldgen/configured_feature/ore_packed_ice.json`
  - Packed-ice ORE worldgen in frozen overworld biomes, NOT the nether ice-melt transport gamerule (that is IceBlockMixin + GameRuleRegistry). Belongs to worldandui.
- `src/main/resources/data/fixedminecraft/worldgen/placed_feature/ore_packed_ice.json`
  - Packed-ice ore placement worldgen. Belongs to worldandui.
- `src/main/resources/data/fixedminecraft/tags/worldgen/biome/is_frozen.json`
  - Biome selector for packed-ice ore worldgen, not nether ice melt. Belongs to worldandui.
- `src/main/resources/data/fixedminecraft/tags/enchantment/loot/trail_ruins.json`
  - Enchantment loot tag (silk_touch/sweeping_edge), already extracted into jabsfixedenchanting. Enchanting, not mapbook.
- `src/main/resources/data/minecraft/tags/worldgen/structure/eye_of_ender_located.json`
  - End-city locating structure tag, no transport/mapbook Java reference. Not transport.
- `src/main/resources/data/minecraft/advancement/husbandry/ride_a_boat_with_a_goat.json`
  - Vanilla husbandry advancement, no fixedminecraft content; ships unchanged. Not a mod transport feature.
- `src/main/resources/data/minecraft/advancement/nether/explore_nether.json`
  - Vanilla nether-biome-exploration advancement, only re-parented onto place_dried_ghast_in_water. Content is nether exploration, not transport; owning split keeps it (parent-id coupling noted).
- `src/main/resources/resourcepacks/removed_features_21_11/data/minecraft/advancement/recipes/combat/netherite_nautilus_armor_smithing.json`
  - Disabled recipe-advancement for removed vanilla tiered netherite nautilus armour, under recipes/combat. Pack-level, combat-adjacent, not transport.
- `src/main/resources/data/minecraft/loot_table/chests/shipwreck_treasure.json`
  - References removed vanilla tiered nautilus armour, not fixedminecraft:nautilus_armor; already copied into jabsfixedenchanting. Out of transport scope.
- `src/main/resources/data/fixedminecraft/loot_table/gameplay/fixed_fishing/mid.json`
  - Fishing loot dropping nautilus_shell; unrelated to nautilus armour. Not transport.
- `src/main/resources/resourcepacks/recolourful_containers/assets/minecraft/textures/opti_vi_tul/gui/{dispenser_minecart,minecart,hopper_minecart}.png`
  - Bundled third-party cosmetic resourcepack GUI recolours; whole pack travels as a unit. dispenser one also ties to redstone (mobsandblocks). Out of core transport slice.

## Points of focus and open decisions

These need a human call during the port. They are not blockers for the extraction itself.

1. BLAST KNOCKBACK (wiki Other: 'Blast protection no longer reduces blast knockback (tnt as transport)'): no transport file in the include set implements this. The logic is `mixin/enchanting/EnchantmentHelperMixin.java` line 71 (`if (source.is(DamageTypeTags.IS_EXPLOSION)) return;`, which skips the knockback reduction for explosion damage), an ENCHANTING-package mixin already owned by jabsfixedenchanting. (The earlier ItemEnchantmentsMixin citation was a false-positive grep on an unrelated biome-decoration line.) Treated as enchanting, not re-included in transport. Human should confirm this knockback behaviour is acceptable to leave with enchanting rather than transport.

2. SWEET BERRIES SWIFTNESS (wiki Other: 'Sweet berries give short swiftness'): no dedicated transport file found in the include set. The sprint-jump-under-swiftness and doubled-jump-boost effects ARE in transport/LivingEntityMixin (included), but the sweet-berry->swiftness application itself was not located as a transport-package file. Needs a human grep for the SweetBerry/eat-effect mixin to decide ownership (likely food/mobsandblocks).

3. GHAST HARNESS SPLIT INCONSISTENCY: the 16 colored *_harness.json recipes are one logical set, but only red_harness.json was extracted into jabsfixedmobsandblocks. The remaining 15 are included in transport per the seed (ghast harness = transport Other). This leaves the harness recipe set split across two mods. Human should decide whether to move red_harness into transport or move all 16 to one mod.

4. AZALEA BOAT OWNERSHIP CONFLICT: azalea_boat + azalea_chest_boat are conceptually transport vehicles but the entire set is already in jabsfixedmobsandblocks (bundled with the azalea wood set). Excluded from transport per the hard already-split rule. If the boats should live in transport, this is an inter-split ownership decision for the orchestrator.

5. MapItemSavedDataMixin OVERLAP: the full 272-line transport version is included, but jabsfixedmobsandblocks already extracted a ~30-line subset (noRemoveCustomIconBanner @Redirect) into mixin/other/MapItemSavedDataMixin.java. Both target vanilla MapItemSavedData. The integrator must reconcile the duplicated @Redirect so the same method is not applied twice (keep it only in transport, or have transport omit that one redirect).

6. DispencerMinecartEntity CROSS-MOD REFERENCE: FixedFurnaceMinecartEntity (included) and the minecart mixins reference DispencerMinecartEntity via same-package instanceof. That class is owned by mobsandblocks (excluded). The transport build needs an adaptation (soft instanceof by class-name, a shared interface, or a guarded check) since it cannot import the mobsandblocks class.

7. SHARED REGISTRY WIRING: the included transport code registers via FixedMinecraft/ItemRegistry/BlockRegistry/GameRuleRegistry/FixedMinecraftClient and the two mixin json configs (all shared_adapt). These must be rebuilt as trimmed transport-only entrypoints/registries wiring MapBookItem, copper rails, FixedFurnaceMinecartEntity, ICE_MELT_IN_NETHER, MAP_BOOK_ADDITIONS, MapDecorationRegistry, network payload registration, and the mapBookMarker command. Not a missing file, but required new scaffolding.

8. TREASURE-MAP LOOT/TRADE SOURCES are out of the classified slices and shared: data/minecraft/loot_table/chests/{buried_treasure,pillager_outpost,jungle_temple,simple_dungeon}.json, loot_table/archaeology/{desert_well,trail_ruins_*}.json, villager_trade/cartographer/3/emerald_and_compass_portal_explorer_map.json, advancement/adventure/salvage_sherd.json. These GENERATE the explorer/treasure maps (transport mapbook) but mix with general/enchanting loot. Human/owning slice must split out the transport map-destination entries (shared_adapt).

## Treasure-map loot overlap (special attention, per green_jab)

The explorer/treasure maps (transport mapbook feature) are injected by `ExplorationCompassLootFunction` into vanilla chest loot-table overrides. Those same loot tables also carry the structure-specific enchanted books, which belong to the enchanting split. The files are shared and cannot be handed wholesale to transport.

Files that inject explorer/treasure maps:
- `data/minecraft/loot_table/chests/buried_treasure.json` (-> ocean monument)
- `data/minecraft/loot_table/chests/pillager_outpost.json` (-> mansion/village)
- `data/minecraft/loot_table/chests/jungle_temple.json` (-> trail ruins)
- `data/minecraft/loot_table/chests/simple_dungeon.json` (-> trial chambers)
- `data/minecraft/loot_table/archaeology/trail_ruins_rare.json`
- `data/minecraft/loot_table/archaeology/desert_well.json`
- `data/minecraft/villager_trade/cartographer/3/emerald_and_compass_portal_explorer_map.json` (-> ruined portal)

Intersection (carry BOTH an explorer map AND enchanted books): `buried_treasure.json`, `pillager_outpost.json`, `jungle_temple.json`, `simple_dungeon.json`. A wider set of 19 chest tables carry enchanted books for the enchanting split.

Handling: these loot tables were left in `shared_adapt`, not copied into transport. During the port, split out only the map-destination entries for transport and leave the enchanted-book entries with enchanting. Owning split must merge, since both mods write the same files.

## Dispenser minecart (confirmed with green_jab)

green_jab placed the dispenser cart in jabsfixedmobsandblocks as a redstone feature and fixed a wrong-direction firing bug there. This manifest matches that: `DispencerMinecartEntity.java` and every `dispenser_minecart` asset/recipe/advancement are excluded from transport. `FixedFurnaceMinecartEntity` (transport) still references `DispencerMinecartEntity` via a same-package `instanceof`. The port must adapt that cross-mod reference (shared interface, class-name check, or guarded import) and must not reintroduce an older copy of the cart that would undo green_jab's bug fix.

## Fabric port checklist (next step, not done here)

Grounded in the QA review against the jabsfixedcombat known-good split (same author, MC 26.1.2, Java 25) and docs.fabricmc.net. The extracted Java still sits under `net/greenjab/fixedminecraft/...` and must be repackaged to `net.greenjab.jabsfixedtransport`.

### fabric.mod.json (src/main/resources/fabric.mod.json)
- Add the `mixson` dependency: `"mixson": ">=1.3.1"`. The source and the combat split both declare it; the scaffold omits it.
- Add an inter-mod dependency on mobsandblocks for the `DispencerMinecartEntity` cross-reference: `"depends": { "jabsfixedmobsandblocks": ">=<version>" }` (or break the coupling, see risks).
- Entrypoints (main/client) and the mixins/accessWidener fields already point at the right names; keep them.

### Mixin configs (populate the two scaffold stubs)
jabsfixedtransport.mixins.json (`"package": "net.greenjab.jabsfixedtransport.mixin"`), 32 server mixins:
- horse (13): AbstractBoat, AbstractChestedHorse, AbstractHorse, ArmorMaterial, Entity, ItemProperties, LeavesBlock, LivingEntity, ServerGamePacketListenerImpl, SkeletonHorse, ThrownEnderPearl, VillageSiege, ZombieHorse
- minecart (10): AbstractMinecart, EntityType, GameRules, HoneycombItem, MinecartDispenseItemBehavior, MinecartFurnace, NewMinecartBehavior, Player, ServerPlayer, WeatheringCopper (omit AxeItem, owned by mobsandblocks)
- transport (4): FireworkRocketItem, Level, LivingEntity, TridentItem (omit LlamaFollowCaravanGoal, owned by mobsandblocks)
- map_book (6): CartographyTableMenu, MapDecoration, MapId, MapItem, MapItemSavedData, ServerWaypointManager (omit MapBanner, owned by mobsandblocks)
- other (1): IceBlock

jabsfixedtransport.client.mixins.json (`"package": "net.greenjab.jabsfixedtransport.client.mixin"`), bump compatibilityLevel to JAVA_25 (combat did this):
- client (9): DonkeyRenderer, map.CartographyTableScreen, map.ClientPacketListener, map.Gui, map.ItemFrameRenderer, map.ItemInHandRenderer, map.LocatorBarRenderer, map.MapRenderer, particle.FireworkParticles
- mixins (1, client-loaded): AbstractMountInventoryScreen

### accessWidener (src/main/resources/jabsfixedtransport.accesswidener)
Carry over exactly 4 of the source's 37 lines (transport has no @Accessor/@Invoker mixins, so each widened member is accessed directly):
```
accessible field net/minecraft/world/inventory/CartographyTableMenu lastSoundTime J
accessible method net/minecraft/client/renderer/entity/LivingEntityRenderer addLayer (Lnet/minecraft/client/renderer/entity/layers/RenderLayer;)Z
accessible field net/minecraft/client/particle/Particle y D
accessible field net/minecraft/world/inventory/AbstractMountInventoryMenu mount Lnet/minecraft/world/entity/LivingEntity;
```
Match the header form (`named` vs `official`) used by the sibling splits.

### Registry / component / networking wiring (rebuild trimmed)
- Data component: re-register `MAP_BOOK_ADDITIONS` into `BuiltInRegistries.DATA_COMPONENT_TYPE` with `.persistent(MapBookAdditionsComponent.CODEC).networkSynchronized(MapBookAdditionsComponent.PACKET_CODEC)` (source ItemRegistry lines 57-58). The extracted component already provides both codecs.
- Networking: the 9 payload classes self-define CODEC + Type ID, but registration is central. Mirror combat's SyncHandler: `PayloadTypeRegistry.{serverbound,clientbound}Play().register(...)` plus `ServerPlayNetworking`/`ClientPlayNetworking.registerGlobalReceiver(...)`. C2S: TrainPayload, MapPositionRequestPayload. S2C: MapBookSync, MapPosition, MapBookOpen. Wire from `JabsFixedTransport.onInitialize()` (server) and `JabsFixedTransportClient` (client receivers).
- Block/item/gamerule registry: trimmed BlockRegistry/ItemRegistry/GameRuleRegistry must register copper rails (8 variants -> CopperRailBlock/OxidizableRailBlock), NewBlueIceBlock/NewPackedIceBlock, MapBookItem, chainmail + nautilus armor items, FixedFurnaceMinecartEntity entity type, the ICE_MELT_IN_NETHER gamerule, MapDecorationRegistry, and the MAP_BOOK_ADDITIONS component. Register the `mapBookMarker` command in the main entrypoint. Register the green_tweaks builtin resourcepack for the dragon-firework/harness retextures, as combat does for its packs.

## Port to a buildable mod (done)

The verbatim extraction was ported to a compiling, building Fabric mod. `./gradlew build` is green locally (MC 26.1.2, Java 25, Loom 1.16), producing `jabsfixedtransport-26.1.2.v1.jar`.

What the port changed:
- Java package renamed `net.greenjab.fixedminecraft` -> `net.greenjab.jabsfixedtransport` across all sources. Entrypoints `FixedMinecraft`/`FixedMinecraftClient` renamed to `JabsFixedTransport`/`JabsFixedTransportClient`.
- Resource namespace kept as `fixedminecraft` (assets/data unchanged). The mod id is `jabsfixedtransport`; a mod may serve resources under any namespace, so registered ids (e.g. `fixedminecraft:copper_rail`) resolve against the unchanged assets. This avoided rewriting ~170 resource cross-references. Renaming the namespace to `jabsfixedtransport` to match the sibling splits is a clean follow-up.
- Trimmed shared registries rebuilt for transport only: `ItemRegistry` (map_book + map_book_additions component, chainmail_horse_armor, nautilus_armor, dragon_firework_rocket, 8 copper rail items), `BlockRegistry` (8 copper rails), `GameRuleRegistry` (ice_melt_in_nether), `SyncHandler`/`ClientSyncHandler` (mapbook + train payloads), `CustomModelLayers` (mule armor). `CustomData` and `FixedMinecraftEnchantmentHelper` copied as-is.
- Entrypoints wired: registries, networking, `mapBookMarker` command, `SERVER` set via `ServerLifecycleEvents`, map_book conditional model property, mule armor model layer.
- Mixin configs populated: 34 main mixins (horse/minecart/transport/map_book + other.IceBlock), 10 client mixins (map.* + DonkeyRenderer + FireworkParticles + AbstractMountInventoryScreen). 5 access-widener lines (CartographyTableMenu.lastSoundTime, LivingEntityRenderer.addLayer, Particle.y, AbstractMountInventoryMenu.mount, DonkeyModel.DONKEY_TRANSFORMER).
- build.gradle: Modrinth repo + `mixson` dependency. fabric.mod.json: `mixson` depend, corrected client entrypoint, contributor credit to Aqu1tain (Akitain).
- Dispenser-cart coupling removed: `AbstractMinecartMixin` and `FixedFurnaceMinecartEntity` no longer reference `DispencerMinecartEntity` (owned by jabsfixedmobsandblocks). The checks were inert without that mod. Restoring dispenser-cart train support requires a dependency on jabsfixedmobsandblocks.

Not yet done (intentional): runtime verification in-game, namespace rename to match siblings, the shared treasure-map loot tables, and the MapItemSavedData double-redirect reconciliation with mobsandblocks.

Credit: extraction and port by Aqu1tain (Akitain). Original mod and code by Green_Jab (GreenJAB/fixed-minecraft).
