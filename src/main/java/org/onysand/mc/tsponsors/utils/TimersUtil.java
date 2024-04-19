package org.onysand.mc.tsponsors.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.dynmap.DynmapAPI;
import org.onysand.mc.tsponsors.TSponsors;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class TimersUtil {
    public static HashMap<UUID, Long> broadcastTimers = new HashMap<>();
    public static HashMap<UUID, Long> dynMapTimers = new HashMap<>();
    public static HashMap<UUID, Long> rgbNameTimers = new HashMap<>();
    private static final Type typeMap = new TypeToken<HashMap<String, HashMap<UUID, Long>>>() {}.getType();

    public static void startDynMapChecker(TSponsors plugin) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task -> {
            long timeNow = System.currentTimeMillis();
            ArrayList<UUID> toRemove = new ArrayList<>();
            dynMapTimers.forEach((UUID uuid, Long endTime) -> {
                if (timeNow > endTime) {
                    TSponsors.dynmapCommonAPI.setPlayerVisiblity(Bukkit.getPlayer(uuid).getName(), true);
                    toRemove.add(uuid);
                }
            });
            toRemove.stream().forEach(it -> dynMapTimers.remove(it));
            toRemove.clear();
        },0L, 60000L);
    }

    public static void saveTimers(TSponsors plugin) {
        Gson gson = new Gson();
        File file = new File(plugin.getDataFolder().getAbsolutePath(), "timers.json");
        file.getParentFile().mkdir();

        try (Writer writer = new FileWriter(file)) {
            HashMap<String, HashMap<UUID, Long>> resultMap = new HashMap<>();
            resultMap.put("broadcast", broadcastTimers);
            resultMap.put("dynMap", dynMapTimers);
            resultMap.put("rgb", rgbNameTimers);

            gson.toJson(resultMap, writer);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void loadTimers(TSponsors plugin) {
        Gson gson = new Gson();
        File file = new File(plugin.getDataFolder().getAbsolutePath(), "timers.json");
        file.getParentFile().mkdir();

        try (Reader reader = new FileReader(file)) {
            HashMap<String, HashMap<UUID, Long>> map = gson.fromJson(reader, typeMap);
            broadcastTimers.putAll(getMap(map, "broadcast"));
            dynMapTimers.putAll(getMap(map, "dynMap"));
            rgbNameTimers.putAll(getMap(map, "rgb"));

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static HashMap<UUID, Long> getMap(HashMap<String, HashMap<UUID, Long>> hashMap, String key) {
        return hashMap.getOrDefault(key, new HashMap<>());
    }
}
