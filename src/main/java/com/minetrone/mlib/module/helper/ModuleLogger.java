package com.minetrone.mlib.module.helper;

import com.minetrone.mlib.MLib;
import com.minetrone.mlib.module.PluginModule;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple implementation of a custom logger for modules.
 */
public class ModuleLogger extends Logger {

    /**
     * Constructor for the logger.
     *
     * @param pluginModule The module that uses the logger.
     */
    public ModuleLogger(PluginModule pluginModule) {
        super(MLib.getPrefix() + "-" + pluginModule.getName(), null);
        this.setParent(MLib.getInstance().getLogger());
        this.setLevel(Level.ALL);
    }

}