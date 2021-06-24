import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Main {
    final static String XLSX_FILENAME = "aad_log.xlsx";
    final static String ROOT_FOLDER = "D:\\LAB-PROJECT\\110_aad_experiment\\after0623";
//    private static Properties properties = new Properties();

    public static void main(String args[]) {
//        loadPropertiesFile();
//        readProperties();

        AADLogFinder aadLogFinder = new AADLogFinder();
        AADLogReader aadLogReader = new AADLogReader();
        AADLogParser aadLogParser = new AADLogParser();

        File rootFolder = new File(ROOT_FOLDER);
        aadLogFinder.findLogFilesForFolder(rootFolder);
        List<String> logPaths = aadLogFinder.getResult();
        List<Map<String, String>> xlsxData = new ArrayList<>();
        logPaths.forEach(path -> {
            aadLogReader.read(path);
            aadLogParser.parse(path, aadLogReader.getResult());
            xlsxData.add(aadLogParser.getResult());
        });

        XLSXWriter xlsxWriter = new XLSXWriter();
        xlsxWriter.write(XLSX_FILENAME, xlsxData);
    }

//    private static void loadPropertiesFile(){
//        InputStream iStream = null;
//        try {
//            // Loading properties file from the path (relative path given here)
//            iStream = new FileInputStream("src/main/resources/app.properties");
//            properties.load(iStream);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }finally {
//            try {
//                if(iStream != null){
//                    iStream.close();
//                }
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * Method to read the properties from a
//     * loaded property file
//     */
//    private static void readProperties(){
//        System.out.println("XLSX_FILENAME - " + properties.getProperty("XLSX_FILENAME"));
//        System.out.println("ROOT_FOLDER - " + properties.getProperty("ROOT_FOLDER"));
//    }
}