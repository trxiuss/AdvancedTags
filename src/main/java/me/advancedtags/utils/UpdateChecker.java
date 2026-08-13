package me.advancedtags.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.advancedtags.AdvancedTags;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UpdateChecker implements Listener {

    private final AdvancedTags plugin;
    private final String currentVersion;
    private String latestVersion = null;
    private boolean updateAvailable = false;
    private final ScheduledExecutorService scheduler;

    public UpdateChecker(AdvancedTags plugin) {
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion();
        
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });

        Bukkit.getPluginManager().registerEvents(this, plugin);

        this.scheduler.execute(() -> {
            if (fetchUpdate()) {
                notifyOpsAndConsole();
                this.scheduler.schedule(() -> {
                    if (updateAvailable) {
                        sendConsoleUpdate(Bukkit.getConsoleSender());
                    }
                }, 5, TimeUnit.MINUTES);
            }
        });

        this.scheduler.scheduleAtFixedRate(() -> {
            if (fetchUpdate()) {
                notifyOpsAndConsole();
            }
        }, 12, 12, TimeUnit.HOURS);
    }

    private boolean fetchUpdate() {
        try {
            URL url = new URL("https://api.modrinth.com/v2/project/advancedtags/version");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "AdvancedTags-UpdateChecker");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() == 200) {
                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                    JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
                    if (jsonArray.size() > 0) {
                        JsonObject latestRelease = jsonArray.get(0).getAsJsonObject();
                        latestVersion = latestRelease.get("version_number").getAsString();
                        updateAvailable = isNewerVersion(currentVersion, latestVersion);
                        return updateAvailable;
                    }
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    private boolean isNewerVersion(String current, String latest) {
        try {
            current = current.replaceAll("[^0-9.]", "");
            latest = latest.replaceAll("[^0-9.]", "");
            String[] currArr = current.split("\\.");
            String[] latArr = latest.split("\\.");
            int length = Math.max(currArr.length, latArr.length);
            for (int i = 0; i < length; i++) {
                int currPart = i < currArr.length ? Integer.parseInt(currArr[i]) : 0;
                int latPart = i < latArr.length ? Integer.parseInt(latArr[i]) : 0;
                if (latPart > currPart) return true;
                if (currPart > latPart) return false;
            }
        } catch (Exception ignored) {}
        return false;
    }

    private void notifyOpsAndConsole() {
        if (!updateAvailable) return;
        sendConsoleUpdate(Bukkit.getConsoleSender());
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                sendPlayerUpdate(player);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (updateAvailable && event.getPlayer().isOp()) {
            SchedulerUtils.runDelayedOnPlayer(plugin, event.getPlayer(), () -> {
                if (event.getPlayer().isOnline()) {
                    sendPlayerUpdate(event.getPlayer());
                }
            }, 150L);
        }
    }

    private void sendConsoleUpdate(CommandSender sender) {
        String msg = "<dark_gray>--------------------------------------------------\n" +
                     "<aqua><bold>ADVANCED TAGS UPDATE</bold></aqua>\n" +
                     "<gray>A new version of the plugin is available!\n" +
                     "<red>Current: " + currentVersion + " <dark_gray>» <green>New: " + latestVersion + "\n" +
                     "<yellow>Download: <green><underlined>https://modrinth.com/plugin/advancedtags</underlined></green>\n" +
                     "<dark_gray>--------------------------------------------------";
        plugin.getMessageManager().sendMessage(sender, msg, Map.of());
    }

    private void sendPlayerUpdate(Player player) {
        String msg = "<dark_gray>--------------------------------------------------\n" +
                     "<aqua><bold>ADVANCED TAGS UPDATE</bold></aqua>\n" +
                     "<gray>A new version of the plugin is available!\n" +
                     "<red>Current: " + currentVersion + " <dark_gray>» <green>New: " + latestVersion + "\n" +
                     "<yellow>Download: <green><underlined>https://modrinth.com/plugin/advancedtags</underlined></green>\n" +
                     "<dark_gray>--------------------------------------------------";
        plugin.getMessageManager().sendMessage(player, msg, Map.of());
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}