package gui;

import database.DBConnector;
import database.DbConnectorFactory;
import readers.Reader;
import readers.ReaderFactory;
import utils.Utils;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class ApplicationGUI {

    static JFrame mainFrame;
    static JTextArea logArea;
    static JFileChooser jFileChooser;
    static JScrollPane areaScrollPane;
    static JButton saveToDBBtn;

    public void run(){
        mainFrame = new JFrame("Britenet Recruitment Task");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();

        saveToDBBtn = new JButton("Save to DB");
        saveToDBBtn.addActionListener(new SaveToDBButtonListener());

        jFileChooser = new JFileChooser();

        logArea = new JTextArea(10, 50);
        logArea.setEditable(false);

        areaScrollPane = new JScrollPane(logArea);
        areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setVisible(false);

        DefaultCaret caret = (DefaultCaret)logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        mainPanel.add(areaScrollPane);
        mainPanel.add(saveToDBBtn);

        mainFrame.getContentPane().add(mainPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private SwingWorker getWorkerToProcessFile(Reader reader, File file){
        return new SwingWorker<Boolean, String>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                //Prepare UI
                areaScrollPane.setVisible(true);
                saveToDBBtn.setEnabled(false);
                mainFrame.pack();

                //Prepare connection to database
                DBConnector connector = DbConnectorFactory.getDbConnector();

                //Send information to user
                connector.setCustomerPersistedListener(data -> publish(data));
                logArea.append("Starting processing data... \n");

                //Read from file and save to DB
                reader.readAndSaveToDB(file, connector);

                saveToDBBtn.setEnabled(true);

                return true;
            }

            @Override
            protected void process(List<String> chunks) {
                for(String data : chunks){
                    logArea.append("Processed object : " + data + "\n");
                }
            }

            @Override
            protected void done() {
                boolean status = false;
                try {
                    status = get();
                }catch (Exception e){
                    showErrorDialog(e.getMessage());
                }
                if(status){
                    logArea.append("Finished processing succesfully.");
                }else{
                    String errorMsg = "Process finished with errors. Processing failed.";
                    showErrorDialog(errorMsg);
                    logArea.append(errorMsg);
                }
            }
        };
    }

    class SaveToDBButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int returnValue = ApplicationGUI.jFileChooser.showOpenDialog(mainFrame);
            if(returnValue == JFileChooser.APPROVE_OPTION){
                File file = jFileChooser.getSelectedFile();
                String extension = Utils.getExtension(file);
                Reader reader = ReaderFactory.getReaderForFileType(extension);
                if(reader != null){
                    SwingWorker swingWorker = getWorkerToProcessFile(reader, file);
                    swingWorker.execute();
                }else{
                    showUnsupportedFileDialog();
                }
            }
        }
    }

    private void showUnsupportedFileDialog(){
        JOptionPane.showMessageDialog(ApplicationGUI.mainFrame,
                "Unsupported file - please choose either txt, csv or xml file to process.",
                "Unsupported file error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showErrorDialog(String message){
        JOptionPane.showMessageDialog(ApplicationGUI.mainFrame,
                "Error occured : " + message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

}
