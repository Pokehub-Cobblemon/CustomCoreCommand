package com.venned.customCoreCommands.listeners;

import com.venned.customCoreCommands.util.TrashHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

/** Deletes everything inside a trash menu when it is closed. */
public class TrashListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof TrashHolder) {
            event.getInventory().clear();
        }
    }
}
