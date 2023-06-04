package io.github.dakotaa.containerblocker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Listens for movement events into containers and cancels the event if ItemCheck.isBlocked() returns true for the event
 * item and inventory type/title.
 */
public class ContainerListeners implements Listener {
    /**
     * Listens for movement via hotbar number, drag + drop, and shift + click
     * @param event InventoryClickEvent
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clicked = event.getClickedInventory();
        if (event.getClick() == ClickType.NUMBER_KEY) { // keyboard number used to swap
            ItemStack moved = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());
            int size = event.getInventory().getSize(); // size of inventory, to check if slot that the item being moved to is in the inventory or not
            if (moved != null && moved.getType() != Material.AIR && event.getRawSlot() < size && ItemCheck.isBlocked((Player) event.getWhoClicked(), moved,
                    event.getWhoClicked().getOpenInventory().getType(), event.getWhoClicked().getOpenInventory().getTitle())) {
                event.setCancelled(true);
            }
        } else if (clicked != event.getWhoClicked().getInventory()) {  // drag + drop item into chest
            // The cursor item is going into the top inventory
            ItemStack onCursor = event.getCursor();
            if (onCursor != null && onCursor.getType() != Material.AIR && ItemCheck.isBlocked((Player) event.getWhoClicked(), onCursor,
                    event.getWhoClicked().getOpenInventory().getType(), event.getWhoClicked().getOpenInventory().getTitle())) {
                event.setCancelled(true);
            }
        } else if (event.getClick().isShiftClick()) { // shift + click item into chest
            if (clicked == event.getWhoClicked().getInventory()) {
                // The item is being shift clicked from the bottom to the top
                ItemStack clickedOn = event.getCurrentItem();
                if (clickedOn != null && clickedOn.getType() != Material.AIR && ItemCheck.isBlocked((Player) event.getWhoClicked(),clickedOn,
                        event.getWhoClicked().getOpenInventory().getType(), event.getWhoClicked().getOpenInventory().getTitle())) {
                    event.setCancelled(true);
                }
            }
        }
    }
    /**
     * Listens for movement via item drag
     * @param event InventoryDragEvent
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        ItemStack dragged = event.getOldCursor(); // This is the item that is being dragged

        if (dragged.getType() != Material.AIR && (ItemCheck.isBlocked((Player) event.getWhoClicked(), dragged,
                event.getWhoClicked().getOpenInventory().getType(), event.getWhoClicked().getOpenInventory().getTitle()))) {
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
    /**
     * Listens for items being moved into containers via hopper
     * @param event InventoryMoveItemEvent
     */
    @EventHandler
    public void onHopperTransfer(InventoryMoveItemEvent event) {
        if (!event.getSource().getType().equals(InventoryType.HOPPER)) return;
        ItemStack moved = event.getItem();
        if (moved.getType() != Material.AIR && ItemCheck.isBlocked(moved, "Hopper")) {
            event.setCancelled(true);
        }
    }
    /**
     * Listens for items being sucked up by hoppers
     * @param event InventoryPickupItemEvent
     */
    @EventHandler
    public void onHopperPickup(InventoryPickupItemEvent event) {
        if (!event.getInventory().getType().equals(InventoryType.HOPPER)) return;
        ItemStack moved = event.getItem().getItemStack();
        if (moved.getType() != Material.AIR && ItemCheck.isBlocked(moved, "Hopper")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemFrameDeposit(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof ItemFrame) {
            Player player = e.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR && ItemCheck.isBlocked(player, item)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onArmorStandDeposit(PlayerArmorStandManipulateEvent e) {
        ItemStack equipped = e.getPlayerItem();
        if (equipped.getType() != Material.AIR && ItemCheck.isBlocked(equipped, "ArmorStand")) {
            e.setCancelled(true);
        }
    }
}
