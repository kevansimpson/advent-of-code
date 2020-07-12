package org.base.advent.code2019.intCode;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.CountDownLatch;
import java.util.function.IntSupplier;

public class Amplifier extends Program {
    @Getter private final String name;
    private CountDownLatch latch;

    public Amplifier(final String nm, final int... c) {
        super(c);
        name = nm;
        latch = new CountDownLatch(1);
    }

    @Override
    public int getOutput() {
        try {
            latch.await();
            latch = new CountDownLatch(1);
        } catch (Exception ex) {
            System.err.println("Latch couldn't wait: " + ex.getMessage());
        }

        return super.getOutput();
    }

    @Override
    protected void setOutput(int output) {
        super.setOutput(output);
        latch.countDown();
    }

    @Override
    public String toString() {
        return String.format("Amp[%s-%d @ %d]", getName(), super.getOutput(), getIndex());
    }

    public static String increase(String v, int targetBase) {
        return StringUtils.leftPad(Integer.toString(Integer.parseInt(v, targetBase) + 1, targetBase), targetBase, "0");
    }

    public static class SignalBoost implements IntSupplier {
        private final int boost;
        private final Amplifier amplifier;
        private boolean boostSignalSent;

        public SignalBoost(final int signal, final Amplifier amp) {
            boost = signal;
            amplifier = amp;
        }

        @Override
        public int getAsInt() {
            if (boostSignalSent) return (amplifier == null) ? 0 : amplifier.getOutput();
            boostSignalSent = true;
            return boost;
        }
    }
}
