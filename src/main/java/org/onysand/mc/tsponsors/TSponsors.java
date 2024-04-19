package org.onysand.mc.tsponsors;

import com.earth2me.essentials.Essentials;
import me.neznamy.tab.api.TabAPI;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapCommonAPI;
import org.onysand.mc.tsponsors.commands.GSit.MeSitCommand;
import org.onysand.mc.tsponsors.commands.GSit.SitHeadCommand;
import org.onysand.mc.tsponsors.commands.bighead.BigHeadCommand;
import org.onysand.mc.tsponsors.commands.broadcast.BroadcastCommand;
import org.onysand.mc.tsponsors.commands.counter.CounterHandler;
import org.onysand.mc.tsponsors.commands.dynMap.DynMapHideCommand;
import org.onysand.mc.tsponsors.commands.tab.ColorNickCommand;
import org.onysand.mc.tsponsors.commands.tab.TabGradientCommands;
import org.onysand.mc.tsponsors.listeners.BigHeadItems;
import org.onysand.mc.tsponsors.listeners.OnLoginGradientCheck;
import org.onysand.mc.tsponsors.listeners.RegionsPlaytime;
import org.onysand.mc.tsponsors.utils.Counters;
import org.onysand.mc.tsponsors.utils.TimersUtil;

public final class TSponsors extends JavaPlugin {
    private static TSponsors plugin;
    public static Essentials essentials;
    public static DynmapCommonAPI dynmapCommonAPI;
    public static NamespacedKey bigHeadKey;
    public static TabAPI tabAPI;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();

        essentials = Essentials.getPlugin(Essentials.class);
        dynmapCommonAPI = (DynmapCommonAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");
        tabAPI = TabAPI.getInstance();

        registerCommands();
        registerListeners();

        TimersUtil.loadTimers(plugin);
        TimersUtil.startDynMapChecker(plugin);

        Counters.loadCounters(plugin);
        Counters.startPermissionsCheck(plugin);
        Counters.initializeMax();

        bigHeadKey = new NamespacedKey(plugin, "bighead");
    }

    @Override
    public void onDisable() {
        TimersUtil.saveTimers(plugin);
        Counters.saveCounters(plugin);
    }

    private void registerCommands() {
        getCommand("scount").setExecutor(new CounterHandler());
        getCommand("sithead").setExecutor(new SitHeadCommand());
        getCommand("mesit").setExecutor(new MeSitCommand());
        getCommand("tabgradient").setExecutor(new TabGradientCommands());
        getCommand("dmaphide").setExecutor(new DynMapHideCommand());
        getCommand("tbroadcast").setExecutor(new BroadcastCommand());
        getCommand("bighead").setExecutor(new BigHeadCommand());
        getCommand("colornick").setExecutor(new ColorNickCommand());
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new BigHeadItems(), this);
        pm.registerEvents(new RegionsPlaytime(), this);
        pm.registerEvents(new OnLoginGradientCheck(), this);
    }

    public static TSponsors getPlugin() {
        return plugin;
    }
}
