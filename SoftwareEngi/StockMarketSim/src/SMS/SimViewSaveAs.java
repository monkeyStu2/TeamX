package SMS;

import javax.swing.*;

public class SimViewSaveAs extends JDialog {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 400;

    public SimViewSaveAs() {
        setTitle("Save As Simulation");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setUp();
        setVisible(true);
    }

    public void setUp() {

    }

}
