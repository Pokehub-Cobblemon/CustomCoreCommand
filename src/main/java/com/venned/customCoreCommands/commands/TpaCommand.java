package com.venned.customCoreCommands.commands;

import com.venned.customCoreCommands.Main;
import com.venned.customCoreCommands.util.CommandUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class TpaCommand implements TabExecutor {

    private final Main plugin;

    public TpaCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.messages().send(sender, "player-only");
            return true;
        }
        if (!player.hasPermission("customcore.tpa")) {
            plugin.messages().send(sender, "no-permission");
            return true;
        }
        if (args.length < 1) {
            plugin.messages().send(sender, "tpa-usage");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            plugin.messages().send(sender, "player-not-found", "%target%", args[0]);
            return true;
        }
        if (target.equals(player)) {
            plugin.messages().send(sender, "tpa-self");
            return true;
        }

        plugin.tpaManager().createRequest(player, target);
        String seconds = String.valueOf(plugin.tpaManager().expireSeconds());

        plugin.messages().send(player, "tpa-sent", "%target%", target.getName(), "%seconds%", seconds);
        plugin.messages().send(target, "tpa-received", "%sender%", player.getName(), "%seconds%", seconds);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return CommandUtil.filter(CommandUtil.onlinePlayerNames(sender), args[0]);
        }
        return new ArrayList<>();
    }
}
