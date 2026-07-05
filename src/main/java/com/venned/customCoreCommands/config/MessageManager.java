package com.venned.customCoreCommands.config;

import com.venned.customCoreCommands.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MessageManager {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    private final Main plugin;
    private String prefix;

    public MessageManager(Main plugin) {
        this.plugin = plugin;
        reload();
    }


    public void reload() {
        this.prefix = plugin.getConfig().getString("prefix", "");
    }

    public String raw(String key) {
        return plugin.getConfig().getString("messages." + key, "&cMissing message: " + key);
    }


    public String format(String key, String... replacements) {
        String message = raw(key).replace("%prefix%", prefix);
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            message = message.replace(replacements[i], replacements[i + 1]);
        }
        return color(message);
    }
    public void send(CommandSender target, String key, String... replacements) {
        if (target == null) {
            return;
        }
        String message = format(key, replacements);
        if (message.isEmpty()) {
            return;
        }
        target.sendMessage(message);
    }


    public static String color(String input) {
        if (input == null || input.isEmpty()) {
            return input == null ? "" : input;
        }
        Matcher matcher = HEX_PATTERN.matcher(input);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            String hex = net.md_5.bungee.api.ChatColor.of("#" + matcher.group(1)).toString();
            matcher.appendReplacement(builder, Matcher.quoteReplacement(hex));
        }
        matcher.appendTail(builder);
        return ChatColor.translateAlternateColorCodes('&', builder.toString());
    }
}
