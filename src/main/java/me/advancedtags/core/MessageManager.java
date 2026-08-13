package me.advancedtags.core;

import me.advancedtags.AdvancedTags;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Map;

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

        String langSetting = plugin.getConfig().getString("settings.lang", "en");
        File file = new File(langFolder, langSetting + ".yml");

        if (!file.exists()) {
            file = new File(langFolder, "en.yml");
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
        if (text == null || text.isEmpty()) return Component.empty();
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

    public String toLegacy(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public String toLegacy(String text) {
        return toLegacy(colorize(text));
    }

    public void sendMessage(CommandSender sender, String text, Map<String, String> placeholders) {
        String msg = text;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            msg = msg.replace(entry.getKey(), entry.getValue());
        }
        Audience audience = plugin.getAdventure().sender(sender);
        audience.sendMessage(colorize(msg));
    }

    public void sendConfigMessage(CommandSender sender, String path, Map<String, String> placeholders) {
        String prefix = config.getString("prefix", "");
        String msg = config.getString(path, path);
        sendMessage(sender, prefix + msg, placeholders);
    }

    public void sendActionBar(Player player, String path, Map<String, String> placeholders) {
        String msg = config.getString(path, path);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            msg = msg.replace(entry.getKey(), entry.getValue());
        }
        Audience audience = plugin.getAdventure().player(player);
        audience.sendActionBar(colorize(msg));
    }

    public void sendTitle(Player player, String mainPath, String subPath, Map<String, String> placeholders) {
        String main = config.getString(mainPath, "");
        String sub = config.getString(subPath, "");
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            main = main.replace(entry.getKey(), entry.getValue());
            sub = sub.replace(entry.getKey(), entry.getValue());
        }
        Audience audience = plugin.getAdventure().player(player);
        audience.showTitle(Title.title(colorize(main), colorize(sub)));
    }
}