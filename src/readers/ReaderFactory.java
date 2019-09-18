package readers;

import utils.Utils;

public class ReaderFactory {
    public static Reader getReaderForFileType(String fileExtension){
        Reader reader;
        switch (fileExtension){
            case Utils.txt:
            case Utils.csv:
                reader = new CSVReader();
                break;
            case Utils.xml:
                reader = new XMLReader();
                break;
            default:
                reader = null;
        }
        return reader;
    }
}
