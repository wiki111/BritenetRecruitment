package gui;

import database.DBConnector;
import database.DbConnectorFactory;
import readers.CSVReader;
import readers.Reader;
import readers.XMLReader;
import utils.Utils;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ApplicationGUI {

    static JFileChooser jFileChooser;
    static JFrame mainFrame;
    static JTextArea logArea;

    public void run(){
        mainFrame = new JFrame("Britenet Recruitment Task");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(300, 100);
        JPanel mainPanel = new JPanel();
        jFileChooser = new JFileChooser();
        JButton saveToDBBtn = new JButton("Save to DB");
        saveToDBBtn.addActionListener(new SaveToDBButtonListener());
        logArea = new JTextArea(10, 50);
        JScrollPane areaScrollPane = new JScrollPane(logArea);
        areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        logArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret)logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        mainPanel.add(areaScrollPane);
        mainPanel.add(saveToDBBtn);
        mainFrame.getContentPane().add(mainPanel);
        mainFrame.setVisible(true);
    }

    class SaveToDBButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int returnValue = ApplicationGUI.jFileChooser.showOpenDialog(mainFrame);

            if(returnValue == JFileChooser.APPROVE_OPTION){
                File file = jFileChooser.getSelectedFile();
                Reader reader;
                String extension = Utils.getExtension(file);
                switch (extension){
                    case Utils.txt:
                        reader = new CSVReader();
                        break;
                    case Utils.csv:
                        reader = new CSVReader();
                        break;
                    case Utils.xml:
                        reader = new XMLReader();
                        break;
                    default:
                        reader = null;
                }
                if(reader != null){
                    DBConnector connector = DbConnectorFactory.getDbConnector();
                    mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    connector.setCustomerPersistedListener(data -> logArea.append(data + "\n"));
                    logArea.append("Starting processing data... \n");
                    Thread t = new Thread(() -> reader.readAndSaveToDB(file, connector));
                    t.start();
                    mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    logArea.append("Data processing finished.");
                }else{
                    JOptionPane.showMessageDialog(ApplicationGUI.mainFrame,
                            "Unsupported file - please choose either txt, csv or xml file to process.",
                            "Unsupported file error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }


        }
    }
}
