package org.onysand.mc.tsponsors.commands.GSit;

import org.onysand.mc.tsponsors.utils.Request;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public class MeSitRequests {
    private static final HashMap<UUID, Request> requests = new HashMap<>();

    public static @Nullable Request getRequest(UUID uuid) {
        return requests.get(uuid);
    }

    public static void addRequest(Request request) {
        requests.put(request.getReceiver(), request);
    }

    public static void removeRequest(Request request) {
        requests.remove(getRequestUID(request));
    }

    public static boolean containsKey(UUID receiver) {
        return requests.containsKey(receiver);
    }

    private static UUID getRequestUID(Request request) {
        return request.getReceiver();
    }
}
