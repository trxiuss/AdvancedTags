/*
 * AdvancedTags - A modern Minecraft title management system.
 * Copyright (C) 2026 ozan
 * 
 * Licensed under Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * You may not use this work for commercial purposes.
 * For more info: https://creativecommons.org/licenses/by-nc/4.0/
 */
package me.advancedtags.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.advancedtags.AdvancedTags;
import me.advancedtags.core.Tag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
        return "AI";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
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

        Component display = plugin.getMessageManager().colorize(tag.getDisplay());
        return LegacyComponentSerializer.legacySection().serialize(display);
    }
}