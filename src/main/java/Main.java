import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    final static String XLSX_FILENAME = "aad_log.xlsx";
    final static String ROOT_FOLDER = "D:\\LAB-PROJECT\\aad\\output\\test";

    public static void main(String args[]) {
        AADLogFinder aadLogFinder = new AADLogFinder();
        AADLogReader aadLogReader = new AADLogReader();
        AADLogParser aadLogParser = new AADLogParser();

        File rootFolder = new File(ROOT_FOLDER);
        aadLogFinder.findLogFilesForFolder(rootFolder);
        List<String> logPaths = aadLogFinder.getResult();
        List<Map<String, String>> xlsxData = new ArrayList<>();
        logPaths.forEach(path -> {
            aadLogReader.read(path);
            if(null == aadLogReader.getResult()) {
                return;
            }
            aadLogParser.parse(path, aadLogReader.getResult());
            xlsxData.add(aadLogParser.getResult());
        });

        XLSXWriter xlsxWriter = new XLSXWriter();
        xlsxWriter.write(XLSX_FILENAME, xlsxData);
    }
}