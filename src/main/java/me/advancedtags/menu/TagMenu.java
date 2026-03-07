/*
 * AdvancedTags - A modern Minecraft title management system.
 * Copyright (C) 2026 ozan
 * 
 * Licensed under Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)
 * You may not use this work for commercial purposes.
 * For more info: https://creativecommons.org/licenses/by-nc/4.0/
 */
package me.advancedtags.menu;

import me.advancedtags.AdvancedTags;
import me.advancedtags.core.Tag;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TagMenu implements InventoryHolder {
    private final AdvancedTags plugin;
    private final Player player;
    private final int page;
    private final Inventory inventory;
    private final List<Tag> allTags;

    public TagMenu(AdvancedTags plugin, Player player, int page) {
        this.plugin = plugin;
        this.player = player;
        this.page = page;
        this.allTags = new ArrayList<>(plugin.getTagManager().getTags().values());

        String titleStr = plugin.getConfig().getString("menu_settings.title", "&8[&aTag Seçici&8]");
        Component title = plugin.getMessageManager().colorize(titleStr);
        int size = plugin.getConfig().getInt("menu_settings.size", 54);

        this.inventory = Bukkit.createInventory(this, size, title);
        setup();
    }

    private void setup() {
        int size = inventory.getSize();
        int maxItems = size - 9;

        String fillerMatName = plugin.getConfig().getString("menu_settings.fill_material", "BLACK_STAINED_GLASS_PANE");
        Material fillerMat = Material.matchMaterial(fillerMatName);
        if (fillerMat == null) fillerMat = Material.BLACK_STAINED_GLASS_PANE;

        ItemStack filler = new ItemStack(fillerMat);
        ItemMeta fm = filler.getItemMeta();
        if (fm != null) {
            fm.displayName(Component.empty());
            filler.setItemMeta(fm);
        }

        for (int i = maxItems; i < size; i++) {
            inventory.setItem(i, filler);
        }

        int start = page * maxItems;
        int end = Math.min(start + maxItems, allTags.size());

        String currentTagId = plugin.getStorageManager().getTag(player.getUniqueId());
        String lockedMatName = plugin.getConfig().getString("menu_settings.locked.material", "GRAY_DYE");
        Material lockedMat = Material.matchMaterial(lockedMatName);
        if (lockedMat == null) lockedMat = Material.GRAY_DYE;
        String lockedLore = plugin.getConfig().getString("menu_settings.locked.lore-append", "&c✘ Bu ünvana sahip değilsin!");

        for (int i = start; i < end; i++) {
            Tag tag = allTags.get(i);
            boolean hasPerm = player.hasPermission("advancedtags.tag." + tag.getId());

            ItemStack item = new ItemStack(hasPerm ? tag.getMaterial() : lockedMat);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(plugin.getMessageManager().colorize(tag.getDisplay()));

                List<Component> lore = new ArrayList<>();
                for (String l : tag.getLore()) {
                    lore.add(plugin.getMessageManager().colorize(l));
                }
                if (!hasPerm) {
                    lore.add(plugin.getMessageManager().colorize(lockedLore));
                }
                meta.lore(lore);

                if (tag.getId().equals(currentTagId)) {
                    meta.addEnchant(Enchantment.LURE, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                item.setItemMeta(meta);
            }
            inventory.setItem(i - start, item);
        }

        if (page > 0) {
            String prevMatName = plugin.getConfig().getString("menu_settings.prev_page_button.material", "ARROW");
            Material prevMat = Material.matchMaterial(prevMatName);
            if (prevMat == null) prevMat = Material.ARROW;

            ItemStack prev = new ItemStack(prevMat);
            ItemMeta pm = prev.getItemMeta();
            if (pm != null) {
                pm.displayName(plugin.getMessageManager().colorize(plugin.getConfig().getString("menu_settings.prev_page_button.display_name", "&aÖnceki Sayfa")));
                List<Component> lore = new ArrayList<>();
                for (String l : plugin.getConfig().getStringList("menu_settings.prev_page_button.lore")) {
                    lore.add(plugin.getMessageManager().colorize(l));
                }
                pm.lore(lore);
                prev.setItemMeta(pm);
            }
            inventory.setItem(size - 6, prev);
        }

        String clearMatName = plugin.getConfig().getString("menu_settings.no_tag_button.material", "BARRIER");
        Material clearMat = Material.matchMaterial(clearMatName);
        if (clearMat == null) clearMat = Material.BARRIER;
        
        ItemStack clear = new ItemStack(clearMat);
        ItemMeta cm = clear.getItemMeta();
        if (cm != null) {
            cm.displayName(plugin.getMessageManager().colorize(plugin.getConfig().getString("menu_settings.no_tag_button.display_name", "&cTagi Kapat/Sıfırla")));
            List<Component> lore = new ArrayList<>();
            for (String l : plugin.getConfig().getStringList("menu_settings.no_tag_button.lore")) {
                lore.add(plugin.getMessageManager().colorize(l));
            }
            cm.lore(lore);
            clear.setItemMeta(cm);
        }
        inventory.setItem(size - 5, clear);

        if (end < allTags.size()) {
            String nextMatName = plugin.getConfig().getString("menu_settings.next_page_button.material", "ARROW");
            Material nextMat = Material.matchMaterial(nextMatName);
            if (nextMat == null) nextMat = Material.ARROW;

            ItemStack next = new ItemStack(nextMat);
            ItemMeta nm = next.getItemMeta();
            if (nm != null) {
                nm.displayName(plugin.getMessageManager().colorize(plugin.getConfig().getString("menu_settings.next_page_button.display_name", "&aSonraki Sayfa")));
                List<Component> lore = new ArrayList<>();
                for (String l : plugin.getConfig().getStringList("menu_settings.next_page_button.lore")) {
                    lore.add(plugin.getMessageManager().colorize(l));
                }
                nm.lore(lore);
                next.setItemMeta(nm);
            }
            inventory.setItem(size - 4, next);
        }
    }

    public void open() {
        player.openInventory(inventory);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public int getPage() {
        return page;
    }

    public List<Tag> getTagsOnPage() {
        int maxItems = inventory.getSize() - 9;
        int start = page * maxItems;
        int end = Math.min(start + maxItems, allTags.size());
        if (start >= allTags.size()) return new ArrayList<>();
        return allTags.subList(start, end);
    }

    public int getTotalAvailable() {
        return allTags.size();
    }
}