package me.advancedtags.command;

import me.advancedtags.AdvancedTags;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class AdminCommand implements CommandExecutor, TabCompleter {
    private final AdvancedTags plugin;

    public AdminCommand(AdvancedTags plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("advancedtags.admin")) {
            plugin.getMessageManager().sendConfigMessage(sender, "no-permission", Map.of());
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            plugin.getMessageManager().load();
            plugin.getTagManager().loadTags();
            plugin.getMessageManager().sendConfigMessage(sender, "reloaded", Map.of());
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1 && sender.hasPermission("advancedtags.admin")) {
            String partial = args[0].toLowerCase();
            if ("reload".startsWith(partial)) {
                return List.of("reload");
            }
        }
        return List.of();
    }
}