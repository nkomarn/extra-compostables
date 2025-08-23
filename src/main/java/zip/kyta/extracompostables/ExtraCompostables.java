package zip.kyta.extracompostables;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zip.kyta.extracompostables.compat.Platform;
import zip.kyta.extracompostables.compat.V_3953;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;
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
    private static final TreeMap<Integer, Class<?>> PLATFORM_VERSIONS = new TreeMap<>() {{
        put(3953, V_3953.class); // 1.21
    }};

    @Nullable
    private Platform platform;

    @Override
    public void onEnable() {
        platform = initializePlatform();

        saveDefaultConfig();
        readConfig().forEach(platform::registerCompostable);
    }

    @Override
    public void onDisable() {
        if (platform != null) {
            platform.revertCompostables();
        }
    }

    @NotNull
    @SuppressWarnings("deprecation")
    private Platform initializePlatform() {
        var dataVersion = getServer().getUnsafe().getDataVersion();
        var closestCompatibleVersion = PLATFORM_VERSIONS.floorEntry(dataVersion);

        getLogger().info("Using platform implementation for data version " + closestCompatibleVersion.getKey() + "..");

        try {
            return (Platform) closestCompatibleVersion.getValue().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to load platform-specific classes; this version of the plugin is likely incompatible with your Minecraft release.", e);
        }
    }

    @NotNull
    private Map<Material, Integer> readConfig() {
        var compostables = getConfig().getConfigurationSection("compostables");

        if (compostables == null) {
            return Collections.emptyMap();
        }

        return compostables.getKeys(false).stream()
                .map(NamespacedKey::minecraft)
                .map(Registry.MATERIAL::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Function.identity(), value -> compostables.getInt(value.getKey().getKey())));
    }
}
