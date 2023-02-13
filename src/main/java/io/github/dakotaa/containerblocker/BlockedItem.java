package io.github.dakotaa.containerblocker;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class BlockedItem {
    Material material;
    String name;
    String[] lore;

    public BlockedItem(Material material, String name, String[] lore) {
        this.material = material;
        this.name = name;
        this.lore = lore;
    }

    public boolean isBlocked(ItemStack itemStack) {
        if (!itemStack.getType().equals(this.material)) return false; // not blocked if not the same material type
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return false; // not blocked if no meta on item
        if (this.name != null && !meta.getDisplayName().equals(this.name)) return false; // not blocked if name is set and doesn't match
        if (meta.getLore() == null) return false;
        if (this.lore != null && !meta.getLore().equals(Arrays.asList(this.lore))) return false;
        return true;
    }
}
