package net.greenjab.jabsfixedtransport;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModTags {

    public static final TagKey<Structure> LODESTONE_COMPASS = TagKey.create(Registries.STRUCTURE, JabsFixedTransport.id("lodestone_compass"));
    public static final TagKey<Structure> ON_RUINED_PORTAL_MAPS = TagKey.create(Registries.STRUCTURE, JabsFixedTransport.id("on_ruined_portal_maps"));
    public static final TagKey<Structure> ON_OUTPOST_MAPS = TagKey.create(Registries.STRUCTURE, JabsFixedTransport.id("on_outpost_maps"));

    public static final TagKey<Item> VANILLA_NAUTILUS_ARMOR = TagKey.create(Registries.ITEM, JabsFixedTransport.id("vanilla_nautilus_armor"));

}
