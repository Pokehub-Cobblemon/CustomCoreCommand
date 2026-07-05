package com.venned.customCoreCommands.listeners;

import com.venned.customCoreCommands.managers.GodManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/** Makes god-mode players immune to all damage and to losing hunger. */
public class GodListener implements Listener {

    private final GodManager godManager;

    public GodListener(GodManager godManager) {
        this.godManager = godManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (godManager.isGod(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHunger(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (godManager.isGod(player.getUniqueId()) && event.getFoodLevel() < player.getFoodLevel()) {
            event.setCancelled(true);
        }
    }
}
