package me.advancedtags.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public final class SchedulerUtils {

    private static final ExecutorService ASYNC_POOL = Executors.newCachedThreadPool();

    private SchedulerUtils() {}

    public static void runAsync(Runnable runnable) {
        CompletableFuture.runAsync(runnable, ASYNC_POOL);
    }

    public static void runDelayedOnPlayer(Plugin plugin, Player player, Runnable runnable, long delayTicks) {
        try {
            Method getSchedulerMethod = player.getClass().getMethod("getScheduler");
            Object entityScheduler = getSchedulerMethod.invoke(player);
            Method runDelayedMethod = entityScheduler.getClass().getMethod("runDelayed", Plugin.class, Consumer.class, Runnable.class, long.class);
            Consumer<Object> consumer = task -> runnable.run();
            runDelayedMethod.invoke(entityScheduler, plugin, consumer, null, delayTicks);
        } catch (Throwable ignored) {
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks);
        }
    }

    public static void shutdown() {
        ASYNC_POOL.shutdown();
    }
}