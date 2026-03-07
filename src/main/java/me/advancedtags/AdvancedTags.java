/*
 * AdvancedTags - A modern Minecraft title management system.
 * Copyright (C) 2026 ozan
 * 
 * Licensed under Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * You may not use this work for commercial purposes.
 * For more info: https://creativecommons.org/licenses/by-nc/4.0/
 */
package me.advancedtags;

import me.advancedtags.command.AdminCommand;
import me.advancedtags.command.UnvanCommand;
import me.advancedtags.core.MessageManager;
import me.advancedtags.core.StorageManager;
import me.advancedtags.core.TagManager;
import me.advancedtags.hook.TagPlaceholder;
import me.advancedtags.menu.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancedTags extends JavaPlugin {
    private TagManager tagManager;
    private StorageManager storageManager;
    private MessageManager messageManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.messageManager = new MessageManager(this);
        this.tagManager = new TagManager(this);
        this.storageManager = new StorageManager(this);
        this.storageManager.load();

        getCommand("unvan").setExecutor(new UnvanCommand(this));
        
        AdminCommand adminCommand = new AdminCommand(this);
        getCommand("advancedtags").setExecutor(adminCommand);
        getCommand("advancedtags").setTabCompleter(adminCommand);
        
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TagPlaceholder(this).register();
        }
    }

    @Override
    public void onDisable() {
        if (this.storageManager != null) {
            this.storageManager.saveSync();
        }
    }

    public TagManager getTagManager() { return tagManager; }
    public StorageManager getStorageManager() { return storageManager; }
    public MessageManager getMessageManager() { return messageManager; }
}