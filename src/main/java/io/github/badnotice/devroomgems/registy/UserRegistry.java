package io.github.badnotice.devroomgems.registy;

import io.github.badnotice.devroomgems.data.User;
import io.github.badnotice.devroomgems.database.UserDatabase;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class UserRegistry {

    private static final Comparator<User> USER_COMPARATOR = Comparator.comparingDouble(User::getBalance);

    private final Map<UUID, User> userMap;

    public UserRegistry(UserDatabase database) {
        this.userMap = new HashMap<>();

        for (User user : database.loadAll()) registerUser(user);
    }

    public void registerUser(User user) {
        this.userMap.put(user.getUniqueId(), user);
    }

    public User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    public User getUser(UUID uuid) {
        return userMap.get(uuid);
    }

    public User getUser(String playerName) {
        return this.userMap
                .values()
                .stream()
                .filter($ -> $.getName().equals(playerName))
                .findFirst()
                .orElse(null);
    }

    public List<User> getTop() {
        return this.userMap
                .values()
                .stream()
                .sorted(USER_COMPARATOR.reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public Iterator<User> iterator() {
        return this.userMap
                .values()
                .iterator();
    }

}
