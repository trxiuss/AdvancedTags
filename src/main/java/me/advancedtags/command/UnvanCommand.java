/*
 * AdvancedTags - A modern Minecraft title management system.
 * Copyright (C) 2026 ozan
 * 
 * Licensed under Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * You may not use this work for commercial purposes.
 * For more info: https://creativecommons.org/licenses/by-nc/4.0/
 */
package me.advancedtags.command;

import me.advancedtags.AdvancedTags;
import me.advancedtags.menu.TagMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class UnvanCommand implements CommandExecutor {
    private final AdvancedTags plugin;

    public UnvanCommand(AdvancedTags plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (!player.hasPermission("advancedtags.command")) {
            player.sendMessage(plugin.getMessageManager().getMessage("no-permission", Map.of()));
            return true;
        }

        new TagMenu(plugin, player, 0).open();
        return true;
    }
}