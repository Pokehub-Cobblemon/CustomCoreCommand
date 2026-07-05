package com.venned.customCoreCommands.managers;

import com.venned.customCoreCommands.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks pending teleport requests. Keyed by the target's UUID; each target
 * keeps only its most recent incoming request. Requests auto-expire.
 */
public class TpaManager {

    private static final class Request {
        private final UUID requester;
        private final BukkitTask expiryTask;

        private Request(UUID requester, BukkitTask expiryTask) {
            this.requester = requester;
            this.expiryTask = expiryTask;
        }
    }

    private final Main plugin;
    private final Map<UUID, Request> requests = new ConcurrentHashMap<>();

    public TpaManager(Main plugin) {
        this.plugin = plugin;
    }

    public int expireSeconds() {
        return Math.max(5, plugin.getConfig().getInt("tpa.expire-seconds", 60));
    }


    public UUID getRequester(UUID target) {
        Request request = requests.get(target);
        return request == null ? null : request.requester;
    }

    public void createRequest(Player requester, Player target) {
        cancel(target.getUniqueId());

        UUID requesterId = requester.getUniqueId();
        UUID targetId = target.getUniqueId();

        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Request removed = requests.remove(targetId);
            if (removed == null) {
                return;
            }
            Player req = Bukkit.getPlayer(requesterId);
            Player tgt = Bukkit.getPlayer(targetId);
            if (req != null) {
                plugin.messages().send(req, "tpa-expired-sender",
                        "%target%", tgt != null ? tgt.getName() : "player");
            }
            if (tgt != null) {
                plugin.messages().send(tgt, "tpa-expired-target",
                        "%target%", req != null ? req.getName() : "player");
            }
        }, expireSeconds() * 20L);

        requests.put(targetId, new Request(requesterId, task));
    }


    public void consume(UUID target) {
        Request request = requests.remove(target);
        if (request == null) {
            return;
        }
        if (request.expiryTask != null) {
            request.expiryTask.cancel();
        }
    }

    private void cancel(UUID target) {
        Request request = requests.remove(target);
        if (request != null && request.expiryTask != null) {
            request.expiryTask.cancel();
        }
    }
}
