package org.base.advent.util;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow;

public class ProgressBar extends JPanel implements Flow.Subscriber<Integer> {
    private final CountDownLatch latch;
    private final int count;
    private int percent = 0;
    private Flow.Subscription subscription = null;
    // swing
    private JProgressBar progressBar;

    public ProgressBar(int expectedCount) {
        super(new BorderLayout());
        count = expectedCount;
        latch = new CountDownLatch(count);
        initUI();
    }

    void initUI() {
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.PAGE_START);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    @Override
    public void onSubscribe(Flow.Subscription sub) {
        subscription = sub;
        subscription.request(1);
    }

    @Override
    public void onNext(Integer item) {
        latch.countDown();
        tally(item);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throw new RuntimeException(throwable); // don't fail silently
    }

    @Override
    public void onComplete() { /* no op */ }

    int displayProgress(String message, int index) {
        JFrame frame = createUI(message, index);
        javax.swing.SwingUtilities.invokeLater(() -> frame.setVisible(true));

        try {
            latch.await();
        }
        catch (Exception ignored) {}

        frame.setVisible(false);
        return percent;
    }

    void tally(int current) {
        int next = (current * 100) / count;
        if (next > percent) {
            percent = next;
            progressBar.setValue(percent);
        }
    }

    private JFrame createUI(String message, int index) {
        //Create and set up the window.
        JFrame frame = new JFrame(message);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = this;
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        frame.pack();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        frame.setLocation(x, 25 + (100 * index));

        return frame;
    }
}
