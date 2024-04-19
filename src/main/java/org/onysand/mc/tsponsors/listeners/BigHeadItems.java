package org.onysand.mc.tsponsors.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.onysand.mc.tsponsors.TSponsors;

import java.util.List;


public class BigHeadItems implements Listener {

    @EventHandler
    public void onItemClickEvent(InventoryClickEvent event) {
        ItemStack item = event.getWhoClicked().getInventory().getItemInOffHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        if (meta.getPersistentDataContainer().has(TSponsors.bigHeadKey)) {
            event.setCancelled(true);
            item.setAmount(0);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        List<ItemStack> toRemove = event.getDrops().stream().filter(this::checkItem).toList();
        event.getDrops().removeAll(toRemove);
    }

    @EventHandler
    public void onItemSwapEvent(PlayerSwapHandItemsEvent event) {
        if (checkItem(event.getMainHandItem())) event.setCancelled(true);
        if (checkItem(event.getOffHandItem())) event.setCancelled(true);
    }

    @EventHandler
    public void onItemPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
        if (!checkItem(event.getItemInHand())) event.setCancelled(false);
    }

    private boolean checkItem(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        return meta.getPersistentDataContainer().has(TSponsors.bigHeadKey);
    }
}
