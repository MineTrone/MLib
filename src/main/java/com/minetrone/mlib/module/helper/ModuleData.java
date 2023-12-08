package com.minetrone.mlib.module.helper;

import lombok.Getter;
import org.bukkit.event.Listener;

@Getter
public class ModuleData {

    private final Listener[] listeners;

    public ModuleData(Listener[] listeners) {
        this.listeners = listeners;
    }

}