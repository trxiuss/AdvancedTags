/*
 * AdvancedTags - A modern Minecraft title management system.
 * Copyright (C) 2026 ozan
 * 
 * Licensed under Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * You may not use this work for commercial purposes.
 * For more info: https://creativecommons.org/licenses/by-nc/4.0/
 */
package me.advancedtags.menu;

import me.advancedtags.AdvancedTags;
import me.advancedtags.core.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.List;
import java.util.Map;

public class MenuListener implements Listener {
    private final AdvancedTags plugin;

    public MenuListener(AdvancedTags plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event) {
        event.getCommands().removeIf(command -> command.contains(":"));
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof TagMenu)) return;
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) return;

        TagMenu menu = (TagMenu) event.getInventory().getHolder();
        int slot = event.getRawSlot();
        int size = event.getInventory().getSize();
        int maxItems = size - 9;

        if (slot < 0 || slot >= size) return;

        if (slot == size - 6 && menu.getPage() > 0) {
            new TagMenu(plugin, player, menu.getPage() - 1).open();
            return;
        }

        if (slot == size - 5) {
            player.closeInventory();
            plugin.getTagManager().clearTag(player);
            return;
        }

        if (slot == size - 4 && (menu.getPage() + 1) * maxItems < menu.getTotalAvailable()) {
            new TagMenu(plugin, player, menu.getPage() + 1).open();
            return;
        }

        if (slot < maxItems) {
            List<Tag> tagsOnPage = menu.getTagsOnPage();
            if (slot < tagsOnPage.size()) {
                Tag selected = tagsOnPage.get(slot);
                
                if (!player.hasPermission("advancedtags.tag." + selected.getId())) {
                    player.sendMessage(plugin.getMessageManager().getMessage("locked-tag", Map.of()));
                    return;
                }
                
                player.closeInventory();
                plugin.getTagManager().selectTag(player, selected);
            }
        }
    }
}