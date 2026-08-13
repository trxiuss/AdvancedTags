package me.advancedtags.core;

import me.advancedtags.AdvancedTags;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TagManager implements Listener {
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
            String matName = section.getString(key + ".material", "PAPER");
            Material mat = Material.matchMaterial(matName);
            if (mat == null) mat = Material.PAPER;
            List<String> lore = section.getStringList(key + ".lore");
            tags.put(key, new Tag(key, display, mat, lore));
        }
    }

    public Map<String, Tag> getTags() {
        return tags;
    }

    public Tag getTag(String id) {
        return tags.get(id);
    }

    public boolean hasTagPermission(Player player, String tagId) {
        if (player.hasPermission("advancedtags.admin")) return true;
        if (player.hasPermission("advancedtags.tag.*")) return true;
        return player.hasPermission("advancedtags.tag." + tagId);
    }

    public boolean isOnCooldown(UUID uuid) {
        Long expiry = cooldowns.get(uuid);
        if (expiry == null) return false;
        if (expiry - System.currentTimeMillis() > 0) return true;
        cooldowns.remove(uuid);
        return false;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        cooldowns.remove(event.getPlayer().getUniqueId());
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
        boolean bypass = player.hasPermission("advancedtags.bypass.cooldown") || player.hasPermission("advancedtags.admin");
        
        if (!bypass && isOnCooldown(player.getUniqueId())) {
            long left = getCooldownSeconds(player.getUniqueId());
            plugin.getMessageManager().sendConfigMessage(player, "cooldown", Map.of("<time>", String.valueOf(left)));
            return;
        }

        plugin.getStorageManager().setTag(player.getUniqueId(), tag.getId());
        setCooldown(player.getUniqueId());
        playSound(player, "success");

        Map<String, String> placeholders = Map.of("<tag>", tag.getDisplay());
        plugin.getMessageManager().sendConfigMessage(player, "tag-selected", placeholders);
        plugin.getMessageManager().sendActionBar(player, "actionbar", placeholders);
        plugin.getMessageManager().sendTitle(player, "title.main", "title.sub", placeholders);
    }

    public void clearTag(Player player) {
        plugin.getStorageManager().setTag(player.getUniqueId(), null);
        plugin.getMessageManager().sendConfigMessage(player, "tag-cleared", Map.of());
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