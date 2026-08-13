package me.advancedtags.menu;

import me.advancedtags.AdvancedTags;
import me.advancedtags.core.Tag;
import me.advancedtags.utils.SchedulerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.List;
import java.util.Map;

public class MenuListener implements Listener {
    private final AdvancedTags plugin;

    public MenuListener(AdvancedTags plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof TagMenu menu)) return;

        event.setCancelled(true);
        event.setResult(Event.Result.DENY);

        if (!(event.getWhoClicked() instanceof Player player)) return;

        int slot = event.getRawSlot();
        int size = event.getInventory().getSize();
        int maxItems = size - 9;

        if (slot < 0 || slot >= size) {
            player.updateInventory();
            return;
        }

        if (slot == size - 6 && menu.getPage() > 0) {
            int targetPage = menu.getPage() - 1;
            SchedulerUtils.runDelayedOnPlayer(plugin, player, () -> new TagMenu(plugin, player, targetPage).open(), 1L);
            return;
        }

        if (slot == size - 5) {
            SchedulerUtils.runDelayedOnPlayer(plugin, player, () -> {
                player.closeInventory();
                plugin.getTagManager().clearTag(player);
            }, 1L);
            return;
        }

        if (slot == size - 4 && (menu.getPage() + 1) * maxItems < menu.getTotalAvailable()) {
            int targetPage = menu.getPage() + 1;
            SchedulerUtils.runDelayedOnPlayer(plugin, player, () -> new TagMenu(plugin, player, targetPage).open(), 1L);
            return;
        }

        if (slot < maxItems) {
            List<Tag> tagsOnPage = menu.getTagsOnPage();
            if (slot < tagsOnPage.size()) {
                Tag selected = tagsOnPage.get(slot);

                if (!plugin.getTagManager().hasTagPermission(player, selected.getId())) {
                    plugin.getMessageManager().sendConfigMessage(player, "locked-tag", Map.of());
                    player.updateInventory();
                    return;
                }

                SchedulerUtils.runDelayedOnPlayer(plugin, player, () -> {
                    player.closeInventory();
                    plugin.getTagManager().selectTag(player, selected);
                }, 1L);
                return;
            }
        }

        player.updateInventory();
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getInventory().getHolder() instanceof TagMenu)) return;

        int topSize = event.getView().getTopInventory().getSize();
        for (int slot : event.getRawSlots()) {
            if (slot < topSize) {
                event.setCancelled(true);
                if (event.getWhoClicked() instanceof Player player) {
                    player.updateInventory();
                }
                return;
            }
        }
    }
}