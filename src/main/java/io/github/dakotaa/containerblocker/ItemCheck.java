package io.github.dakotaa.containerblocker;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class ItemCheck {
    public static boolean isBlocked(ItemStack itemStack, InventoryType inventoryType) {
        if (itemStack.getType().equals(Material.ACACIA_DOOR)) return true;
        return false;
    }
}
