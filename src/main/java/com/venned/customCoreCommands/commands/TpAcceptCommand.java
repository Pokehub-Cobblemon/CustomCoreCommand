package com.venned.customCoreCommands.commands;

import com.venned.customCoreCommands.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;


public class TpAcceptCommand implements CommandExecutor {

    private final Main plugin;

    public TpAcceptCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player target)) {
            plugin.messages().send(sender, "player-only");
            return true;
        }
        if (!target.hasPermission("customcore.tpa")) {
            plugin.messages().send(sender, "no-permission");
            return true;
        }

        UUID requesterId = plugin.tpaManager().getRequester(target.getUniqueId());
        if (requesterId == null) {
            plugin.messages().send(sender, "tpa-no-request");
            return true;
        }

        // Optional player argument to make sure we accept the right person.
        if (args.length >= 1) {
            Player named = Bukkit.getPlayerExact(args[0]);
            if (named == null || !named.getUniqueId().equals(requesterId)) {
                plugin.messages().send(sender, "tpa-not-from", "%target%", args[0]);
                return true;
            }
        }

        plugin.tpaManager().consume(target.getUniqueId());

        Player requester = Bukkit.getPlayer(requesterId);
        if (requester == null) {
            plugin.messages().send(sender, "tpa-requester-offline");
            return true;
        }

        requester.teleport(target.getLocation());
        plugin.messages().send(target, "tpa-accepted-target", "%target%", requester.getName());
        plugin.messages().send(requester, "tpa-accepted-sender", "%target%", target.getName());
        return true;
    }
}
