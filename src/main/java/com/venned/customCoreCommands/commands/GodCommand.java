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


public class GodCommand implements TabExecutor {

    private final Main plugin;

    public GodCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("customcore.god")) {
            plugin.messages().send(sender, "no-permission");
            return true;
        }

        Player target;
        if (args.length >= 1) {
            if (!sender.hasPermission("customcore.god.others")) {
                plugin.messages().send(sender, "no-permission");
                return true;
            }
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                plugin.messages().send(sender, "player-not-found", "%target%", args[0]);
                return true;
            }
        } else {
            if (!(sender instanceof Player selfPlayer)) {
                plugin.messages().send(sender, "player-only");
                return true;
            }
            target = selfPlayer;
        }

        boolean nowGod = plugin.godManager().toggle(target.getUniqueId());

        if (target.equals(sender)) {
            plugin.messages().send(sender, nowGod ? "god-enabled-self" : "god-disabled-self");
        } else {
            plugin.messages().send(sender,
                    nowGod ? "god-enabled-other" : "god-disabled-other", "%target%", target.getName());
            plugin.messages().send(target,
                    nowGod ? "god-enabled-by" : "god-disabled-by", "%sender%", sender.getName());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender.hasPermission("customcore.god.others")) {
            return CommandUtil.filter(CommandUtil.onlinePlayerNames(sender), args[0]);
        }
        return new ArrayList<>();
    }
}
