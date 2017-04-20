package SMS;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.awt.*;

public class SimView extends JFrame {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;

    private JButton bSimulation, bDay, bWeek, bMonth, bYear, bNext, bPrev;

    private JMenuBar mBar;
    private JMenu mFile, mGraph;
    private JMenuItem mNew, mLoad, mSave, mSaveAs, mExit, mLineArea;

    private JTabbedPane tab;
    private JPanel tStock, tBottomStock, tPortfolio;

    private SimGraph stockMarketGraph;

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

        // Build buttons and actions handlers
        bSimulation = new JButton("Start Simulation");
        bSimulation.setBackground(Color.DARK_GRAY);
        bSimulation.setForeground(Color.WHITE);
        bSimulation.addActionListener(new PlayHandler());
        bDay = new JButton("Day");
        bDay.setBackground(Color.DARK_GRAY);
        bDay.setForeground(Color.WHITE);
        bDay.addActionListener(new Switch2DayHandler());
        bWeek = new JButton("Week");
        bWeek.setBackground(Color.DARK_GRAY);
        bWeek.setForeground(Color.WHITE);
        bWeek.addActionListener(new Switch2WeekHandler());
        bMonth = new JButton("Month");
        bMonth.setBackground(Color.DARK_GRAY);
        bMonth.setForeground(Color.WHITE);
        bMonth.addActionListener(new Switch2MonthHandler());
        bYear = new JButton("Year");
        bYear.setBackground(Color.DARK_GRAY);
        bYear.setForeground(Color.WHITE);
        bYear.addActionListener(new Switch2YearHandler());
        bNext = new JButton("Next");
        bNext.setBackground(Color.DARK_GRAY);
        bNext.setForeground(Color.WHITE);
        bNext.addActionListener(new NextPeriodHandler());
        bPrev = new JButton("Prev");
        bPrev.setBackground(Color.DARK_GRAY);
        bPrev.setForeground(Color.WHITE);
        bPrev.addActionListener(new PrevPeriodHandler());

        // Build Menu Bar, Menus and Menu Items
        mBar = new JMenuBar();
        mFile = new JMenu("File");
        mFile.setMnemonic(KeyEvent.VK_F);
        mGraph = new JMenu("Graph");
        mGraph.setMnemonic(KeyEvent.VK_G);
        // file menu items
        mNew = new JMenuItem("New Simulation", KeyEvent.VK_N);
        mLoad = new JMenuItem("Load Simulation", KeyEvent.VK_L);
        mSave = new JMenuItem("Save Simulation", KeyEvent.VK_S);
        mSaveAs = new JMenuItem("Save As Simulation", KeyEvent.VK_A);
        mExit = new JMenuItem("Exit program", KeyEvent.VK_X);
        mExit.addActionListener(e -> System.exit(0));
        mFile.add(mNew);
        mFile.add(mLoad);
        mFile.add(mSave);
        mFile.add(mSaveAs);
        mFile.add(mExit);
        // graph menu items
        mLineArea = new JMenuItem("Switch render", KeyEvent.VK_R);
        mLineArea.addActionListener(new SwitchGraph());
        mGraph.add(mLineArea);
        // add to menu bar
        mBar.add(mFile);
        mBar.add(mGraph);
        setJMenuBar(mBar);

        // Build tabs and add components
        tab = new JTabbedPane();
        // Stock Market tab. There are two panels,
        // first to fit the graph, play button and bottom panel
        stockMarketGraph = new SimGraph();
        tStock = new JPanel(new BorderLayout());
        tStock.add(bSimulation, BorderLayout.NORTH);
        tStock.add(stockMarketGraph, BorderLayout.CENTER);
        // and second to fit bottom row buttons
        tBottomStock = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        tBottomStock.add(bDay, gbcConstraint(2, 1, 0, 0, 0.1, 0));
        tBottomStock.add(bWeek, gbcConstraint(2, 1, 2, 0, 0.1, 0));
        tBottomStock.add(bMonth, gbcConstraint(2, 1, 4, 0, 0.1, 0));
        tBottomStock.add(bYear, gbcConstraint(2, 1, 6, 0, 0.1, 0));
        tBottomStock.add(bNext, gbcConstraint(2, 1, 8, 0, 0.1, 0));
        tBottomStock.add(bPrev, gbcConstraint(2, 1, 10, 0, 0.1, 0));
        tStock.add(tBottomStock, BorderLayout.SOUTH);
        // Portfolio tab
        tPortfolio = new JPanel(new GridBagLayout());

        tab.addTab("Stock Market", tStock);
        tab.addTab("Portfolio", tPortfolio);
        pane.add(tab);
    }

    public void setDataset(XYDataset dataset) {
        stockMarketGraph.setDataset(dataset);
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

    private class Switch2DayHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Day button pressed");
            stockMarketGraph.switch2Day();
        }
    }

    private class Switch2WeekHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Week button pressed");
            stockMarketGraph.switch2Week();
        }
    }

    private class Switch2MonthHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Month button pressed");
            stockMarketGraph.switch2Month();
        }
    }

    private class Switch2YearHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Year button pressed");
            stockMarketGraph.switch2Year();
        }
    }

    private class NextPeriodHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Next button pressed");
            stockMarketGraph.nextPeriod();
        }
    }

    private class PrevPeriodHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Prev button pressed");
            stockMarketGraph.prevPeriod();
        }
    }

    private class SwitchGraph implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Switch button pressed");
            stockMarketGraph.switchGraph();
        }
    }

    /**Set where a component will be placed on a GridBagLayout panel
     * gridW and gridH set the size of the component on the grid
     * gridX and gridY set the location of the component on the grid
     * weightX and weightY set whether the component will fill up the panel or not*/
    protected static GridBagConstraints gbcConstraint(int gridW, int gridH, int gridX, int gridY, double weightX, double weightY) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(1, 2, 1, 2);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = gridW;
        gbc.gridheight = gridH;
        gbc.gridx = gridX;
        gbc.gridy = gridY;
        gbc.weightx = weightX;
        gbc.weighty = weightY;
        return gbc;
    }
}
