package SMS;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.*;

public class Simulation {
    private SimView view;
    private Market model;
    private boolean activeCycle;

    // Test commit
    public Simulation() {
        view = new SimView();
        activeCycle = false;
        setUpAction();
    }

    private void setUpAction() {
        // add action to play/pause button
        view.getBtnPlay().setAction(new AbstractAction("Play") {
            private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            private ScheduledFuture sf;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                ViewPlay va = new SMS.ViewPlay();
                if (!activeCycle) {
                    activeCycle = true;
                    sf = executor.scheduleAtFixedRate(va.play, 0, 1, TimeUnit.SECONDS);
                } else {
                    activeCycle = false;
                    sf.cancel(true);
                }
            }
        });

        // add action to 'New' menu item under 'File'
        // create new Market object here by passing two .xlsx files
        view.getMenuNew().setAction(new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("New Sim");
                SimViewNew newSimFrame = new SimViewNew();
                if (!newSimFrame.getInitialData().isEmpty() && !newSimFrame.getExternalData().isEmpty()) {
                    model = new Market(newSimFrame.getInitialData(), newSimFrame.getExternalData());
                }
                System.out.println(newSimFrame.getInitialData());
                System.out.println(newSimFrame.getExternalData());
            }
        });
    }
}
