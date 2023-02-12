package io.github.dakotaa.containerblocker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemCheck {
    static BlockedGroup[] groups;
    /***
     * Reads the configured groups and builds BlockedGroup objects for each
     */
    public static void registerGroups() {
        groups = new BlockedGroup[] {new BlockedGroup("global", new Material[]{Material.ACACIA_DOOR}, new String[]{"door"}, new String[]{"door"}, new String[]{"door"})};
        // TODO: read group elements from config
        // TODO: add groups to list
    }
    public static boolean isBlocked(ItemStack itemStack, InventoryType inventoryType, String inventoryName) {
        boolean blocked = false;
        for (BlockedGroup g : groups) {
            blocked = g.checkMaterial(itemStack) || g.checkName(itemStack) || g.checkLore(itemStack);
            if (blocked) { // if item is blocked, make final check for whether the inventory name is whitelisted
                return g.checkContainerTitle(inventoryName);
            }
//            Matcher matcher = pattern.matcher(inventoryName);
//            if (matcher.find()) return false; // if container name matches whitelisted containers, don't block
//            if (itemStack.getType().equals(Material.ACACIA_DOOR)) return true;
//            return false;
        }
        return false;
    }
}
