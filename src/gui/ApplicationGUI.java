package gui;

import database.DbConnectorFactory;
import readers.CSVReader;
import readers.Reader;
import readers.XMLReader;
import utils.Utils;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ApplicationGUI {

    static JFileChooser jFileChooser;
    static JFrame mainFrame;

    public void run(){
        mainFrame = new JFrame("Britenet Recruitment Task");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(300, 100);
        JPanel mainPanel = new JPanel();
        jFileChooser = new JFileChooser();
        JButton saveToDBBtn = new JButton("Save to DB");
        saveToDBBtn.addActionListener(new SaveToDBButtonListener());
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
                    reader.readAndSaveToDB(file, DbConnectorFactory.getDbConnector());
                }else{
                    JOptionPane.showMessageDialog(ApplicationGUI.mainFrame,
                            "Unsupported file - please choose either csv or xml file to process.",
                            "Unsupported file error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }


        }
    }
}
