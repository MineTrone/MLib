package com.minetrone.mlib.economy;

import com.minetrone.mlib.economy.providers.*;
import org.bukkit.Bukkit;

/**
 * Type of economy
 * @author amownyy
 * @since 1.0.0
 */
public enum EconomyType {
    ElementalGems,
    GrinGotts,
    PlayerPoints,
    RoyaleEconomy,
    Vault;

    /**
     * Gets type of economy by string
     * @param type
     * @return
     */
    public static EconomyType getEconomyType(String type) {
        if (type.equalsIgnoreCase("vault")
                || Bukkit.getPluginManager().isPluginEnabled("Vault")
                && type.equalsIgnoreCase("auto"))
            return EconomyType.Vault;
        else if (type.equalsIgnoreCase("royaleeconomy")
                || Bukkit.getPluginManager().isPluginEnabled("RoyaleEconomy")
                && type.equalsIgnoreCase("auto"))
            return EconomyType.RoyaleEconomy;
        else if (type.equalsIgnoreCase("playerpoints")
                || Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")
                && type.equalsIgnoreCase("auto"))
            return EconomyType.PlayerPoints;
        else if (type.equalsIgnoreCase("gringotts")
                || Bukkit.getPluginManager().isPluginEnabled("GrinGotts")
                && type.equalsIgnoreCase("auto"))
            return EconomyType.GrinGotts;
        else if(type.equalsIgnoreCase("elementalgems")
                || Bukkit.getPluginManager().isPluginEnabled("ElementalGems")
                && type.equalsIgnoreCase("auto"))
            return EconomyType.ElementalGems;
        else return EconomyType.Vault;
    }

    /**
     * Gets economy class
     * @param type of economy
     * @return Economy class
     */
    public static Economy getEconomy(EconomyType type) {
        switch (type) {
            case RoyaleEconomy:
                return new RoyaleEconomy();
            case PlayerPoints:
                return new PlayerPoints();
            case GrinGotts:
                return new GrinGotts();
            case ElementalGems:
                return new ElementalGems();
            default:
                return new Vault();
        }
    }
}