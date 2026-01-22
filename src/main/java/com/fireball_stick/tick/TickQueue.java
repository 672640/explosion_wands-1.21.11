package com.fireball_stick.tick;

import java.util.ArrayDeque;
import java.util.Queue;

public class TickQueue {
    private final Queue<Runnable> queue = new ArrayDeque<>();
    private Runnable onComplete;
    //The amount of entities the queue will cycle through
    private int amount;
    //How many entities the queue will cycle through in one tick
    private int amountPerTick;

    //private int iterations = 0;
    public TickQueue(int amount, int amountPerTick) {
        this.amount = amount;
        this.amountPerTick = amountPerTick;
    }

    public void add(Runnable task) {
        queue.add(task);
    }

    public void onComplete(Runnable callback) {
        this.onComplete = callback;
    }

    public boolean tick() {
        if(queue.isEmpty()
                //|| iterations > amount
            ) {
            if(onComplete != null) {
                onComplete.run();
                onComplete = null;
            }
            return true;
        }

        int runsThisTick = Math.min(amountPerTick, queue.size());

        for(int i = 0; i < runsThisTick; i++) {
            queue.poll().run();
        }
        //Obsolete. Can be replaced with the below one instead
        //return iterations >= amount || queue.isEmpty();
        return false;
    }
}
