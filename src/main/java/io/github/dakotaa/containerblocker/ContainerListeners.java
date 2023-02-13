package io.github.dakotaa.containerblocker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ContainerListeners implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clicked = event.getClickedInventory();
        if (event.getClick() == ClickType.NUMBER_KEY) { // keyboard number used to swap
            ItemStack moved = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());
            int size = event.getInventory().getSize(); // size of inventory, to check if slot that the item being moved to is in the inventory or not
            if (event.getRawSlot() < size && ItemCheck.isBlocked((Player) event.getWhoClicked(), moved, event.getWhoClicked().getOpenInventory().getType(), event.getWhoClicked().getOpenInventory().getTitle())) {
                event.setCancelled(true);
            }
        } else if (clicked != event.getWhoClicked().getInventory()) {  // drag + drop item into chest
            // The cursor item is going into the top inventory
            ItemStack onCursor = event.getCursor();
            if (onCursor != null && ItemCheck.isBlocked((Player) event.getWhoClicked(), onCursor, event.getWhoClicked().getOpenInventory().getType(), event.getWhoClicked().getOpenInventory().getTitle())) {
                event.setCancelled(true);
            }
        } else if (event.getClick().isShiftClick()) { // shift + click item into chest
            if (clicked == event.getWhoClicked().getInventory()) {
                // The item is being shift clicked from the bottom to the top
                ItemStack clickedOn = event.getCurrentItem();
                if (clickedOn != null && ItemCheck.isBlocked((Player) event.getWhoClicked(),clickedOn, event.getWhoClicked().getOpenInventory().getType(), event.getWhoClicked().getOpenInventory().getTitle())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        ItemStack dragged = event.getOldCursor(); // This is the item that is being dragged

        if (dragged.getType() != Material.AIR && (ItemCheck.isBlocked((Player) event.getWhoClicked(), dragged, event.getWhoClicked().getOpenInventory().getType(), event.getWhoClicked().getOpenInventory().getTitle()))) {
            int inventorySize = event.getInventory().getSize(); // The size of the inventory, for reference

            // Now we go through all of the slots and check if the slot is inside our inventory (using the inventory size as reference)
            for (int i : event.getRawSlots()) {
                if (i < inventorySize) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onHopperTransfer(InventoryMoveItemEvent event) {
        if (!event.getSource().getType().equals(InventoryType.HOPPER)) return;
        ItemStack moved = event.getItem();
        if (moved.getType() != Material.AIR && ItemCheck.isBlocked(moved, event.getDestination().getType(), "Hopper")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHopperPickup(InventoryPickupItemEvent event) {
        if (!event.getInventory().getType().equals(InventoryType.HOPPER)) return;
        ItemStack moved = event.getItem().getItemStack();
        if (moved.getType() != Material.AIR && ItemCheck.isBlocked(moved, event.getInventory().getType(), "Hopper")) {
            event.setCancelled(true);
        }
    }
}
