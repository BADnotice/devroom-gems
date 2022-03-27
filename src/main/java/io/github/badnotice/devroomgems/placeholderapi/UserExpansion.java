package io.github.badnotice.devroomgems.placeholderapi;

import io.github.badnotice.devroomgems.data.User;
import io.github.badnotice.devroomgems.registry.UserRegistry;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class UserExpansion extends PlaceholderExpansion {

    private final UserRegistry registry;

    @Override
    public @NotNull String getIdentifier() {
        return "gem";
    }

    @Override
    public @NotNull String getAuthor() {
        return "BADnotice";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        final List<User> top = registry.getTop();

        switch (params.toLowerCase()) {
            case "top1":
                return getUserName(top, 0);

            case "top2":
                return getUserName(top, 1);

            case "top3":
                return getUserName(top, 2);
        }

        return super.onPlaceholderRequest(player, params);
    }

    private String getUserName(List<User> top, int position) {
        try {
            final User user = top.get(position);
            return user.getName();
        } catch (Exception e) {
            return "§c?";
        }
    }

}
