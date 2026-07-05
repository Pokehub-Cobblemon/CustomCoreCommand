package com.venned.customCoreCommands;

import com.venned.customCoreCommands.commands.CustomCoreCommand;
import com.venned.customCoreCommands.commands.EnderChestCommand;
import com.venned.customCoreCommands.commands.FixCommand;
import com.venned.customCoreCommands.commands.GamemodeCommand;
import com.venned.customCoreCommands.commands.GodCommand;
import com.venned.customCoreCommands.commands.OpenInvCommand;
import com.venned.customCoreCommands.commands.TpAcceptCommand;
import com.venned.customCoreCommands.commands.TpDenyCommand;
import com.venned.customCoreCommands.commands.TpaCommand;
import com.venned.customCoreCommands.commands.TrashCommand;
import com.venned.customCoreCommands.config.MessageManager;
import com.venned.customCoreCommands.listeners.GodListener;
import com.venned.customCoreCommands.listeners.TrashListener;
import com.venned.customCoreCommands.managers.GodManager;
import com.venned.customCoreCommands.managers.TpaManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private MessageManager messages;
    private GodManager godManager;
    private TpaManager tpaManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        this.messages = new MessageManager(this);
        this.godManager = new GodManager();
        this.tpaManager = new TpaManager(this);

        registerCommands();
        registerListeners();

        getLogger().info("CustomCoreCommands enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomCoreCommands disabled.");
    }

    private void registerCommands() {
        bind("gamemode", new GamemodeCommand(this));
        bind("god", new GodCommand(this));
        bind("openinv", new OpenInvCommand(this));
        bind("enderchest", new EnderChestCommand(this));
        bind("fix", new FixCommand(this));
        bind("tpa", new TpaCommand(this));
        bind("tpaccept", new TpAcceptCommand(this));
        bind("tpdeny", new TpDenyCommand(this));
        bind("trash", new TrashCommand(this));
        bind("customcorecommands", new CustomCoreCommand(this));
    }

    private void bind(String name, CommandExecutor executor) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            getLogger().warning("Command '" + name + "' is not declared in plugin.yml.");
            return;
        }
        command.setExecutor(executor);
        if (executor instanceof TabCompleter completer) {
            command.setTabCompleter(completer);
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new GodListener(godManager), this);
        getServer().getPluginManager().registerEvents(new TrashListener(), this);
    }

    public MessageManager messages() {
        return messages;
    }

    public GodManager godManager() {
        return godManager;
    }

    public TpaManager tpaManager() {
        return tpaManager;
    }
}
