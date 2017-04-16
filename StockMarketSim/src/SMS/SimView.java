package SMS;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class SimView extends JFrame {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;

    private JButton bSimulation;
    private PlayHandler bhSim;

    private JMenuBar mBar;
    private JMenu mFile;
    private JMenuItem mNew, mLoad, mSave, mSaveAs, mExit;

    private JTabbedPane tab;
    private JPanel tStock, tPortfolio;

    private SimGraph lineG;

    // Construct the frame
    public SimView() {
        setTitle("Team-X Stock Market Simulation");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUp();
        setVisible(true);
    }

    // Add contents to frame
    public void setUp() {
        Container pane = getContentPane();
        pane.setLayout(new GridLayout(1, 1));
        GridBagConstraints c = new GridBagConstraints();

        // Build Menu Bar, Menus and Menu Items
        mBar = new JMenuBar();
        mFile = new JMenu("File");
        mFile.setMnemonic(KeyEvent.VK_F);
        mNew = new JMenuItem("New Simulation", KeyEvent.VK_N);
        mFile.add(mNew);
        mLoad = new JMenuItem("Load Simulation", KeyEvent.VK_L);
        mFile.add(mLoad);
        mSave = new JMenuItem("Save Simulation", KeyEvent.VK_S);
        mFile.add(mSave);
        mSaveAs = new JMenuItem("Save As Simulation", KeyEvent.VK_A);
        mFile.add(mSaveAs);
        mExit = new JMenuItem("Exit program", KeyEvent.VK_X);
        mExit.addActionListener(e -> System.exit(0));
        mFile.add(mExit);
        mBar.add(mFile);
        setJMenuBar(mBar);

        // Build buttons and actions handlers
        bSimulation = new JButton("Start Simulation");
        bhSim = new PlayHandler();
        bSimulation.addActionListener(bhSim);

        // Build tabs and add components
        tab = new JTabbedPane();
        tStock = new JPanel(new BorderLayout());
        tab.addTab("Stock Market", tStock);
        tStock.add(bSimulation, BorderLayout.NORTH);
        lineG = new SimGraph();
        lineG.addData();
        tStock.add(lineG, BorderLayout.CENTER);
        tPortfolio = new JPanel(new GridBagLayout());
        tab.addTab("Portfolio", tPortfolio);

        pane.add(tab);
    }

    public AbstractButton getBtnPlay() { return bSimulation; }
    public AbstractButton getMenuNew() { return mNew; }
    public AbstractButton getMenuLoad() { return mLoad; }
    public AbstractButton getMenuSave() { return mSave; }
    public AbstractButton getMenuSaveAs() { return mSaveAs; }

    private class PlayHandler implements ActionListener {
        private boolean clicked = false;
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!clicked) {
                bSimulation.setText("Pause");
                clicked = true;
            } else {
                bSimulation.setText("Play");
                clicked = false;
            }
        }
    }
}
