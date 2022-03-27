package io.github.badnotice.devroomgems.command;

import io.github.badnotice.devroomgems.GemsPlugin;
import io.github.badnotice.devroomgems.data.User;
import io.github.badnotice.devroomgems.registry.UserRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GemsCommand implements CommandExecutor {

    private static final String[] DEFAULT_COMMANDS = new String[]{
            "",
            " §aGems Commands",
            "",
            " §a/gems set <player> <gems> - set an amount.",
            " §a/gems deposit <player> <gems> - add an amount.",
            " §a/gems withdraw <player> <gems> - withdraw an amount.",
            ""
    };

    private final UserRegistry registry;

    public GemsCommand(GemsPlugin plugin) {
        this.registry = plugin.getUserRegistry();
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (!sender.hasPermission("gems.admin")) {
            sender.sendMessage("§cYou don't have enough permissions");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(DEFAULT_COMMANDS);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "set":
                processing(sender, args, OperationType.SET);
                break;
            case "withdraw":
                processing(sender, args, OperationType.WITHDRAW);
                break;
            case "add":
                processing(sender, args, OperationType.DEPOSIT);
                break;
        }

        return false;
    }

    public void processing(CommandSender sender, String[] args, OperationType type) {
        if (args.length != 3) {
            sender.sendMessage("§cUse: /gems [set|remove|add] <player> <gems>");
            return;
        }

        final String targetName = args[1];

        final User user = registry.getUser(targetName);
        if (user == null) {
            sender.sendMessage("§cAn error has occurred! User not found.");
            return;
        }

        final double value = parse(args[2]);
        if (value <= 0) {
            sender.sendMessage("§cPlease enter a valid number.");
            return;
        }

        switch (type) {
            case SET:
                user.set(value);
                break;

            case DEPOSIT:
                user.deposit(value);
                break;

            case WITHDRAW:
                user.withdraw(value);
                break;
        }

        sender.sendMessage("§aYou changed player gems " + targetName + " for " + value);
    }

    private boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private double parse(String s) {
        if (!isDouble(s)) return 0;

        final double v = Double.parseDouble(s);
        if (Double.isNaN(v)) return 0;

        return v;
    }

    enum OperationType {
        WITHDRAW,
        DEPOSIT,
        SET
    }

}
