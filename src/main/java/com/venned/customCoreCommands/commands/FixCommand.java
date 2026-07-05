package com.venned.customCoreCommands.commands;

import com.venned.customCoreCommands.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FixCommand implements TabExecutor {

    private final Main plugin;

    public FixCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.messages().send(sender, "player-only");
            return true;
        }
        if (!player.hasPermission("customcore.fix")) {
            plugin.messages().send(sender, "no-permission");
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("all")) {
            if (!player.hasPermission("customcore.fix.all")) {
                plugin.messages().send(sender, "no-permission");
                return true;
            }
            int fixed = repairInventory(player.getInventory());
            plugin.messages().send(sender, fixed == 0 ? "fix-nothing-all" : "fix-success-all");
            return true;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.getType().isAir()) {
            plugin.messages().send(sender, "fix-no-item");
            return true;
        }
        if (!isRepairable(hand)) {
            plugin.messages().send(sender, "fix-not-damageable");
            return true;
        }
        setFullDurability(hand);
        player.getInventory().setItemInMainHand(hand);
        plugin.messages().send(sender, "fix-success");
        return true;
    }

    private int repairInventory(PlayerInventory inventory) {
        int fixed = 0;

        ItemStack[] storage = inventory.getStorageContents();
        for (int i = 0; i < storage.length; i++) {
            if (repairIfDamaged(storage[i])) {
                inventory.setItem(i, storage[i]);
                fixed++;
            }
        }

        ItemStack[] armor = inventory.getArmorContents();
        boolean armorChanged = false;
        for (ItemStack piece : armor) {
            if (repairIfDamaged(piece)) {
                armorChanged = true;
                fixed++;
            }
        }
        if (armorChanged) {
            inventory.setArmorContents(armor);
        }

        ItemStack offHand = inventory.getItemInOffHand();
        if (repairIfDamaged(offHand)) {
            inventory.setItemInOffHand(offHand);
            fixed++;
        }

        return fixed;
    }

    /** True if the item type has durability and its meta supports damage. */
    private boolean isRepairable(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return false;
        }
        if (item.getType().getMaxDurability() <= 0) {
            return false;
        }
        return item.getItemMeta() instanceof Damageable;
    }

    /** Repairs an item only if it is actually damaged. Returns true if changed. */
    private boolean repairIfDamaged(ItemStack item) {
        if (!isRepairable(item)) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        Damageable damageable = (Damageable) meta;
        if (!damageable.hasDamage()) {
            return false;
        }
        damageable.setDamage(0);
        item.setItemMeta(meta);
        return true;
    }

    private void setFullDurability(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        Damageable damageable = (Damageable) meta;
        damageable.setDamage(0);
        item.setItemMeta(meta);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> out = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission("customcore.fix.all")
                && "all".startsWith(args[0].toLowerCase(Locale.ROOT))) {
            out.add("all");
        }
        return out;
    }
}
