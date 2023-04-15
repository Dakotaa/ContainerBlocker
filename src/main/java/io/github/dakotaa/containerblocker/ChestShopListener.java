package io.github.dakotaa.containerblocker;

import com.Acrobot.ChestShop.Events.ItemParseEvent;
import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import com.Acrobot.ChestShop.Signs.ChestShopSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ChestShopListener implements Listener {
    @EventHandler
    public void onChestShopTransaction(PreTransactionEvent event) {
        ItemStack item = event.getStock()[0];
        Player player = event.getClient();
        item.setAmount(1);
        if (ItemCheck.isBlocked(item)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ContainerBlocker.plugin.getConfig().getString("messages.deny-shop", "&cShop transaction blocked.")));
        }
    }
}
