package com.venned.customCoreCommands.managers;

import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** In-memory store of players who currently have god mode enabled. */
public class GodManager {

    private final Set<UUID> godPlayers = Collections.synchronizedSet(new HashSet<>());

    public boolean isGod(UUID uuid) {
        return godPlayers.contains(uuid);
    }

    public boolean isGod(Player player) {
        return isGod(player.getUniqueId());
    }

    public void setGod(UUID uuid, boolean god) {
        if (god) {
            godPlayers.add(uuid);
        } else {
            godPlayers.remove(uuid);
        }
    }


    public boolean toggle(UUID uuid) {
        if (godPlayers.contains(uuid)) {
            godPlayers.remove(uuid);
            return false;
        }
        godPlayers.add(uuid);
        return true;
    }
}
