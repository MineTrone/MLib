package com.minetrone.mlib.economy.providers;

import com.minetrone.mlib.economy.Economy;
import com.minetrone.mlib.economy.EconomyAPI;
import me.qKing12.RoyaleEconomy.API.APIHandler;
import org.bukkit.OfflinePlayer;

/**
 * RoyaleEconomy economy integration class
 *
 * @author amownyy
 * @since v1.0.0
 */
public class RoyaleEconomy implements Economy {

    private APIHandler economy = null;

    /**
     * Constructor register event of super class
     */
    public RoyaleEconomy() {
        setupEconomy();
    }

    private boolean setupEconomy() {
        if (EconomyAPI.instance.getServer().getPluginManager().getPlugin("RoyaleEconomy") == null) {
            return false;
        }
        economy = me.qKing12.RoyaleEconomy.RoyaleEconomy.apiHandler;
        return economy != null;
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
            this.economy.balance.removeBankBalance(player.getUniqueId() + "", price);
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
            this.economy.balance.addBankBalance(player.getUniqueId() + "", price);
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
            return this.economy.balance.getBankBalance(player.getUniqueId() + "");
        }
        return 0;
    }
}