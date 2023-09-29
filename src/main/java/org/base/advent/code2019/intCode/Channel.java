package org.base.advent.code2019.intCode;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Channel extends ArrayBlockingQueue<Long> {
    public Channel(int capacity) {
        super(capacity);
    }

    public void send(Long o) {
        try {
            super.put(o);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public Long accept() {
        try {
            return super.poll(15, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex) {
            throw new RuntimeException("Channel.accept", ex);
        }
    }

    public static Channel newChannel(int capacity, final long... values) {
        Channel channel = new Channel(capacity);
        for (long v : values) {
            channel.send(v);
        }
        return channel;
    }
}
