package org.onysand.mc.tsponsors.utils;

import com.google.gson.Gson;
import org.onysand.mc.tsponsors.TSponsors;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class TimersUtility {
    public static HashMap<UUID, Long> broadcastTimers = new HashMap<>();
    public static HashMap<UUID, Long> dynMapTimers = new HashMap<>();

    public static void saveTimers(TSponsors plugin) {
        Gson gson = new Gson();
        File file = new File(plugin.getDataFolder().getAbsolutePath(), "timers.json");
        file.getParentFile().mkdir();

        try (Writer writer = new FileWriter(file)) {
            HashMap<String, HashMap<UUID, Long>> resultMap = new HashMap<>();
            resultMap.put("broadcast", broadcastTimers);
            resultMap.put("dynMap", dynMapTimers);

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
            HashMap<String, HashMap<UUID, Long>> map = gson.fromJson(reader, HashMap.class);
            broadcastTimers.putAll(map.get("broadcast"));
            dynMapTimers.putAll(map.get("dynMap"));

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
