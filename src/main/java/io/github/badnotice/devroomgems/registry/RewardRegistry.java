package io.github.badnotice.devroomgems.registry;

import io.github.badnotice.devroomgems.data.Reward;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RewardRegistry {

    private final Set<Reward> rewardSet;

    public RewardRegistry(Configuration configuration) {
        this.rewardSet = new HashSet<>();
        this.loadRewards(configuration);
    }

    public void registerReward(Reward reward) {
        this.rewardSet.add(reward);
    }

    public Reward getReward(Material material) {
        return this.rewardSet
                .stream()
                .filter($ -> $.getMaterial() == material)
                .findFirst()
                .orElse(null);
    }

    private void loadRewards(Configuration configuration) {
        final ConfigurationSection rewards = configuration.getConfigurationSection("rewards");
        if (rewards == null) return;

        final Iterator<String> iterator = rewards.getKeys(false).iterator();
        while (iterator.hasNext()) {
            final String key = iterator.next();

            final Reward read = Reward.read(
                    key,
                    rewards
            );

            registerReward(read);
        }

    }

}
