package io.github.dakotaa.containerblocker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemCheck {
    static BlockedGroup[] groups;
    /***
     * Reads the configured groups and builds BlockedGroup objects for each
     */
    public static void registerGroups(ContainerBlocker plugin) {
        //groups = new BlockedGroup[] {new BlockedGroup("global", new Material[]{Material.ACACIA_DOOR}, new String[]{"door"}, new String[]{"door"}, new String[]{"door"})};
        FileConfiguration config = plugin.getConfig();
        Set<String> groupNames = config.getConfigurationSection("blocked-groups").getKeys(false);
        int numGroups = groupNames.size();
        groups = new BlockedGroup[numGroups];
        int i = 0;
        for (String name : groupNames) {
            Bukkit.getLogger().info(name);
            // parse materials
            String[] materialStrings = config.getStringList("blocked-groups." + name + ".materials").toArray(new String[0]);
            Material[] materials = new Material[materialStrings.length];
            int j = 0;
            for (String s : materialStrings) {
                materials[j] = Material.getMaterial(s);
                j++;
            }
            String[] names = config.getStringList("blocked-groups." + name + ".names").toArray(new String[0]);
            String[] lores = config.getStringList("blocked-groups." + name + ".lore").toArray(new String[0]);
            String[] containerTitles = config.getStringList("blocked-groups." + name + ".container-title-whitelist").toArray(new String[0]);
            String message = config.getString("blocked-groups." + name + ".message");
            groups[i] = new BlockedGroup(name, materials, names, lores, containerTitles, message);
            i++;
        }

    }

    /**
     * Checks if the given itemstack is blocked from the specified inventory type and title
     * @param itemStack the itemstack being moved into the container
     * @param inventoryType the container type
     * @param inventoryName the name of the container
     * @return true if the itemstack is blocked from being moved into the container, false otherwise
     */
    public static boolean isBlocked(ItemStack itemStack, InventoryType inventoryType, String inventoryName) {
        boolean blocked = false;
        for (BlockedGroup g : groups) {
            blocked = g.checkMaterial(itemStack) || g.checkName(itemStack) || g.checkLore(itemStack);
            if (blocked) { // if item is blocked, make final check for whether the inventory name is whitelisted
                return g.checkContainerTitle(inventoryName);
            }
        }
        return false;
    }
    public static boolean isBlocked(Player player, ItemStack itemStack, InventoryType inventoryType, String inventoryName) {
        boolean blocked = false;
        if (inventoryType.equals(InventoryType.CRAFTING)) return false; // never block movement within the inventory/armour/crafting slots
        for (BlockedGroup g : groups) {
            blocked = g.checkMaterial(itemStack) || g.checkName(itemStack) || g.checkLore(itemStack);
            if (blocked) { // if item is blocked, make final check for whether the inventory name is whitelisted
                if (g.checkContainerTitle(inventoryName)) {
                    player.sendMessage(g.getMessage());
                    return true;
                }
            }
        }
        return false;
    }
}
