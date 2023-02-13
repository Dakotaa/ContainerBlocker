package io.github.dakotaa.containerblocker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class BlockedGroup {
    private String id;
    private Material[] materials;
    private String[] names;
    private String[] loreElements;
    private String[] containerTitles;
    private String message;
    static Pattern pattern = Pattern.compile(ChatColor.translateAlternateColorCodes('&', "&8Go{2,4}d Inventory"), Pattern.CASE_INSENSITIVE);
    public BlockedGroup(String id, Material[] materials, String[] names, String[] loreElements, String[] containerTitles, String message) {
        this.id = id;
        this.materials = materials;
        for (int i = 0; i < names.length; i++) {
            names[i] = ChatColor.translateAlternateColorCodes('&', names[i]);
        }
        this.names = names;
        for (int i = 0; i < loreElements.length; i++) {
            loreElements[i] = ChatColor.translateAlternateColorCodes('&', loreElements[i]);
        }
        this.loreElements = loreElements;
        this.containerTitles = containerTitles;
        this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * given an itemstack, returns whether its item material is blocked in this group
     * @param itemStack the item to check
     * @return true if the given material type is blocked in this group
     */
    public boolean checkMaterial(ItemStack itemStack) {
        return Arrays.asList(materials).contains(itemStack.getType());
    }

    /**
     * given an itemstack, returns whether the name of the itemstack is blocked in this group
     * @param itemStack the item to check
     * @return true if the given item name is blocked in this group
     */
    public boolean checkName(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta != null && Arrays.asList(names).contains(itemMeta.getDisplayName());
    }

    /**
     * given an itemstack, returns whether the lore of the itemstack contains a line that is blocked in this group
     * @param itemStack the item to check
     * @return true if the given item contains a lore line that is blocked in this group
     */
    public boolean checkLore(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || itemMeta.getLore() == null) return false;
        for (String line : itemMeta.getLore()) {
            if (Arrays.asList(loreElements).contains(line)) return true;
        }
        return false;
    }

    public boolean checkContainerTitle(String containerTitle) {
        return !Arrays.asList(containerTitles).contains(containerTitle);
        // TODO: use regex
    }
//
    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    //    public Material[] getMaterials() {
//        return materials;
//    }
//
//    public String[] getNames() {
//        return names;
//    }
//
//    public String[] getLoreElements() {
//        return loreElements;
//    }
}
