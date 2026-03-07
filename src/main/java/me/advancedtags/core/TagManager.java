/*
 * AdvancedTags - A modern Minecraft title management system.
 * Copyright (C) 2026 ozan
 * 
 * Licensed under Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * You may not use this work for commercial purposes.
 * For more info: https://creativecommons.org/licenses/by-nc/4.0/
 */
package me.advancedtags.core;

import me.advancedtags.AdvancedTags;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TagManager {
    private final AdvancedTags plugin;
    private final Map<String, Tag> tags = new LinkedHashMap<>();
    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();

    public TagManager(AdvancedTags plugin) {
        this.plugin = plugin;
        loadTags();
    }

    public void loadTags() {
        tags.clear();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("tags");
        if (section == null) return;
        for (String key : section.getKeys(false)) {
            String display = section.getString(key + ".display_name", key);
            String matName = section.getString(key + ".material", "NAME_TAG");
            Material mat = Material.matchMaterial(matName);
            if (mat == null) mat = Material.NAME_TAG;
            List<String> lore = section.getStringList(key + ".lore");
            tags.put(key, new Tag(key, display, mat, lore));
        }
    }

    public Map<String, Tag> getTags() { return tags; }
    public Tag getTag(String id) { return tags.get(id); }

    public boolean isOnCooldown(UUID uuid) {
        if (!cooldowns.containsKey(uuid)) return false;
        return (cooldowns.get(uuid) - System.currentTimeMillis()) > 0;
    }

    public void setCooldown(UUID uuid) {
        long cd = plugin.getConfig().getLong("settings.cooldown", 1) * 1000L;
        cooldowns.put(uuid, System.currentTimeMillis() + cd);
    }

    public long getCooldownSeconds(UUID uuid) {
        if (!cooldowns.containsKey(uuid)) return 0;
        return Math.max(0, (cooldowns.get(uuid) - System.currentTimeMillis()) / 1000L);
    }

    public void selectTag(Player player, Tag tag) {
        // GÜNCELLEME: Eğer oyuncu adminse VEYA bypass yetkisi varsa cooldown kontrolünü atla.
        boolean bypass = player.hasPermission("advancedtags.bypass.cooldown") || player.hasPermission("advancedtags.admin");
        
        if (!bypass && isOnCooldown(player.getUniqueId())) {
            long left = getCooldownSeconds(player.getUniqueId());
            player.sendMessage(plugin.getMessageManager().getMessage("cooldown", Map.of("<time>", String.valueOf(left))));
            return;
        }

        plugin.getStorageManager().setTag(player.getUniqueId(), tag.getId());
        setCooldown(player.getUniqueId());
        playSound(player, "success");

        Map<String, String> placeholders = Map.of("<tag>", tag.getDisplay());
        player.sendMessage(plugin.getMessageManager().getMessage("tag-selected", placeholders));

        Component actionbar = plugin.getMessageManager().getRawMessage("actionbar", placeholders);
        player.sendActionBar(actionbar);

        Component title = plugin.getMessageManager().getRawMessage("title.main", placeholders);
        Component subtitle = plugin.getMessageManager().getRawMessage("title.sub", placeholders);
        player.showTitle(Title.title(title, subtitle));
    }

    public void clearTag(Player player) {
        plugin.getStorageManager().setTag(player.getUniqueId(), null);
        player.sendMessage(plugin.getMessageManager().getMessage("tag-cleared", Map.of()));
    }

    public void playSound(Player player, String type) {
        String sound = plugin.getConfig().getString("settings.sounds." + type);
        if (sound != null) {
            try {
                player.playSound(player.getLocation(), Sound.valueOf(sound), 1f, 1f);
            } catch (Exception ignored) {
            }
        }
    }
}