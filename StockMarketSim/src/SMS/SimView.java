package SMS;

import org.jfree.data.xy.XYDataset;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.*;

public class SimView extends JFrame {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;

    private JButton bSimulation, bDay, bWeek, bMonth, bYear, bNext, bPrev, bSave, bLoad;

    private JMenuBar mBar;
    private JMenu mFile, mGraph;
    private JMenuItem mNew, mLoad, mSave, mSaveAs, mExit, mLineArea;

    private JTabbedPane tab;
    private JPanel tStockGraph, tBottomStock, tPortfolio;

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

        Color bgColour = Color.DARK_GRAY;
        Color fgColour = Color.WHITE;

        // Build buttons and actions handlers
        bSimulation = new JButton("Start Simulation");
        bSimulation.setBackground(bgColour);
        bSimulation.setForeground(fgColour);
        bSimulation.setMaximumSize(new Dimension(70, 50));
        bSimulation.addActionListener(new PlayHandler());
        bSave = new JButton("Save");
        bSave.setBackground(bgColour);
        bSave.setForeground(fgColour);
        bSave.setMaximumSize(new Dimension(70, 50));
        bSave.addActionListener(new SaveHandler());
        bLoad = new JButton("Load");
        bLoad.setBackground(bgColour);
        bLoad.setForeground(fgColour);
        bLoad.setMaximumSize(new Dimension(70, 50));
        bLoad.addActionListener(new LoadHandler());
        bDay = new JButton("Day");
        bDay.setBackground(bgColour);
        bDay.setForeground(fgColour);
        bDay.addActionListener(new Switch2DayHandler());
        bWeek = new JButton("Week");
        bWeek.setBackground(bgColour);
        bWeek.setForeground(fgColour);
        bWeek.addActionListener(new Switch2WeekHandler());
        bMonth = new JButton("Month");
        bMonth.setBackground(bgColour);
        bMonth.setForeground(fgColour);
        bMonth.addActionListener(new Switch2MonthHandler());
        bYear = new JButton("Year");
        bYear.setBackground(bgColour);
        bYear.setForeground(fgColour);
        bYear.addActionListener(new Switch2YearHandler());
        bNext = new JButton("Next");
        bNext.setBackground(bgColour);
        bNext.setForeground(fgColour);
        bNext.addActionListener(new NextPeriodHandler());
        bPrev = new JButton("Prev");
        bPrev.setBackground(bgColour);
        bPrev.setForeground(fgColour);
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
        mNew.setBackground(bgColour);
        mNew.setForeground(fgColour);
        mLoad.setBackground(bgColour);
        mLoad.setForeground(fgColour);
        mSave.setBackground(bgColour);
        mSave.setForeground(fgColour);
        mSaveAs.setBackground(bgColour);
        mSaveAs.setForeground(fgColour);
        mExit.setBackground(bgColour);
        mExit.setForeground(fgColour);
        mLineArea.setBackground(bgColour);
        mLineArea.setForeground(fgColour);
        mFile.setBackground(bgColour);
        mFile.setForeground(fgColour);
        mGraph.setBackground(bgColour);
        mGraph.setForeground(fgColour);
        mBar.setBackground(bgColour);
        setJMenuBar(mBar);

        JPanel tStock = new JPanel(new BorderLayout());
        Box tLeftButtons = Box.createVerticalBox();
        Border b = new EmptyBorder(5,5,5,5);
        UIManager.put("TabbedPane.selected", Color.DARK_GRAY); //change colour of tab when selected
        // Build tabs and add components
        tab = new JTabbedPane();
        // Stock Market tab. There are two panels,
        // first to fit the graph, play button and bottom panel
        stockMarketGraph = new SimGraph();
        tStockGraph = new JPanel(new BorderLayout());
        tStockGraph.add(stockMarketGraph, BorderLayout.CENTER);
        // and second to fit bottom row buttons
        tBottomStock = new JPanel(new GridBagLayout());
        tBottomStock.add(bDay, gbcConstraint(2, 1, 0, 0, 0.1, 0));
        tBottomStock.add(bWeek, gbcConstraint(2, 1, 2, 0, 0.1, 0));
        tBottomStock.add(bMonth, gbcConstraint(2, 1, 4, 0, 0.1, 0));
        tBottomStock.add(bYear, gbcConstraint(2, 1, 6, 0, 0.1, 0));
        tBottomStock.add(bNext, gbcConstraint(2, 1, 8, 0, 0.1, 0));
        tBottomStock.add(bPrev, gbcConstraint(2, 1, 10, 0, 0.1, 0));
        tStockGraph.add(tBottomStock, BorderLayout.SOUTH);
        tLeftButtons.add(bSimulation);
        tLeftButtons.add(Box.createVerticalStrut(20));
        tLeftButtons.add(bSave);
        tLeftButtons.add(Box.createHorizontalGlue());
        tLeftButtons.add(Box.createVerticalStrut(20));
        tLeftButtons.add(bLoad);
        tLeftButtons.setBorder(b);
        tStock.add(tStockGraph, BorderLayout.CENTER);
        tStock.add(tLeftButtons, BorderLayout.WEST);
        // Portfolio tab
        tPortfolio = new JPanel(new GridBagLayout());

        tab.addTab("Stock Market", tStock);
        tab.addTab("Portfolio", tPortfolio);
        tab.setBackground(Color.GRAY);
        tab.setForeground(fgColour);
        tab.setVisible(true);
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

    private class SaveHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Save button pressed");
        }
    }

    private class LoadHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Load button pressed");
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
