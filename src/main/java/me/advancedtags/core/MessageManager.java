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
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;
import java.util.List;

public class MessageManager {
    private final AdvancedTags plugin;
    private YamlConfiguration config;
    private final List<String> availableLangs = List.of(
        "en", "tr", "de", "es", "ru", "zh", "ja", "az", "fr", "ar", 
        "nl", "id", "hy", "it", "gd", "sv", "ky", "ko", "hu", "cs", 
        "el", "fa", "pl", "ro", "vi", "pt", "th", "uk"
    );

    public MessageManager(AdvancedTags plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) langFolder.mkdirs();

        for (String langName : availableLangs) {
            saveLangFile(langName + ".yml");
        }

        String langSetting = plugin.getConfig().getString("settings.lang", "tr");
        File file = new File(langFolder, langSetting + ".yml");

        if (!file.exists()) {
            file = new File(langFolder, "tr.yml");
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    private void saveLangFile(String name) {
        File file = new File(plugin.getDataFolder(), "lang/" + name);
        if (!file.exists()) {
            plugin.saveResource("lang/" + name, false);
        }
    }

    public Component colorize(String text) {
        if (text == null) return Component.empty();
        String parsed = text.replace("&0", "<black>").replace("&1", "<dark_blue>").replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>").replace("&4", "<dark_red>").replace("&5", "<dark_purple>")
                .replace("&6", "<gold>").replace("&7", "<gray>").replace("&8", "<dark_gray>")
                .replace("&9", "<blue>").replace("&a", "<green>").replace("&b", "<aqua>")
                .replace("&c", "<red>").replace("&d", "<light_purple>").replace("&e", "<yellow>")
                .replace("&f", "<white>").replace("&l", "<bold>").replace("&o", "<italic>")
                .replace("&n", "<underlined>").replace("&m", "<strikethrough>").replace("&k", "<obfuscated>")
                .replace("&r", "<reset>");
        return MiniMessage.miniMessage().deserialize(parsed);
    }

    public Component getMessage(String path, Map<String, String> placeholders) {
        String prefix = config.getString("prefix", "");
        String msg = config.getString(path, path);
        String full = prefix + msg;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            full = full.replace(entry.getKey(), entry.getValue());
        }
        return colorize(full);
    }

    public Component getRawMessage(String path, Map<String, String> placeholders) {
        String msg = config.getString(path, path);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            msg = msg.replace(entry.getKey(), entry.getValue());
        }
        return colorize(msg);
    }
}