package SMS;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.concurrent.*;

/**
 * Created by Robert on 26/03/2017.
 */
public class SimView extends JFrame {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 300;

    private JLabel l, no, data;
    private JButton b;

    private ButtonHandler bh;

    private int x = 0;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    // Construct the frame
    public SimView() {
        setTitle("Team-X Stock Market Simulation");
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUp();
    }

    // Add contents to frame
    public void setUp() {
        Container pane = getContentPane();
        pane.setLayout(new GridLayout(4, 2));
        l = new JLabel("This is a stock market simulation", SwingConstants.CENTER);
        no = new JLabel(String.valueOf(x), SwingConstants.CENTER);
        data = new JLabel("You are seeing actual data here", SwingConstants.CENTER);
        b = new JButton("Start simulation");
        bh = new ButtonHandler();
        b.addActionListener(bh);

        pane.add(l);
        pane.add(no);
        pane.add(data);
        pane.add(b);
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            executorService.scheduleAtFixedRate((Runnable) () -> {
                x += 1;
                no.setText(String.valueOf(x));
                if (x == 15){
                    data.setText("There is really data here...");
                } else if (x == 30) {
                    data.setText("You are under an illusion that there is no data here");
                } else if (x == 45) {
                    data.setText("Look, Stock Market is done. We can just hand this in! We will get as least 95%");
                } else if (x == 60) {
                    data.setText("The other 5% is that no one written an user instruction");
                } else if (x == 75) {
                    data.setText("sigh... I quit");
                } else if (x == 100) {
                    data.setText("Joking, why would I throw away such good work here!");
                } else if (x == 125) {
                    data.setText("This program will hack W&G server... oh wait wrong module! Forget I said anything!");
                }
            }, 0, 300, TimeUnit.MILLISECONDS);
        }
    }
}
