package com.minetrone.mlib.economy.providers;

import com.minetrone.mlib.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * PlayerPoints economy integration class
 *
 * @author amownyy
 * @since v1.0.0
 */
public class PlayerPoints implements Economy {

    private PlayerPointsAPI economy = null;

    /**
     * Constructor register event of super class
     */
    public PlayerPoints() {
        setupEconomy();
    }

    private boolean setupEconomy() {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlayerPoints"))
            return false;
        this.economy = org.black_ixx.playerpoints.PlayerPoints.getInstance().getAPI();
        return (this.economy != null);
    }

    /**
     * Withdraw player balance
     * @param player
     * @param price
     * @return price
     */
    @Override
    public double withdrawPlayer(OfflinePlayer player, double price) {
        if (this.economy != null) {
            this.economy.take(player.getUniqueId(), (int) price);
        }
        return price;
    }

    /**
     * Deposit player balance
     * @param player
     * @param price
     * @return price
     */
    @Override
    public double depositPlayer(OfflinePlayer player, double price) {
        if (this.economy != null) {
            this.economy.give(player.getUniqueId(), (int) price);
        }
        return price;
    }

    /**
     * Get player balance
     * @param player
     * @return player balance
     */
    public double getBalance(OfflinePlayer player) {
        if (this.economy != null) {
            return this.economy.look(player.getUniqueId());
        }
        return 0;
    }
}