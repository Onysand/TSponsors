package org.onysand.mc.tsponsors.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.onysand.mc.tsponsors.utils.TimersUtil;

public class OnLoginGradientCheck implements Listener {

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        if (!TimersUtil.rgbNameTimers.containsKey(event.getPlayer().getUniqueId())) return;

        Player player = event.getPlayer();
        long timerTime = TimersUtil.rgbNameTimers.get(player.getUniqueId());
        long timeNow = System.currentTimeMillis();
        long checkTimeInMillis = 7 * 24 * 60 * 60 * 1000;
        if (timeNow - timerTime > checkTimeInMillis) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("tab player %s customtabname", player.getName()));

            player.sendMessage(Component.text("Пришло время обновить градиент ника!\n").color(NamedTextColor.GREEN)
                    .append(Component.text("Для установки используйте команду: /colornick HEX цвет начала HEX цвет конца").color(NamedTextColor.GOLD)));
        }
    }
}
