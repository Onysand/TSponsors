package org.onysand.mc.tsponsors.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.onysand.mc.tsponsors.TSponsors;
import org.onysand.mc.tsponsors.utils.Utils;

public class RegionsPlaytime implements Listener {
    RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.getPlayer().hasPermission("tsponsors.baypass.regionsprotecttime")) return;

        if (Utils.getMinutesPlayedAsync(player) > TSponsors.getPlugin().getConfig().getInt("regionsPlayTime")) return;

        World world = BukkitAdapter.adapt(event.getBlock().getWorld());
        RegionManager manager = regionContainer.get(world);
        Block block = event.getBlock();

        if (manager == null) return;
        ApplicableRegionSet regions = manager.getApplicableRegions(BlockVector3.at(block.getX(), block.getY(), block.getZ()));

        if (regions.getRegions().stream().map(ProtectedRegion::getId).noneMatch(it -> it.equalsIgnoreCase("spawn")
                || it.equalsIgnoreCase("nether_spawn"))) return;
        long timeLeft = (TSponsors.getPlugin().getConfig().getLong("regionsPlayTime") * 60 * 1000) - Utils.getTimePlayed(player);
        player.sendMessage(Component.text("Вы должны отыграть еще " + Utils.formatTimeToMinutes(timeLeft) + " чтобы ломать блоки на спавне.")
                .color(NamedTextColor.DARK_RED));
        event.setCancelled(true);
    }
}
