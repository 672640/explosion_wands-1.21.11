package com.fireball_stick.tick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TickQueueManager {
    private static final List<TickQueue> ACTIVE_QUEUE = new ArrayList<>();

    public static TickQueue createQueue(int amount, int amountPerTick) {
        TickQueue queue = new TickQueue(amount, amountPerTick);
        ACTIVE_QUEUE.add(queue);
        return queue;
    }

    public static void tick() {
        Iterator<TickQueue> iterator = ACTIVE_QUEUE.iterator();

        while(iterator.hasNext()) {
            TickQueue queue = iterator.next();
            boolean finished = queue.tick();

            if(finished) {
                iterator.remove();
            }
        }
    }
}
