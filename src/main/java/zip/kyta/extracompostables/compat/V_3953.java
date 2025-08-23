package zip.kyta.extracompostables.compat;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import zip.kyta.extracompostables.util.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.Map;

public class V_3953 implements Platform {
    private final Class<?> classBuiltInRegistries = Class.forName("net.minecraft.core.registries.BuiltInRegistries");
    private final Class<?> classDefaultedRegistry = Class.forName("net.minecraft.core.RegistryBlocks");
    private final Class<?> classResourceLocation = Class.forName("net.minecraft.resources.MinecraftKey");
    private final Class<?> classComposter = Class.forName("net.minecraft.world.level.block.BlockComposter");
    private final Method methodRegistryGet = classDefaultedRegistry.getDeclaredMethod("a", classResourceLocation);

    private final Object itemRegistry = ReflectionHelper.getStaticValue(classBuiltInRegistries, "g");
    private final Map<Object, Float> compostablesMap = ReflectionHelper.getStaticValue(classComposter, "f");

    public V_3953() throws Exception {
    }

    @Override
    public void registerCompostable(@NotNull Material material, float chance) {
        try {
            var resourceKey = classResourceLocation
                    .getDeclaredMethod("a", String.class)
                    .invoke(null, material.getKey().toString());

            var item = methodRegistryGet.invoke(itemRegistry, resourceKey);
            compostablesMap.put(item, chance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void revertCompostables() {
        try {
            compostablesMap.clear();
            classComposter.getDeclaredMethod("b").invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
