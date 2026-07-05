package com.venned.customCoreCommands.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class CommandUtil {


    public static List<String> onlinePlayerNames(CommandSender viewer) {
        List<String> names = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (viewer instanceof Player && !((Player) viewer).canSee(player)) {
                continue;
            }
            names.add(player.getName());
        }
        return names;
    }

    public static List<String> filter(List<String> options, String current) {
        String lower = current == null ? "" : current.toLowerCase(Locale.ROOT);
        List<String> result = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase(Locale.ROOT).startsWith(lower)) {
                result.add(option);
            }
        }
        return result;
    }
}
