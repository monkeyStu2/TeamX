package SMS;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @author Robert Garner
 */
public class SimViewNew extends JDialog {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;

    private JTextField initialFileAddress, externalFileAddress;
    private JButton bInitFileFind, bExFileFind, bExecute, bCancel;
    private JLabel lInitial, lExternal;

    private JPanel pOptions, pInitialFile, pExternalFile;

    private String initialDataPath, externalEventPath;

    public SimViewNew() {
        setTitle("New Simulation");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setUp();
        initialDataPath = "";
        externalEventPath = "";
        setModal(true);  // hold simulation until this dialog is closed
        setVisible(true);
    }

    public void setUp() {
        setLayout(new GridLayout(3, 1));

        pInitialFile = new JPanel(new GridBagLayout());
        pExternalFile = new JPanel(new GridBagLayout());
        pOptions = new JPanel(new GridBagLayout());

        // adding components and it actions to InitialFile panel
        lInitial = new JLabel("Initial Data");
        pInitialFile.add(lInitial, SimView.gbcConstraint(2, 1, 0, 0, 0, 0));  // label
        initialFileAddress = new JTextField("Enter file address here");
        pInitialFile.add(initialFileAddress, SimView.gbcConstraint(3, 1, 0, 1, 1, 0));  // textfield
        bInitFileFind = new JButton("Find");
        bInitFileFind.addActionListener(e -> initialFileAddress.setText(fileChooser()));
        pInitialFile.add(bInitFileFind, SimView.gbcConstraint(1, 1, 3, 1, 0, 0));  // button

        // adding components and it actions to ExternalFile panel
        lExternal = new JLabel("External Event Data");
        pExternalFile.add(lExternal, SimView.gbcConstraint(2, 1, 0, 0, 0, 0));  // label
        externalFileAddress = new JTextField("Enter file address here");
        pExternalFile.add(externalFileAddress, SimView.gbcConstraint(3, 1, 0, 1, 1, 0));  // textfield
        bExFileFind = new JButton("Find");
        bExFileFind.addActionListener(e -> externalFileAddress.setText(fileChooser()));
        pExternalFile.add(bExFileFind, SimView.gbcConstraint(1, 1, 3, 1, 0, 0));  // button

        // adding components and it actions to Options panel
        bExecute = new JButton("Ok");
        bExecute.addActionListener(e -> {
            if (fileCheck(initialFileAddress.getText())) {
                if (fileCheck(externalFileAddress.getText())) {
                    initialDataPath = initialFileAddress.getText();
                    externalEventPath = externalFileAddress.getText();
                    dispose(); // if both file passed, start building new simulation with these files
                }
            }
        });
        pOptions.add(bExecute, SimView.gbcConstraint(2, 1, 0, 0, 0, 0));
        bCancel = new JButton("Cancel");
        bCancel.addActionListener(e -> dispose());  // cancel the progress
        pOptions.add(bCancel, SimView.gbcConstraint(2, 1, 2, 0, 0, 0));

        add(pInitialFile);
        add(pExternalFile);
        add(pOptions);

    }

    /**Open file explorer, select a file and return path as String or null if cancelled*/
    private String fileChooser() {
        JFileChooser fc = new JFileChooser();
        if (0 == fc.showOpenDialog(this)) {
            File file = fc.getSelectedFile();
            return file.getPath();
        } else {
            System.out.println("File chooser cancelled");
        }
        return null;
    }

    /**Check if the file exist and is a valid .xlsx file for creating new simulation*/
    private boolean fileCheck(String filePath) {
        File f = new File(filePath);
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
        if (!extension.equals("xlsx")) {
            JOptionPane.showMessageDialog(this, "Invalid .xlsx file", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!f.isFile()) {
            JOptionPane.showMessageDialog(this, "File does not exist", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            return true;
        }
        return false;
    }

    public String getInitialData() {
        return initialDataPath;
    }

    public String getExternalData() {
        return externalEventPath;
    }
}
