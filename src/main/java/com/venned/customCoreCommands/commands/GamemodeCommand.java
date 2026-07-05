package com.venned.customCoreCommands.commands;

import com.venned.customCoreCommands.Main;
import com.venned.customCoreCommands.util.CommandUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class GamemodeCommand implements TabExecutor {

    private final Main plugin;

    public GamemodeCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("customcore.gamemode")) {
            plugin.messages().send(sender, "no-permission");
            return true;
        }
        if (args.length == 0) {
            plugin.messages().send(sender, "usage",
                    "%usage%", "/" + label + " <survival|creative|adventure|spectator> [player]");
            return true;
        }

        GameMode mode = parseGameMode(args[0]);
        if (mode == null) {
            plugin.messages().send(sender, "gamemode-invalid", "%input%", args[0]);
            return true;
        }

        Player target;
        if (args.length >= 2) {
            if (!sender.hasPermission("customcore.gamemode.others")) {
                plugin.messages().send(sender, "no-permission");
                return true;
            }
            target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                plugin.messages().send(sender, "player-not-found", "%target%", args[1]);
                return true;
            }
        } else {
            if (!(sender instanceof Player selfPlayer)) {
                plugin.messages().send(sender, "player-only");
                return true;
            }
            target = selfPlayer;
        }

        target.setGameMode(mode);
        String modeName = pretty(mode);

        if (target.equals(sender)) {
            plugin.messages().send(sender, "gamemode-set-self", "%gamemode%", modeName);
        } else {
            plugin.messages().send(sender, "gamemode-set-other",
                    "%target%", target.getName(), "%gamemode%", modeName);
            plugin.messages().send(target, "gamemode-set-by",
                    "%gamemode%", modeName, "%sender%", sender.getName());
        }
        return true;
    }

    private GameMode parseGameMode(String input) {
        switch (input.toLowerCase(Locale.ROOT)) {
            case "0":
            case "s":
            case "survival":
                return GameMode.SURVIVAL;
            case "1":
            case "c":
            case "creative":
                return GameMode.CREATIVE;
            case "2":
            case "a":
            case "adventure":
                return GameMode.ADVENTURE;
            case "3":
            case "sp":
            case "spectator":
                return GameMode.SPECTATOR;
            default:
                return null;
        }
    }

    private String pretty(GameMode mode) {
        String lower = mode.name().toLowerCase(Locale.ROOT);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return CommandUtil.filter(
                    Arrays.asList("survival", "creative", "adventure", "spectator"), args[0]);
        }
        if (args.length == 2 && sender.hasPermission("customcore.gamemode.others")) {
            return CommandUtil.filter(CommandUtil.onlinePlayerNames(sender), args[1]);
        }
        return new ArrayList<>();
    }
}
