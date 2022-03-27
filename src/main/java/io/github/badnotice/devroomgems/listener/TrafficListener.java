package io.github.badnotice.devroomgems.listener;

import io.github.badnotice.devroomgems.GemsPlugin;
import io.github.badnotice.devroomgems.data.User;
import io.github.badnotice.devroomgems.database.UserDatabase;
import io.github.badnotice.devroomgems.registy.UserRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TrafficListener implements Listener {

    private final UserRegistry registry;
    private final UserDatabase database;

    public TrafficListener(GemsPlugin plugin) {
        this.registry = plugin.getUserRegistry();
        this.database = plugin.getUserDatabase();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        User user = registry.getUser(player);
        if (user != null) return;

        user = new User(
                player.getUniqueId(),
                player.getName(),
                0
        );

        database.insertOrUpdate(user);
        registry.registerUser(user);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        User user = registry.getUser(player);
        if (user == null) return;

        database.insertOrUpdate(user);
    }

}
