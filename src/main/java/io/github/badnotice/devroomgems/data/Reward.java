package io.github.badnotice.devroomgems.data;

import lombok.Data;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

@Data
public class Reward {

    private final Material material;
    private final double percent;

    public static Reward read(String key, ConfigurationSection section) {
        final Material material = Material.matchMaterial(key);
        final double percent = section.getDouble(key);

        return new Reward(
                material,
                percent
        );
    }

    public boolean inPercent() {
        return Math.random() * 100 <= percent;
    }

}
