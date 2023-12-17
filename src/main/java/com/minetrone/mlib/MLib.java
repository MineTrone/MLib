package com.minetrone.mlib;

import com.minetrone.mlib.adventure.AdventureAPI;
import com.minetrone.mlib.config.ConfigAPI;
import com.minetrone.mlib.connection.ConnectionAPI;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public class MLib {

    @Getter
    private static JavaPlugin instance;

    @Getter
    private static String prefix;

    @Getter
    private static BukkitAudiences adventure;

    @Getter @Setter
    private static ConnectionAPI connectionAPI;

    @Getter
    private static boolean initialized = false;

    public MLib(JavaPlugin plugin, String pluginName) {
        instance = plugin;
        prefix = pluginName;
        adventure = BukkitAudiences.create(plugin);
        initialized = true;
    }

    public static AdventureAPI getAdventureAPI() {
        return new AdventureAPI(instance);
    }

    public static AdventureAPI getAdventureAPI(JavaPlugin instance) {
        return new AdventureAPI(instance);
    }

    public static ConfigAPI getConfigAPI() {
        return new ConfigAPI(instance);
    }

    public static ConfigAPI getConfigAPI(JavaPlugin instance) {
        return new ConfigAPI(instance);
    }

    public static ConnectionAPI getConnectionAPI(String patchID) {
        ConnectionAPI connectionAPI = new ConnectionAPI();
        connectionAPI.postConnection(instance, patchID);
        connectionAPI.getConnection(connectionAPI);
        return connectionAPI;
    }

}