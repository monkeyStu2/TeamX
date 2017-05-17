package SMS;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * @author Robert Garner
 */
class Simulation {
    private SimView view;
    private Market model;
    private boolean activeCycle;

    Simulation() {
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
            public void actionPerformed(ActionEvent e) {
                ViewPlay va = new ViewPlay();
                if (!activeCycle) {
                    activeCycle = true;
                    sf = executor.scheduleAtFixedRate(va.play, 0, 50, TimeUnit.MILLISECONDS);
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
                    model = new Market(newSimFrame.getInitialData());
                    view.createDataset(model.getDate(), model.getTime(), (ArrayList<Company>) model.getCompanies(), model.getWGTrader());
                    view.simButtonEnable();
                }
            }
        });

        view.getMenuTest().setAction(new AbstractAction("Test") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Test Sim");
                SimViewNew newSimFrame = new SimViewNew();
                if (!newSimFrame.getInitialData().isEmpty() && !newSimFrame.getExternalData().isEmpty()) {
                    model = new Market(newSimFrame.getInitialData());
                    view.createDataset(model.getDate(), model.getTime(), (ArrayList<Company>) model.getCompanies(), model.getWGTrader());
                }
            }
        });

        view.getMenuStopTest().setAction(new AbstractAction("Start/Stop Test") {
            private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            private ScheduledFuture sf;

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Start/Stop Test Sim");
                ViewTestPlay va = new ViewTestPlay();
                if (!activeCycle) {
                    activeCycle = true;
                    sf = executor.scheduleAtFixedRate(va.play, 0, 50, TimeUnit.MILLISECONDS);
                } else {
                    activeCycle = false;
                    sf.cancel(true);
                }
            }
        });

        view.getMenuReset().setAction(new AbstractAction("Reset Test") {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Reset Sim");
                view.clearAll();
            }
        });

        view.getClientTable().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                view.updateClientTable(model.getWGTrader());
            }
        });

//        view.getMenuLoad().setAction(new AbstractAction("Load") {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Load Sim");
//                SimViewNew loadSimFrame = new SimViewNew();
//            }
//        });
//
//        view.getMenuSave().setAction(new AbstractAction("Save") {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Save Sim");
//                SimViewNew saveSimFrame = new SimViewNew();
//            }
//        });
//
//        view.getMenuSaveAs().setAction(new AbstractAction("Save As") {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Save As Sim");
//                SimViewNew saveAsSimFrame = new SimViewNew();
//            }
//        });
    }

    private class ViewPlay {
        Runnable play = () -> {
            model.cycle();
            view.updateDataset(model.getDate(), model.getTime(), (ArrayList<Company>) model.getCompanies());
        };
    }

    private class ViewTestPlay {
        Runnable play = () -> {
            model.cycle("Test");
            view.updateDataset(model.getDate(), model.getTime(), (ArrayList<Company>) model.getCompanies());
        };
    }


}
