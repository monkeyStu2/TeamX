package SMS;

import org.jfree.data.xy.XYDataset;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.awt.*;

public class SimView extends JFrame {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;

    private JButton bSimulation, bDay, bWeek, bMonth, bYear, bNext, bPrev, bSave, bLoad;

    private JMenuItem mNew;
    private JMenuItem mLoad;
    private JMenuItem mSave;
    private JMenuItem mSaveAs;

    private JTabbedPane tab;
    private JPanel tStock, tPortfolio;

    private JTable stockTable;

    private SimGraph stockMarketGraph;

    // Construct the frame
    public SimView() {
        setTitle("Team-X Stock Market Simulation");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 600));
        setUp();
        setVisible(true);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFrame.setDefaultLookAndFeelDecorated(false);
            JDialog.setDefaultLookAndFeelDecorated(false);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    // Add contents to frame
    private void setUp() {
        UIManager.put("TabbedPane.borderHightlightColor", Color.WHITE);
        UIManager.put("TabbedPane.darkShadow", Color.WHITE);
        UIManager.put("TabbedPane.light", Color.WHITE);
        UIManager.put("TabbedPane.selectHighlight", Color.BLACK);
        UIManager.put("TabbedPane.focus", Color.DARK_GRAY);
        UIManager.put("TabbedPane.contentAreaColor", Color.BLACK);
        UIManager.put("TabbedPane.selected", Color.BLACK);
        UIManager.put("TabbedPane.unselected", Color.DARK_GRAY);

        Container pane = getContentPane();
        pane.setLayout(new GridLayout(1, 1));

        setUpButtons();
        setUpMenuBar();
        setUpStockTable();
        setUpTabbedPane();

        pane.add(tab);
    }

    private void setUpButtons() {
        Color bgColour = Color.DARK_GRAY;
        Color fgColour = Color.WHITE;
        Border buttonB = BorderFactory.createLineBorder(Color.WHITE, 1, true);

        // Build buttons and actions handlers
        Dimension size = new Dimension(50, 40);
        bSimulation = createButton("Simulation", bgColour, fgColour, size, new PlayHandler(), buttonB, false);
        bSave = createButton("Save", bgColour, fgColour, size, new SaveHandler(), buttonB, false);
        bLoad = createButton("Load", bgColour, fgColour, size, new LoadHandler(), buttonB, false);

        size = new Dimension(50, 20);
        bDay = createButton("Day", bgColour, fgColour, size, new Switch2DayHandler(), buttonB, false);
        bWeek = createButton("Week", bgColour, fgColour, size, new Switch2WeekHandler(), buttonB, false);
        bMonth = createButton("Month", bgColour, fgColour, size, new Switch2MonthHandler(), buttonB, false);
        bYear = createButton("Year", bgColour, fgColour, size, new Switch2YearHandler(), buttonB, false);
        bNext = createButton("Next", bgColour, fgColour, size, new NextPeriodHandler(), buttonB, false);
        bPrev = createButton("Prev", bgColour, fgColour, size, new PrevPeriodHandler(), buttonB, false);
    }

    private JButton createButton(String name, Color bg, Color fg, Dimension size, ActionListener handler, Border border, Boolean focusable) {
        JButton but = new JButton(name);
        but.setBackground(bg);
        but.setForeground(fg);
        but.setMaximumSize(size);
        but.setPreferredSize(size);
        but.addActionListener(handler);
        but.setFocusable(focusable);
        but.setBorder(border);
        return but;
    }

    private void setUpMenuBar() {
        JMenuBar mBar = new JMenuBar();
        JMenu mFile = new JMenu("File");
        JMenu mGraph = new JMenu("Graph");
        JMenuItem mExit = new JMenuItem("Exit program", KeyEvent.VK_X);
        JMenuItem mZoomIn = new JMenuItem("Zoom-In", KeyEvent.VK_I);
        JMenuItem mZoomOut = new JMenuItem("Zoom-Out", KeyEvent.VK_O);

        Color bgColour = Color.DARK_GRAY;
        Color fgColour = Color.WHITE;

        // Build Menu Bar, Menus and Menu Items
        mFile.setMnemonic(KeyEvent.VK_F);
        mGraph.setMnemonic(KeyEvent.VK_G);

        // file menu items
        mNew = new JMenuItem("New Simulation", KeyEvent.VK_N);
        mLoad = new JMenuItem("Load Simulation", KeyEvent.VK_L);
        mSave = new JMenuItem("Save Simulation", KeyEvent.VK_S);
        mSaveAs = new JMenuItem("Save As Simulation", KeyEvent.VK_A);
        mExit.addActionListener(e -> System.exit(0));
        mFile.add(mNew);
        mFile.add(mLoad);
        mFile.add(mSave);
        mFile.add(mSaveAs);
        mFile.add(mExit);

        //graph menu items
        mZoomIn.addActionListener(e -> stockMarketGraph.zoomIn());
        mZoomOut.addActionListener(e -> stockMarketGraph.zoomOut());
        mGraph.add(mZoomIn);
        mGraph.add(mZoomOut);

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
        mFile.setBackground(bgColour);
        mFile.setForeground(fgColour);
        mZoomIn.setBackground(bgColour);
        mZoomIn.setForeground(fgColour);
        mZoomOut.setBackground(bgColour);
        mZoomOut.setForeground(fgColour);
        mGraph.setBackground(bgColour);
        mGraph.setForeground(fgColour);

        mBar.setBackground(bgColour);
        setJMenuBar(mBar);
    }

    /**create table for tStock tab*/
    private void setUpStockTable() {
        String[] columnsName = new String[]{"Company", "Type", "Pence"};
        Object[][] data = {{"", "", ""}};
        stockTable = setUpTable(data, columnsName, Color.BLACK, Color.WHITE, Color.DARK_GRAY, new Color(156, 0, 0, 199),
                true, false, true);
        ListSelectionModel listModel = stockTable.getSelectionModel();
        listModel.addListSelectionListener(new TableRowListener());
    }

    /** Build tabs and add components*/
    private void setUpTabbedPane() {
        tab = new JTabbedPane();

        Color bgColour = Color.DARK_GRAY;
        Color fgColour = Color.WHITE;

        stockMarketTab();
        portfolioTab();

        tab.addTab("Stock Market", tStock);
        tab.addTab("Portfolio", tPortfolio);
        tab.setBackground(bgColour);
        tab.setForeground(fgColour);
        tab.setOpaque(true);
        tab.setVisible(true);
    }

    /** Stock Market tab. There are four panels,
    first to fit the graph,
     second for left panel buttons
     third for bottom panel buttons
     fourth for right panel table with scrollpane*/
    private void stockMarketTab() {
        tStock = new JPanel(new BorderLayout());
        JPanel tStockGraph = new JPanel(new BorderLayout());
        JPanel tRightStock = new JPanel(new BorderLayout());
        JPanel tBottomStock = new JPanel(new GridBagLayout());
        JScrollPane sRightStock = new JScrollPane(tRightStock);
        Box tLeftButtons = Box.createVerticalBox();
        Border b = new EmptyBorder(5,5,5,5);

        stockMarketGraph = new SimGraph();
        tStockGraph.add(stockMarketGraph, BorderLayout.CENTER);
        tStockGraph.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        // and second to fit bottom row buttons
        tBottomStock.add(bDay, gbcConstraint(2, 1, 0, 0, 0.1, 0));
        tBottomStock.add(bWeek, gbcConstraint(2, 1, 2, 0, 0.1, 0));
        tBottomStock.add(bMonth, gbcConstraint(2, 1, 4, 0, 0.1, 0));
        tBottomStock.add(bYear, gbcConstraint(2, 1, 6, 0, 0.1, 0));
        tBottomStock.add(bNext, gbcConstraint(2, 1, 8, 0, 0.1, 0));
        tBottomStock.add(bPrev, gbcConstraint(2, 1, 10, 0, 0.1, 0));
        tBottomStock.setOpaque(true);
        tBottomStock.setBackground(Color.BLACK);
        tStockGraph.add(tBottomStock, BorderLayout.SOUTH);
        tLeftButtons.add(bSimulation);
        tLeftButtons.add(Box.createVerticalStrut(20));
        tLeftButtons.add(bSave);
        tLeftButtons.add(Box.createHorizontalGlue());
        tLeftButtons.add(Box.createVerticalStrut(20));
        tLeftButtons.add(bLoad);
        tLeftButtons.setBorder(b);
        tLeftButtons.setOpaque(true);
        tLeftButtons.setBackground(Color.BLACK);
        tRightStock.add(stockTable.getTableHeader(), BorderLayout.PAGE_START);
        tRightStock.add(stockTable, BorderLayout.CENTER);
        tStock.add(tStockGraph, BorderLayout.CENTER);
        tStock.add(tLeftButtons, BorderLayout.WEST);
        tStock.add(sRightStock, BorderLayout.EAST);
    }

    private void portfolioTab() {
        // Portfolio tab
        tPortfolio = new JPanel(new GridBagLayout());

        Object[][] data = {{"", "", ""}};
        Object[][] data2 = {{"", "", "", ""}};
        String[] stockTableColumnsNames = new String[]{"Company", "Closing Price", "Stock Issued"};
        String[] transactionsTableColumnsNames = new String[]{"Action", "No of Stock", "Value", "Time"};
        String[] clientTableColumnsNames = new String[]{"Name", "Cash Holding (£)", "Total Client Worth"};
        String[] clientStockTableColumnsNames = new String[]{"Company", "Amount", "Worth"};

        Color bgColour = Color.BLACK;
        Color fgColour = Color.WHITE;
        Color headerBgColour = Color.DARK_GRAY;
        Color selectedColour = new Color(156, 0, 0, 199);

        tPortfolio.setBackground(bgColour);
        JTable tTableStock = setUpTable(data, stockTableColumnsNames, bgColour, fgColour, headerBgColour,
                selectedColour, true, false, true);
        JTable tTableTransactions = setUpTable(data2, transactionsTableColumnsNames, bgColour, fgColour, headerBgColour,
                selectedColour, true, false, true);
        JTable tClients = setUpTable(data, clientTableColumnsNames, bgColour, fgColour, headerBgColour,
                selectedColour, true, false, true);
        JTable tClientStock = setUpTable(data, clientStockTableColumnsNames, bgColour, fgColour, headerBgColour,
                selectedColour, true, false, true);

        tPortfolio.add(new JScrollPane(tTableStock), gbcConstraint(1, 1, 0, 0, 0.1, 0.1));
        tPortfolio.add(new JScrollPane(tTableTransactions), gbcConstraint(1, 1, 1, 0, 0.1, 0.1));
        tPortfolio.add(new JScrollPane(tClients), gbcConstraint(1, 1, 2, 0, 0.1, 0.1));
        tPortfolio.add(new JScrollPane(tClientStock), gbcConstraint(1, 1, 3, 0, 0.1, 0.1));

    }

    private JTable setUpTable(Object[][] data, String[] names, Color bg, Color fg, Color header, Color selected, Boolean fills, Boolean focus, Boolean opaque) {
        JTable table = new JTable(createTableModel(data, names));
        table.setBackground(bg);
        table.setForeground(fg);
        table.getTableHeader().setBackground(header);
        table.getTableHeader().setForeground(fg);
        table.setSelectionBackground(selected);
        table.setSelectionForeground(fg);
        table.setFillsViewportHeight(fills);
        table.setFocusable(focus);
        table.setOpaque(opaque);
        return table;
    }

    private DefaultTableModel createTableModel(Object[][] data, String[] colNames) {
        return new DefaultTableModel(data, colNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    /**replace the graph dataset and table with new dataset*/
    public void setDataset(XYDataset dataset) {
        stockMarketGraph.setDataset(dataset);
        DefaultTableModel model = (DefaultTableModel) stockTable.getModel();

        if (model.getRowCount() < dataset.getSeriesCount()) {
            model.setRowCount(0);
        }

        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            if (model.getRowCount() < dataset.getSeriesCount()) {
                double d = (double) dataset.getY(i, dataset.getItemCount(i) - 1);
                model.addRow(new Object[]{dataset.getSeriesKey(i), "Godmode", (double) Math.round(d * 100d) / 100d});
            } else {
                double d = (double) dataset.getY(i, dataset.getItemCount(i) - 1);
                model.setValueAt((double) Math.round(d * 100d) / 100d, i, 2);
            }
        }
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

//    private class SwitchGraph implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            System.out.println("Switch button pressed");
//            stockMarketGraph.switchGraph();
//        }
//    }

    private class TableRowListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int[] rowsNo = new int[stockTable.getSelectedRowCount()];
            for (int i = 0; i < stockTable.getSelectedRowCount(); i++) {
                rowsNo[i] = stockTable.getSelectedRows()[i];
            }
            if (rowsNo.length < 1) {
                stockMarketGraph.setSeriesVisible();
            } else {
                stockMarketGraph.setSeriesVisible(rowsNo);
            }
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
