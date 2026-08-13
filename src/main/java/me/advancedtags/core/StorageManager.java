package me.advancedtags.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.advancedtags.AdvancedTags;
import me.advancedtags.utils.SchedulerUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class StorageManager {
    private final AdvancedTags plugin;
    private final Map<UUID, String> playerTags = new ConcurrentHashMap<>();
    private final File file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final ReentrantLock fileLock = new ReentrantLock();

    public StorageManager(AdvancedTags plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "player.json");
    }

    public void load() {
        if (!file.exists()) return;
        fileLock.lock();
        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<UUID, String>>(){}.getType();
            Map<UUID, String> data = gson.fromJson(reader, type);
            if (data != null) {
                playerTags.putAll(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fileLock.unlock();
        }
    }

    public void save() {
        SchedulerUtils.runAsync(this::saveSync);
    }

    public void saveSync() {
        fileLock.lock();
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try (Writer writer = new FileWriter(file)) {
                gson.toJson(playerTags, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fileLock.unlock();
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