package xyz.nkomarn.extracompostables;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ComposterBlock;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Compost the world!
 * <p>
 * A simple, absolutely bare-bones, and performance-optimal way
 * to make more items compostable in a composter block!
 *
 * @author Mykyta @nkomarn
 */
public class ExtraCompostables extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        readConfig().forEach(this::registerCompostable);
    }

    @Override
    public void onDisable() {
        revertCompostables();
    }

    /**
     * Reads the configuration into a map of compostable materials
     * and their configured success rates.
     *
     * @return A map of configured materials and chances.
     */
    @NotNull
    private Map<Material, Integer> readConfig() {
        var compostables = getConfig().getConfigurationSection("compostables");

        if (compostables == null) {
            return Collections.emptyMap();
        }

        return compostables.getKeys(false).stream()
                .map(String::toUpperCase)
                .filter(key -> EnumUtils.isValidEnum(Material.class, key))
                .collect(Collectors.toMap(Material::valueOf, compostables::getInt));
    }

    /**
     * Registers an item type as compostable during runtime. This
     * directly modifies <code>ComposterBlock#COMPOSTABLES</code> to
     * register items as compostable.
     *
     * @param material The item to register as compostable.
     * @param chance   The chance for a successful compost, from 1-100.
     */
    private void registerCompostable(@NotNull Material material, int chance) {
        var item = Registry.ITEM.get(new ResourceLocation(material.getKey().toString()));
        ComposterBlock.COMPOSTABLES.put(item, chance / 100F);
    }

    private void revertCompostables() {
        ComposterBlock.COMPOSTABLES.clear();
        ComposterBlock.bootStrap();
    }
}
