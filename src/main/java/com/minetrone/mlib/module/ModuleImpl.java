package com.minetrone.mlib.module;

import com.google.common.base.Preconditions;
import com.minetrone.mlib.MLib;
import com.minetrone.mlib.module.container.ModulesContainer;
import com.minetrone.mlib.module.extras.Either;
import com.minetrone.mlib.module.extras.FileClassLoader;
import com.minetrone.mlib.module.extras.JarFiles;
import com.minetrone.mlib.module.helper.Manager;
import com.minetrone.mlib.module.helper.ModuleData;
import com.minetrone.mlib.module.helper.ModuleLoadTime;
import com.minetrone.mlib.module.helper.ModuleManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class ModuleImpl extends Manager implements ModuleManager {

    private final ModulesContainer modulesContainer;
    private final File modulesFolder;
    private final File dataFolder;

    public ModuleImpl(JavaPlugin plugin, ModulesContainer modulesContainer) {
        super(plugin);
        this.modulesContainer = modulesContainer;
        this.modulesFolder = new File(plugin.getDataFolder(), "modules");
        this.dataFolder = new File(plugin.getDataFolder(), "datastore/modules");
    }

    @Override
    public void loadData() {
        if (!modulesFolder.exists())
            //noinspection ResultOfMethodCallIgnored
            modulesFolder.mkdirs();

        registerExternalModules();
    }

    @Override
    public void registerModule(PluginModule pluginModule) {
        Preconditions.checkNotNull(pluginModule, "pluginModule parameter cannot be null.");
        this.modulesContainer.registerModule(pluginModule, modulesFolder, dataFolder);
    }

    @Override
    public PluginModule registerModule(File moduleFile) throws IOException, ReflectiveOperationException {
        Preconditions.checkArgument(moduleFile.exists(), "The file " + moduleFile.getName() + " does not exist.");
        Preconditions.checkArgument(moduleFile.getName().endsWith(".jar"), "The file " + moduleFile.getName() + " is not a valid jar file.");

        FileClassLoader moduleClassLoader = new FileClassLoader(moduleFile, MLib.getPluginClassLoader());

        Either<Class<?>, Throwable> moduleClassLookup = JarFiles.getClass(moduleFile.toURL(), PluginModule.class, moduleClassLoader);

        if (moduleClassLookup.getLeft() != null)
            throw new RuntimeException("An error occurred while reading " + moduleFile.getName(), moduleClassLookup.getLeft());

        Class<?> moduleClass = moduleClassLookup.getRight();

        if (moduleClass == null)
            throw new RuntimeException("The module file " + moduleFile.getName() + " is not valid.");

        PluginModule pluginModule = createInstance(moduleClass);
        pluginModule.initModuleLoader(moduleFile, moduleClassLoader);

        registerModule(pluginModule);

        return pluginModule;
    }

    @Override
    public void unregisterModule(PluginModule pluginModule) {
        Preconditions.checkNotNull(pluginModule, "pluginModule parameter cannot be null.");
        Preconditions.checkState(getModule(pluginModule.getName()) != null, "PluginModule with the name " + pluginModule.getName() + " is not registered in the plugin anymore.");

        MLib.getInstance().getLogger().severe("Disabling the module " + pluginModule.getName() + "...");

        try {
            pluginModule.onDisable(plugin);
        } catch (Throwable err) {
            MLib.getInstance().getLogger().severe("An error occurred while disabling the module " + pluginModule.getName() + ".");
            MLib.getInstance().getLogger().severe("Error: " + err.getMessage());
        }

        this.modulesContainer.unregisterModule(pluginModule);

        // We now want to unload the ClassLoader and free the held handles for the file.
        ClassLoader classLoader = pluginModule.getClassLoader();
        if (classLoader instanceof URLClassLoader) {
            try {
                ((URLClassLoader) classLoader).close();
                // This is an attempt to force Windows to free the handles of the file.
                System.gc();
            } catch (IOException ignored) {}
        }
    }

    @Override
    public @Nullable PluginModule getModule(String name) {
        Preconditions.checkNotNull(name, "name parameter cannot be null.");
        return this.modulesContainer.getModule(name);
    }

    @Override
    public Collection<PluginModule> getModules() {
        return this.modulesContainer.getModules();
    }

    @Override
    public void enableModule(PluginModule pluginModule) {
        Preconditions.checkNotNull(pluginModule, "pluginModule parameter cannot be null.");

        long startTime = System.currentTimeMillis();

        MLib.getInstance().getLogger().severe("Enabling the module " + pluginModule.getName() + "...");

        try {
            pluginModule.onEnable(plugin);
        } catch (Exception err) {
            MLib.getInstance().getLogger().severe("An error occurred while enabling the module " + pluginModule.getName() + ".");
            MLib.getInstance().getLogger().severe("Error: " + err.getMessage());

            try {
                unregisterModule(pluginModule);
            } catch (Throwable err2) {
                MLib.getInstance().getLogger().severe("An error occurred while disabling the module " + pluginModule.getName() + ".");
                MLib.getInstance().getLogger().severe("Error: " + err2.getMessage());
            }
            return;
        }

        Listener[] listeners = pluginModule.getModuleListeners(plugin);

        if  (listeners != null) {
            this.modulesContainer.addModuleData(pluginModule, new ModuleData(listeners));
            Arrays.stream(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, plugin));
        }

        MLib.getInstance().getLogger().severe("The module " + pluginModule.getName() + " has been enabled in " + (System.currentTimeMillis() - startTime) + "ms.");
    }

    @Override
    public void enableModules(ModuleLoadTime moduleLoadTime) {
        Preconditions.checkNotNull(moduleLoadTime, "moduleLoadTime parameter cannot be null.");
        filterModules(moduleLoadTime).forEach(this::enableModule);
    }

    public void reloadModules(ModuleLoadTime moduleLoadTime) {
        Preconditions.checkNotNull(moduleLoadTime, "moduleLoadTime parameter cannot be null.");
        filterModules(moduleLoadTime).forEach(this::reloadModuleInternal);
    }

    public void loadModulesData(JavaPlugin plugin) {
        getModules().forEach(pluginModule -> {
            try {
                pluginModule.loadData(plugin);
            } catch (Throwable error) {
                MLib.getInstance().getLogger().severe("An error occurred while loading the data of the module " + pluginModule.getName() + ".");
                MLib.getInstance().getLogger().severe("Error: &e" + error.getMessage());
            }
        });
    }

    private void reloadModuleInternal(PluginModule pluginModule) {
        try {
            pluginModule.onReload(plugin);
        } catch (Throwable error) {
            MLib.getInstance().getLogger().severe("An error occurred while reloading the module " + pluginModule.getName() + ".");
            MLib.getInstance().getLogger().severe("Error: " + error.getMessage());
        }
    }

    private void registerExternalModules() {
        File[] folderFiles = modulesFolder.listFiles();

        if (folderFiles != null) {
            for (File file : folderFiles) {
                if (!file.isDirectory() && file.getName().endsWith(".jar")) {
                    try {
                        registerModule(file);
                    } catch (Exception error) {
                        MLib.getInstance().getLogger().severe("An error occurred while registering the module " + file.getName() + ".");
                        MLib.getInstance().getLogger().severe("Error: " + error.getMessage());
                    }
                }
            }
        }
    }

    private Stream<PluginModule> filterModules(ModuleLoadTime moduleLoadTime) {
        return this.modulesContainer.getModules().stream()
                .filter(pluginModule -> pluginModule.getLoadTime() == moduleLoadTime);
    }

    private PluginModule createInstance(Class<?> clazz) throws ReflectiveOperationException {
        Preconditions.checkArgument(PluginModule.class.isAssignableFrom(clazz), "Class " + clazz + " is not a PluginModule.");

        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                if (!constructor.isAccessible())
                    constructor.setAccessible(true);

                return (PluginModule) constructor.newInstance();
            }
        }

        throw new IllegalArgumentException("Class " + clazz + " has no valid constructors.");
    }
}