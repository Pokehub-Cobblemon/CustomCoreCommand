package com.venned.customCoreCommands.commands;

import com.venned.customCoreCommands.Main;
import com.venned.customCoreCommands.config.MessageManager;
import com.venned.customCoreCommands.util.TrashHolder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TrashCommand implements CommandExecutor {

    private final Main plugin;

    public TrashCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.messages().send(sender, "player-only");
            return true;
        }
        if (!player.hasPermission("customcore.trash")) {
            plugin.messages().send(sender, "no-permission");
            return true;
        }

        int rows = plugin.getConfig().getInt("trash.rows", 3);
        rows = Math.max(1, Math.min(6, rows));
        String title = MessageManager.color(plugin.getConfig().getString("trash.title", "&8Trash"));

        TrashHolder holder = new TrashHolder();
        Inventory inventory = Bukkit.createInventory(holder, rows * 9, title);
        holder.setInventory(inventory);

        player.openInventory(inventory);
        plugin.messages().send(player, "trash-opened");
        return true;
    }
}
