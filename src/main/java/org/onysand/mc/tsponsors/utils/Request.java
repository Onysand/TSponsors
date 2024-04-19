package org.onysand.mc.tsponsors.utils;

import java.util.UUID;

public class Request {
    private final UUID sender;
    private final UUID receiver;
    private final long endTime;

    public Request(UUID sender, UUID receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.endTime = System.currentTimeMillis() + (15 * 1000); // secs * 1000 = in millis
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public long getEndTime() {
        return endTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > endTime;
    }
}
