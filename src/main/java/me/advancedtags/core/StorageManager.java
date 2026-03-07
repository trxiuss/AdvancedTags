/*
 * AdvancedTags - A modern Minecraft title management system.
 * Copyright (C) 2026 ozan
 * 
 * Licensed under Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * You may not use this work for commercial purposes.
 * For more info: https://creativecommons.org/licenses/by-nc/4.0/
 */
package me.advancedtags.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.advancedtags.AdvancedTags;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StorageManager {
    private final AdvancedTags plugin;
    private final Map<UUID, String> playerTags = new ConcurrentHashMap<>();
    private final File file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public StorageManager(AdvancedTags plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "player.json");
    }

    public void load() {
        if (!file.exists()) return;
        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<UUID, String>>(){}.getType();
            Map<UUID, String> data = gson.fromJson(reader, type);
            if (data != null) {
                playerTags.putAll(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        plugin.getServer().getAsyncScheduler().runNow(plugin, task -> saveSync());
    }

    public void saveSync() {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try (Writer writer = new FileWriter(file)) {
                gson.toJson(playerTags, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTag(UUID uuid, String tagId) {
        if (tagId == null) {
            playerTags.remove(uuid);
        } else {
            playerTags.put(uuid, tagId);
        }
        save();
    }

    public String getTag(UUID uuid) {
        return playerTags.get(uuid);
    }
}