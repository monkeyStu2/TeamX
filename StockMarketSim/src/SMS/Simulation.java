package SMS;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.*;

public class Simulation {
    private SimView view;
    private MarketTest model;
    private boolean activeCycle;

    // Test commit
    public Simulation() {
        view = new SimView();
        model = new MarketTest();
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
                ViewPlay va = new ViewPlay();
                if (!activeCycle) {
                    activeCycle = true;
                    sf = executor.scheduleAtFixedRate(va.play, 0, 10, TimeUnit.MILLISECONDS);
                } else {
                    activeCycle = false;
                    sf.cancel(true);
                }
            }
        });

        // add action to 'New' menu item under 'File'
        // create new MarketTest object here by passing two .xlsx files
        view.getMenuNew().setAction(new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("New Sim");
                SimViewNew newSimFrame = new SimViewNew();
                if (!newSimFrame.getInitialData().isEmpty() && !newSimFrame.getExternalData().isEmpty()) {
                    model = new MarketTest(newSimFrame.getInitialData(), newSimFrame.getExternalData());
                }
            }
        });

        view.getMenuLoad().setAction(new AbstractAction("Load") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Load Sim");
                SimViewNew loadSimFrame = new SimViewNew();
            }
        });

        view.getMenuSave().setAction(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Save Sim");
                SimViewNew saveSimFrame = new SimViewNew();
            }
        });

        view.getMenuSaveAs().setAction(new AbstractAction("Save As") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Save As Sim");
                SimViewNew saveAsSimFrame = new SimViewNew();
            }
        });
    }

    private class ViewPlay {
        private int x = 0;
        Runnable play = () -> {
            System.out.println("Test - " + x);
            x += 1;
            model.addData();
            view.setDataset(model.getDataset());
        };
    }
}
