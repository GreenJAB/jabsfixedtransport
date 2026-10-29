package net.greenjab.jabsfixedtransport.registry.registries;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.greenjab.jabsfixedtransport.JabsFixedTransport;
import net.minecraft.resources.Identifier;
import net.ramixin.mixson.Mixson;
import net.ramixin.mixson.enums.ErrorPolicy;
import net.ramixin.mixson.enums.Lifetime;
import net.ramixin.mixson.util.Index;

public class MixsonRegistry {

    public static void registerMixsons() {
        System.out.println("register Mixsons");
    }

    static {
        registerAxeBlockTransformer();

    }

    private static void registerAxeBlockTransformer() {
        Mixson.registerEvent(
                1,
                Lifetime.PERSISTENT,
                ErrorPolicy.LOG,
                JabsFixedTransport.id("add_axe_block_transformers").toString(),
                index -> index.idEquals(new Index(Identifier.fromNamespaceAndPath("minecraft", "block_transformer/axe"))),
                context -> {
                    JsonArray root = context.getFile().getAsJsonArray();
                    if (root == null) return;
                    if (!root.toString().contains("rail")) {
                        JsonObject newCase = new JsonObject();
                        JsonObject provider = new JsonObject();
                        provider.addProperty("type", "minecraft:rule_based");
                        JsonArray rules = new JsonArray();
                        rules.add(makeRule("jabsfixedtransport:waxed_copper_rail", "jabsfixedtransport:copper_rail"));
                        rules.add(makeRule("jabsfixedtransport:waxed_exposed_copper_rail", "jabsfixedtransport:exposed_copper_rail"));
                        rules.add(makeRule("jabsfixedtransport:waxed_weathered_copper_rail", "jabsfixedtransport:weathered_copper_rail"));
                        rules.add(makeRule("jabsfixedtransport:waxed_oxidized_copper_rail", "jabsfixedtransport:oxidized_copper_rail"));
                        provider.add("rules", rules);
                        newCase.add("block_state_provider", provider);
                        newCase.addProperty("item_damage_per_use", 1);
                        newCase.addProperty("sound", "minecraft:item.axe.strip");
                        root.add(newCase);
                    }
                }
        );
    }

    private static JsonObject makeRule(String from, String to){
        JsonObject rule = new JsonObject();

        JsonObject if_true = new JsonObject();
        if_true.addProperty("type", "minecraft:matching_blocks");
        if_true.addProperty("blocks", from);
        rule.add("if_true", if_true);

        JsonObject then = new JsonObject();
        then.addProperty("type", "minecraft:copy_properties");
        JsonObject source = new JsonObject();
        source.addProperty("id", to);
        JsonObject properties = new JsonObject();
        properties.addProperty("axis", "y");
        source.add("properties", properties);
        then.add("source", source);
        rule.add("then", then);

        return rule;
    }
}
