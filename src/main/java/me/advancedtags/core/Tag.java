/*
 * AdvancedTags - A modern Minecraft title management system.
 * Copyright (C) 2026 ozan
 * 
 * Licensed under Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * You may not use this work for commercial purposes.
 * For more info: https://creativecommons.org/licenses/by-nc/4.0/
 */
package me.advancedtags.core;

import org.bukkit.Material;
import java.util.List;

public class Tag {
    private final String id;
    private final String display;
    private final Material material;
    private final List<String> lore;

    public Tag(String id, String display, Material material, List<String> lore) {
        this.id = id;
        this.display = display;
        this.material = material;
        this.lore = lore;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public Material getMaterial() {
        return material;
    }

    public List<String> getLore() {
        return lore;
    }
}