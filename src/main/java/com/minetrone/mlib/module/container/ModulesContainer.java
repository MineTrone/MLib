package com.minetrone.mlib.module.container;

import com.minetrone.mlib.module.PluginModule;
import com.minetrone.mlib.module.helper.ModuleData;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;

public interface ModulesContainer {

    void registerModule(PluginModule pluginModule, File modulesFolder, File modulesDataFolder);

    void unregisterModule(PluginModule pluginModule);

    @Nullable
    PluginModule getModule(String name);

    Collection<PluginModule> getModules();

    void addModuleData(PluginModule pluginModule, ModuleData moduleData);

}