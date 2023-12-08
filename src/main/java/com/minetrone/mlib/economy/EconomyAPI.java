package com.minetrone.mlib.economy;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * EconomyAPI
 *
 * @author amownyy
 * @since 1.0.0
 */
@Getter
public class EconomyAPI {

    public static JavaPlugin instance;

    private Economy economy;

    public EconomyAPI(JavaPlugin instance, String economy) {
        EconomyAPI.instance = instance;
        this.economy = EconomyType.getEconomy(EconomyType.getEconomyType(economy));
    }
}