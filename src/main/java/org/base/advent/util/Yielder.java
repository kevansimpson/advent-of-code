package org.base.advent.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Refactored from
 * <a href="https://github.com/mherrmann/java-generator-functions/blob/master/src/main/java/io/herrmann/generator/Generator.java">here</a>.
 * @param <T> The type of elements returned by the Iterator.
 */
public abstract class Yielder<T> implements Iterable<T> {
    private static class Condition {
        private boolean isSet;

        public synchronized void set() {
            isSet = true;
            notify();
        }

        public synchronized void await() throws InterruptedException {
            try {
                if (isSet) return;
                wait();
            } finally {
                isSet = false;
            }
        }
    }

    static ThreadGroup THREAD_GROUP;

    private boolean finished = false, itemAvailable;
    private T nextItem;
    private RuntimeException raisedError;
    private final Condition availableOrDone = new Condition(), requested = new Condition();
    private Thread daemon;

    protected abstract void run() throws InterruptedException;

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return isWaiting();
            }

            @Override
            public T next() {
                if (!isWaiting()) throw new NoSuchElementException("Yielder.iterator.next");
                itemAvailable = false;
                return nextItem;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Yielder.iterator.remove");
            }

            private boolean isWaiting() {
                if (itemAvailable) return true;
                if (finished) return false;
                // start producer 1x
                if (daemon == null) daemon = startDaemon();

                requested.set();
                try {
                    availableOrDone.await();
                } catch (InterruptedException ex) {
                    finished = true;
                }
                if (raisedError != null) throw raisedError;
                return !finished;
            }
        };
    }

    protected void yield(final T item) throws InterruptedException {
        nextItem = item;
        itemAvailable = true;
        availableOrDone.set();
        requested.await();
    }

    protected Thread startDaemon() {
        if (THREAD_GROUP == null)
            THREAD_GROUP = new ThreadGroup("advent-of-code");
        daemon = new Thread(THREAD_GROUP, () -> {
            //noinspection CatchMayIgnoreException
            try {
                requested.await();
                Yielder.this.run();
            } catch (InterruptedException ex) {
            } catch (RuntimeException ex) {
                raisedError = ex;
            }
            finished = true;
            availableOrDone.set();
        }, "Yielder-"+ this.getClass().getSimpleName());
        daemon.setDaemon(true);
        daemon.start();
        return daemon;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void finalize() throws Throwable {
        daemon.interrupt();
        daemon.join();
        super.finalize();
    }
}
