package org.onysand.mc.tsponsors.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.onysand.mc.tsponsors.TSponsors;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Counters {
    public static HashMap<UUID, Integer> gotHeads = new HashMap<>();
    public static HashMap<UUID, Integer> gotMaps = new HashMap<>();
    public static HashMap<UUID, Integer> gotRollbacks = new HashMap<>();
    public static HashMap<UUID, Integer> gotDiscs = new HashMap<>();
    private static final Type typeMap = new TypeToken<HashMap<String, HashMap<UUID, Integer>>>() {}.getType();
    private static final HashMap<String, Integer> maxMaps = new HashMap<>();
    private static final HashMap<String, Integer> maxHeads = new HashMap<>();
    private static final HashMap<String, Integer> maxRollbacks = new HashMap<>();
    private static final HashMap<String, Integer> maxDiscs = new HashMap<>();
    private static final ArrayList<String> sponsorGroups = new ArrayList<>();

    public static void saveCounters(TSponsors plugin) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(plugin.getDataFolder().getAbsolutePath(), "counters.json");
        file.getParentFile().mkdir();

        try (Writer writer = new FileWriter(file)) {
            HashMap<String, HashMap<UUID, Integer>> resultMap = new HashMap<>();
            resultMap.put("heads", gotHeads);
            resultMap.put("maps", gotMaps);
            resultMap.put("rollbacks", gotRollbacks);
            resultMap.put("discs", gotDiscs);

            gson.toJson(resultMap, writer);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void loadCounters(TSponsors plugin) {
        Gson gson = new Gson();
        File file = new File(plugin.getDataFolder().getAbsolutePath(), "counters.json");
        file.getParentFile().mkdir();

        try (Reader reader = new FileReader(file)) {
            HashMap<String, HashMap<UUID, Integer>> map = gson.fromJson(reader, typeMap);
            if (map == null) return;
            gotHeads.putAll(getMap(map, "heads"));
            gotMaps.putAll(getMap(map, "maps"));
            gotRollbacks.putAll(getMap(map, "rollbacks"));
            gotDiscs.putAll(getMap(map, "discs"));

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void startPermissionsCheck(TSponsors plugin) {
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, permCheck -> {
            List<UUID> toRemove = getCountersKeys().stream()
                    .filter(it -> {
                        Player player = Bukkit.getPlayer(it);
                        return player != null && player.getEffectivePermissions().stream()
                                .map(PermissionAttachmentInfo::getPermission)
                                .noneMatch(sponsorGroups::contains);
                    }).toList();

            toRemove.forEach((Counters::removeFromAll));
        }, 0L, 60L * 60 * 1000); // 60 min
    }

    private static HashMap<UUID, Integer> getMap(HashMap<String, HashMap<UUID, Integer>> hashMap, String key) {
        if (hashMap.isEmpty()) return new HashMap<>();
        return hashMap.get(key) == null ? new HashMap<>() : hashMap.get(key);
    }

    private static ArrayList<UUID> getCountersKeys() {
        ArrayList<UUID> keys = new ArrayList<>();
        keys.addAll(gotHeads.keySet());
        keys.addAll(gotMaps.keySet());
        keys.addAll(gotRollbacks.keySet());
        keys.addAll(gotDiscs.keySet());

        return keys;
    }

    private static void removeFromAll(UUID uuid) {
        gotHeads.remove(uuid);
        gotMaps.remove(uuid);
        gotRollbacks.remove(uuid);
        gotDiscs.remove(uuid);
    }

    public static HashMap<String, Integer> getMaxes(UUID uuid) {
        HashMap<String, Integer> result = new HashMap<>();
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return result;

        List<String> perms = player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(sponsorGroups::contains)
                .toList();

        if (perms.isEmpty()) return result;
        String permission = perms.get(0);
        result.put("maps", maxMaps.get(permission));
        result.put("heads", maxHeads.get(permission));
        result.put("rollbacks", maxRollbacks.get(permission));
        result.put("discs", maxDiscs.get(permission));

        return result;
    }

    public static void initializeMax() {
        sponsorGroups.add("tsponsors.month");
        sponsorGroups.add("tsponsors.decade");
        sponsorGroups.add("tsponsors.season");

        maxMaps.put("tsponsors.month", 3);
        maxMaps.put("tsponsors.decade", 5);
        maxMaps.put("tsponsors.season", 8);

        maxHeads.put("tsponsors.month", 3);
        maxHeads.put("tsponsors.decade", 5);
        maxHeads.put("tsponsors.season", 8);

        maxRollbacks.put("tsponsors.month", 1);
        maxRollbacks.put("tsponsors.decade", 2);
        maxRollbacks.put("tsponsors.season", 3);

        maxDiscs.put("tsponsors.month", 1);
        maxDiscs.put("tsponsors.decade", 3);
        maxDiscs.put("tsponsors.season", 5);
    }
}
