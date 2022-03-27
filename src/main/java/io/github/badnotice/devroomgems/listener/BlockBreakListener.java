package io.github.badnotice.devroomgems.listener;

import io.github.badnotice.devroomgems.GemsPlugin;
import io.github.badnotice.devroomgems.data.Reward;
import io.github.badnotice.devroomgems.data.User;
import io.github.badnotice.devroomgems.registy.RewardRegistry;
import io.github.badnotice.devroomgems.registy.UserRegistry;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final UserRegistry userRegistry;
    private final RewardRegistry rewardRegistry;

    public BlockBreakListener(GemsPlugin plugin) {
        this.rewardRegistry = plugin.getRewardRegistry();
        this.userRegistry = plugin.getUserRegistry();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();

        final Reward reward = rewardRegistry.getReward(block.getType());
        if (reward == null || !reward.inPercent()) return;

        final Player player = event.getPlayer();

        final User user = userRegistry.getUser(player);
        if (user == null) return;

        user.deposit(1);
        player.sendMessage("§aYou received 1 gem.");
    }

}
