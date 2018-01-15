package org.base.advent.code2017.day18;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class PairedTablet extends Tablet {
    @Getter @Setter
    private PairedTablet pair;
    @Getter
    private int sentCount;
    @Getter(AccessLevel.PRIVATE)
    private final LinkedBlockingQueue<Long> queue = new LinkedBlockingQueue<>();

    public PairedTablet(final long id) {
        set("p", id);
    }

    /**
     *  - snd X sends the value of X to the other program. These values wait in a queue until that program is ready
     *          to receive them. Each program has its own message queue, so a program can never receive a message it sent.
     *
     * @param register The register of X
     */
    @Override
    public void listen(final String register) throws Exception {
        ++sentCount;
        getPair().getQueue().put(get(register));
    }

    /**
     *  - rcv X receives the next value and stores it in register X. If no values are in the queue, the program waits
     *          for a value to be sent to it. Programs do not continue to the next instruction until they have received a
     *          value. Values are received in the order they are sent.
     *
     * @param register The register of X
     */
    @Override
    public void receive(final String register) {
        try {
            set(register, getQueue().poll(50, TimeUnit.MILLISECONDS));
        }
        catch (final InterruptedException ex) {
            throw new RuntimeException("Interrupted: "+ register +" ~ "+ getQueue());
        }
    }
}
