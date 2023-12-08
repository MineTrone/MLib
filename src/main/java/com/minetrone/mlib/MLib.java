package com.minetrone.mlib;

import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public class MLib {

    @Getter
    private static JavaPlugin instance;

    @Getter
    private static String prefix;

    @Getter
    private static BukkitAudiences adventure;

    @Getter
    private static ClassLoader pluginClassLoader = instance.getClass().getClassLoader();

    public MLib(JavaPlugin plugin, String pluginName) {
        instance = plugin;
        prefix = pluginName;
        adventure = BukkitAudiences.create(plugin);
    }

}