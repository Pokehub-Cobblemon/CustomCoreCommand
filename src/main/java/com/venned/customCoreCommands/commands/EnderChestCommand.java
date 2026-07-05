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


public class EnderChestCommand implements TabExecutor {

    private final Main plugin;

    public EnderChestCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("customcore.enderchest")) {
            plugin.messages().send(sender, "no-permission");
            return true;
        }

        // Another player's ender chest.
        if (args.length >= 1) {
            if (!sender.hasPermission("customcore.enderchest.others")) {
                plugin.messages().send(sender, "no-permission");
                return true;
            }
            if (!(sender instanceof Player viewer)) {
                plugin.messages().send(sender, "player-only");
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                plugin.messages().send(sender, "player-not-found", "%target%", args[0]);
                return true;
            }
            viewer.openInventory(target.getEnderChest());
            plugin.messages().send(sender, "enderchest-opened-other", "%target%", target.getName());
            return true;
        }

        // Own ender chest.
        if (!(sender instanceof Player player)) {
            plugin.messages().send(sender, "player-only");
            return true;
        }
        player.openInventory(player.getEnderChest());
        plugin.messages().send(sender, "enderchest-opened-self");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender.hasPermission("customcore.enderchest.others")) {
            return CommandUtil.filter(CommandUtil.onlinePlayerNames(sender), args[0]);
        }
        return new ArrayList<>();
    }
}
