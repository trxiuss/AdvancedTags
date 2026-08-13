package me.advancedtags;

import me.advancedtags.command.AdminCommand;
import me.advancedtags.command.UnvanCommand;
import me.advancedtags.core.MessageManager;
import me.advancedtags.core.StorageManager;
import me.advancedtags.core.TagManager;
import me.advancedtags.hook.TagPlaceholder;
import me.advancedtags.menu.MenuListener;
import me.advancedtags.utils.SchedulerUtils;
import me.advancedtags.utils.UpdateChecker;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancedTags extends JavaPlugin {
    private TagManager tagManager;
    private StorageManager storageManager;
    private MessageManager messageManager;
    private BukkitAudiences adventure;
    private UpdateChecker updateChecker;

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        int pluginId = 33319;
        new Metrics(this, pluginId);

        saveDefaultConfig();
        this.messageManager = new MessageManager(this);
        this.tagManager = new TagManager(this);
        this.storageManager = new StorageManager(this);
        this.storageManager.load();

        this.updateChecker = new UpdateChecker(this);

        if (getCommand("unvan") != null) {
            getCommand("unvan").setExecutor(new UnvanCommand(this));
        }
        
        AdminCommand adminCommand = new AdminCommand(this);
        if (getCommand("advancedtags") != null) {
            getCommand("advancedtags").setExecutor(adminCommand);
            getCommand("advancedtags").setTabCompleter(adminCommand);
        }
        
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
        getServer().getPluginManager().registerEvents(this.tagManager, this);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TagPlaceholder(this).register();
        }
    }

    @Override
    public void onDisable() {
        if (this.updateChecker != null) {
            this.updateChecker.shutdown();
        }
        if (this.storageManager != null) {
            this.storageManager.saveSync();
        }
        SchedulerUtils.shutdown();
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public TagManager getTagManager() { return tagManager; }
    public StorageManager getStorageManager() { return storageManager; }
    public MessageManager getMessageManager() { return messageManager; }
    public BukkitAudiences getAdventure() { return adventure; }
}