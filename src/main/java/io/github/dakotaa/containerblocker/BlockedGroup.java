package io.github.dakotaa.containerblocker;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.in;

/**
 * BlockedGroup represents a collection of materials, item names, and item lore lines that should be blocked.
 * Certain container titles can be whitelisted, allowing an item that would be otherwise blocked.
 * The message is displayed to the user when they attempt to move a blocked item.
 */
public class BlockedGroup {
    private final String id;
    private final Material[] materials;
    private final Pattern[] namePatterns;
    private final Pattern[] lorePatterns;
    private final HashMap<Enchantment, Integer> enchantments;
    private final String[] nbtTags;
    private final Pattern[] containerTitlePatterns;
    private final String message;
    private final boolean containerTitlePatternsIsWhitelist;
    static Pattern pattern = Pattern.compile(ChatColor.translateAlternateColorCodes('&', "&8Go{2,4}d Inventory"), Pattern.CASE_INSENSITIVE);
    public BlockedGroup(String id, Material[] materials, String[] names, String[] loreElements, String[] enchantments, String[] nbtTags, String[] containerTitles, boolean containerTitlePatternsIsWhitelist, String message) {
        this.id = id;
        this.materials = materials;
        // compile regex patterns for blocked names
        this.namePatterns = new Pattern[names.length];
        for (int i = 0; i < names.length; i++) {
            this.namePatterns[i] = Pattern.compile(ChatColor.translateAlternateColorCodes('&', names[i]), Pattern.CASE_INSENSITIVE);
        }
        // compile regex patterns for blocked lore elements
        this.lorePatterns = new Pattern[loreElements.length];
        for (int i = 0; i < loreElements.length; i++) {
            this.lorePatterns[i] = Pattern.compile(ChatColor.translateAlternateColorCodes('&', loreElements[i]), Pattern.CASE_INSENSITIVE);
        }
        this.enchantments = parseEnchantments(enchantments);
        this.nbtTags = nbtTags;
        // compile regex patterns for container titles
        this.containerTitlePatterns = new Pattern[containerTitles.length];
        for (int i = 0; i < containerTitles.length; i++) {
            this.containerTitlePatterns[i] = Pattern.compile(ChatColor.translateAlternateColorCodes('&', containerTitles[i]), Pattern.CASE_INSENSITIVE);
        }
        this.containerTitlePatternsIsWhitelist = containerTitlePatternsIsWhitelist;
        this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * from the configured list of enchantment IDs and levels, builds the map of enchantments and levels
     * @param enchantments a list of enchantments in the form "enchantment:level"
     * @return the map of enchantment objects with levels
     */
    private HashMap<Enchantment, Integer> parseEnchantments(String[] enchantments) {
        HashMap<Enchantment, Integer> map = new HashMap<>();
        for (String enchant : enchantments) {
            Enchantment enchantName = Enchantment.getByKey(NamespacedKey.minecraft(enchant.split(":")[0]));
            Integer enchantLevel = Integer.valueOf(enchant.split(":")[1]);
            map.put(enchantName, enchantLevel);
        }
        return map;
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
        if (itemMeta == null) return false;
        String name = itemMeta.getDisplayName();
        for (Pattern p : this.namePatterns) {
            Matcher matcher = p.matcher(name);
            if (matcher.find()) return true;
        }
        return false;
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
            for (Pattern p : this.lorePatterns) {
                Matcher matcher = p.matcher(line);
                if (matcher.find()) return true;
            }
        }
        return false;
    }

    public boolean checkEnchantments(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        Map<Enchantment, Integer> enchants;
        if (itemStack.getType().equals(Material.ENCHANTED_BOOK)) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
            enchants = meta.getStoredEnchants();
        } else {
            enchants = itemMeta.getEnchants();
        }
        for (Enchantment e : enchants.keySet()) {
            Integer level = this.enchantments.get(e);
            if (level != null) {
                if (enchants.get(e) >= level) return true;
            }
        }
        return false;
    }

    public boolean checkNBT(ItemStack itemStack) {
        NBTItem nbt = new NBTItem(itemStack);
        List<String> blockedTags = Arrays.asList(nbtTags);
        for (String tag : nbt.getKeys()) {
            if (blockedTags.contains(tag)) return true;
        }
        return false;
    }

    public boolean isWhitelistedContainer(String containerTitle) {
        for (Pattern p : this.containerTitlePatterns) {
            Matcher matcher = p.matcher(containerTitle);
            if (matcher.find()) return true;
        }
        return false;
    }

    public boolean isContainerTitlePatternsWhitelist() {
        return this.containerTitlePatternsIsWhitelist;
    }

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
