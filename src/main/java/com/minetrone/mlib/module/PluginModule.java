package com.minetrone.mlib.module;

import com.minetrone.mlib.module.helper.ModuleLoadTime;
import com.minetrone.mlib.module.helper.ModuleLogger;
import com.minetrone.mlib.module.helper.ModuleResources;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Plugin modules are similar to plugins. The difference between them and regular plugins is that the modules are
 * designed to work only with Used Plugins, and they are loaded by MNavigation and not by the server.
 * The advantages of using modules over plugins is the ability to manage them through the plugin's commands, without
 * needing to restart the server. Moreover, Used Plugins handles listeners and commands for you - it will register
 * them when needed, unregister them and will do all of that work for you.
 */
public abstract class PluginModule {

    private final String moduleName;
    private final String authorName;

    private File dataFolder;
    private File moduleFile;
    private File moduleFolder;
    private ClassLoader classLoader;
    private Logger logger;
    private ModuleResources moduleResources;

    private boolean initialized = false;

    /**
     * Constructor for a module.
     *
     * @param moduleName The name of the module.
     * @param authorName The name of the author of the module.
     */
    protected PluginModule(String moduleName, String authorName) {
        this.moduleName = moduleName;
        this.authorName = authorName;
    }

    /**
     * Called when the module is enabled.
     *
     * @param plugin Instance of the plugin.
     */
    public abstract void onEnable(JavaPlugin plugin);

    /**
     * Called when the module is reloaded.
     *
     * @param plugin Instance of the plugin.
     */
    public abstract void onReload(JavaPlugin plugin);

    /**
     * Called when the module is disabled.
     *
     * @param plugin Instance of the plugin.
     */
    public abstract void onDisable(JavaPlugin plugin);

    /**
     * Called when the module can load data about players.
     * It's called after the plugin's data is loaded.
     * <p>
     *
     * @param plugin Instance of the plugin.
     */
    public void loadData(JavaPlugin plugin) {}

    /**
     * Called when the module initialized for the first time.
     *
     * @param plugin Instance of the plugin.
     */
    protected void onPluginInit(JavaPlugin plugin) {
        // Can be overridden by custom modules.
    }

    /**
     * List of listeners to register for the module.
     * The plugin will handle the registers for the module - register them when the module is enabled,
     * and unregister them when it is disabled.
     *
     * @param plugin Instance of the plugin.
     * @return Array of listeners for the module. May be null for no listeners.
     */
    @Nullable
    public abstract Listener[] getModuleListeners(JavaPlugin plugin);

    /**
     * Get when the module should be enabled.
     * There are 3 loading stages for modules:
     * {@link ModuleLoadTime#AFTER_HANDLERS_LOADING} - modules that should be loaded before the worlds are created.
     * Should be used if the module needs to override the WorldsProvider.
     * {@link ModuleLoadTime#NORMAL} - modules that should be loaded without any specifications.
     * Default for all the modules.
     * {@link ModuleLoadTime#AFTER_HANDLERS_LOADING} - modules that should be loaded after all the plugin handlers.
     * Should be used if the module is interacting with the built-in handlers on its {@link #onEnable} method.
     */
    public ModuleLoadTime getLoadTime() {
        return ModuleLoadTime.NORMAL;
    }

    /**
     * Get the name of the module.
     */
    public final String getName() {
        return moduleName;
    }

    /**
     * Get the author of the module.
     */
    public final String getAuthor() {
        return authorName;
    }

    /**
     * Get the data folder of the module.
     * The path for the folder is always plugins/MNavigation/modules/{module-name}/
     *
     * @deprecated Misleading name; check out {@link #getModuleFolder()}
     */
    @Deprecated
    public final File getDataFolder() {
        return this.getModuleFolder();
    }

    /**
     * Get the folder of the module.
     * The path for the folder is always plugins/MNavigation/modules/{module-name}/
     */
    public final File getModuleFolder() {
        return this.moduleFolder;
    }

    /**
     * Get the jar file of the module.
     * May be null if the module was registered without calling {@link #initModuleLoader(File, ClassLoader)}
     * This is not an expected behavior, and the plugin will never initialize the module with a null file!
     */
    @Nullable
    public final File getModuleFile() {
        return moduleFile;
    }

    /**
     * Get the folder where data of the module can be stored at.
     * The path for the folder is always plugins/MNavigation/datastore/modules/{module-name}/
     */
    public final File getDataStoreFolder() {
        return this.dataFolder;
    }

    /**
     * Get the class loader of the module.
     * May be null if the module was registered without calling {@link #initModuleLoader(File, ClassLoader)}
     * This is not an expected behavior, and the plugin will never initialize the module with a null class loader!
     */
    @Nullable
    public final ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Get the logger of the module {@link ModuleLogger}
     */
    public final Logger getLogger() {
        return logger;
    }

    /**
     * Check whether the module was initialized or not.
     * Modules will be initialized after calling to {@link #initModule(JavaPlugin, File, File)}
     */
    public final boolean isInitialized() {
        return initialized;
    }

    /**
     * Saves the raw contents of an embedded resource within the module.
     *
     * @param resourceName The name of the resource to save.
     */
    public final void saveResource(String resourceName) {
        if (this.moduleResources == null)
            throw new IllegalArgumentException("Cannot save resources for an uninitialized module.");

        moduleResources.saveResource(resourceName);
    }

    /**
     * Get the raw contents of an embedded resource within the module.
     *
     * @param resourceName The name of the resource to get contents of.
     */
    public final InputStream getResource(String resourceName) {
        if (this.moduleResources == null)
            throw new IllegalArgumentException("Cannot get resources for an uninitialized module.");

        return moduleResources.getResource(resourceName);
    }

    /**
     * Initialize the module.
     * This method cannot be called twice - do not call it unless you know what you are doing.
     *
     * @param plugin     An instance to the plugin.
     * @param dataFolder The database folder of the module.
     */
    @Deprecated
    public final void initModule(JavaPlugin plugin, File dataFolder) {
        this.initModule(plugin, new File(moduleFile.getParentFile(), moduleName), dataFolder);
    }

    /**
     * Initialize the module.
     * This method cannot be called twice - do not call it unless you know what you are doing.
     *
     * @param plugin       An instance to the plugin.
     * @param dataFolder   The database folder of the module.
     * @param moduleFolder The folder of the module.
     */
    public final void initModule(JavaPlugin plugin, File moduleFolder, File dataFolder) {
        if (initialized)
            throw new RuntimeException("The module " + moduleName + " was already initialized.");

        initialized = true;

        this.dataFolder = dataFolder;
        this.moduleFolder = moduleFolder;

        if (!moduleFolder.exists() && !moduleFolder.mkdirs())
            throw new RuntimeException("Cannot create module folder for " + moduleName + ".");

        this.logger = new ModuleLogger(this);

        if (moduleFile != null && classLoader != null)
            this.moduleResources = new ModuleResources(this.moduleFile, this.moduleFolder, this.classLoader);

        onPluginInit(plugin);
    }

    /**
     * Initialize the module's loaders settings.
     *
     * @param moduleFile  The file of the module jar.
     * @param classLoader The class loader used to load the module.
     */
    public final void initModuleLoader(File moduleFile, ClassLoader classLoader) {
        if (initialized)
            throw new RuntimeException("The module " + moduleName + " was already initialized.");

        this.moduleFile = moduleFile;
        this.classLoader = classLoader;
    }

    /**
     * Disable the module.
     */
    public final void disableModule() {
        initialized = false;
    }

}