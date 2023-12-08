package com.minetrone.mlib.module.helper;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class Manager {

    protected JavaPlugin plugin;

    protected Manager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void loadData() throws Throwable;
}