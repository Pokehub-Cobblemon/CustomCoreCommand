package com.venned.customCoreCommands.commands;

import com.venned.customCoreCommands.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class CustomCoreCommand implements TabExecutor {

    private final Main plugin;

    public CustomCoreCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("customcore.admin")) {
                plugin.messages().send(sender, "no-permission");
                return true;
            }
            plugin.reloadConfig();
            plugin.messages().reload();
            plugin.messages().send(sender, "reloaded");
            return true;
        }

        String version = plugin.getDescription().getVersion();
        sender.sendMessage(ChatColor.AQUA + "CustomCoreCommands " + ChatColor.GRAY + "v" + version);
        sender.sendMessage(ChatColor.GRAY + "Commands: " + ChatColor.WHITE
                + "/gamemode, /god, /openinv, /enderchest, /fix, /tpa, /trash");
        sender.sendMessage(ChatColor.GRAY + "Reload config: " + ChatColor.WHITE + "/" + label + " reload");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender.hasPermission("customcore.admin")
                && "reload".startsWith(args[0].toLowerCase(Locale.ROOT))) {
            List<String> out = new ArrayList<>();
            out.add("reload");
            return out;
        }
        return Collections.emptyList();
    }
}
