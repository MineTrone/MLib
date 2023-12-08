package com.minetrone.mlib.economy.providers;

import com.minetrone.mlib.economy.EconomyAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import net.milkbowl.vault.economy.Economy;

/**
 * Vault economy integration class
 *
 * @author amownyy
 * @since v1.0.0
 */
public class Vault implements com.minetrone.mlib.economy.Economy {

    private Economy economy = null;

    /**
     * Constructor register event of super class
     */
    public Vault() {
        setupEconomy();
    }

    private void setupEconomy() {
        if (EconomyAPI.instance.getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = EconomyAPI.instance.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
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
            this.economy.withdrawPlayer(player, price);
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
            this.economy.depositPlayer(player, price);
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
            return this.economy.getBalance(player);
        }
        return 0;
    }
}