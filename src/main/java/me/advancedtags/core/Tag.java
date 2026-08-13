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