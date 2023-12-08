package com.minetrone.mlib.module.container;

import com.google.common.base.Preconditions;
import com.minetrone.mlib.module.PluginModule;
import com.minetrone.mlib.module.helper.ModuleData;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class ModuleContainer implements ModulesContainer {
    private final Map<String, PluginModule> modulesMap = new HashMap<>();
    private final Map<PluginModule, ModuleData> modulesData = new HashMap<>();
    private final JavaPlugin plugin;

    public ModuleContainer(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerModule(PluginModule pluginModule, File modulesFolder, File modulesDataFolder) {
        String moduleName = pluginModule.getName().toLowerCase(Locale.ENGLISH);

        Preconditions.checkState(!modulesMap.containsKey(moduleName), "Module with name %s is already registered");

        File dataFolder = new File(modulesDataFolder, pluginModule.getName());
        File moduleFolder = new File(modulesFolder, pluginModule.getName());

        try {
            pluginModule.initModule(plugin, dataFolder, moduleFolder);
        } catch (Throwable err) {
            plugin.getLogger().severe("An unexpected error occurred while initializing the module " + moduleName + " !");
            plugin.getLogger().severe(err.getMessage() + "Contact " + pluginModule.getAuthor() + " for more information.");
            return;
        }

        modulesMap.put(moduleName, pluginModule);
    }

    @Override
    public void unregisterModule(PluginModule pluginModule) {
        ModuleData moduleData = modulesData.remove(pluginModule);

        if  (moduleData != null) {
            if (moduleData.getListeners() != null)
                Arrays.stream(moduleData.getListeners()).forEach(HandlerList::unregisterAll);
        }

        pluginModule.disableModule();

        modulesMap.remove(pluginModule.getName().toLowerCase(Locale.ENGLISH));
    }

    @Override
    public @Nullable PluginModule getModule(String name) {
        return this.modulesMap.get(name.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public Collection<PluginModule> getModules() {
        return new SequentialListBuilder<PluginModule>().build(modulesMap.values());
    }

    @Override
    public void addModuleData(PluginModule pluginModule, ModuleData moduleData) {
        this.modulesData.put(pluginModule, moduleData);
    }
}