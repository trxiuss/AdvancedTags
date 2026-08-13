package me.advancedtags.hook;

import me.advancedtags.AdvancedTags;
import me.advancedtags.core.Tag;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class TagPlaceholder extends PlaceholderExpansion {
    private final AdvancedTags plugin;

    public TagPlaceholder(AdvancedTags plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "advancedtags";
    }

    @Override
    public @NotNull String getAuthor() {
        return "ozan";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.2";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";
        if (!params.equals("unvan")) return null;

        String tagId = plugin.getStorageManager().getTag(player.getUniqueId());
        if (tagId == null) return "";

        Tag tag = plugin.getTagManager().getTag(tagId);
        if (tag == null) return "";

        return plugin.getMessageManager().toLegacy(tag.getDisplay());
    }
}