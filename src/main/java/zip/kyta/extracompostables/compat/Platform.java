package zip.kyta.extracompostables.compat;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public interface Platform {
    void registerCompostable(@NotNull Material material, float chance);
    void revertCompostables();
}
