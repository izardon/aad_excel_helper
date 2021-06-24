import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class XLSXWriter {
    private Workbook workbook;
    List<Map<String, String>> xlsxData;

    public XLSXWriter() {
        workbook = new XSSFWorkbook();
    }

    public void write(String fileName, List<Map<String, String>> data) {
        sortData(data);
        processStateImmutableAnomaly(xlsxData);

        writeFile(fileName);
    }

    private void processStateImmutableAnomaly(List<Map<String, String>> data) {
        Sheet sheet = workbook.createSheet("StateImmutableAnomaly");
        List<String> headRowText;
        if(data.get(0).containsKey("Repeated Anomaly")) {
            headRowText = Arrays.asList("AppName", "Operation", "Level", "time", "Total Injected Operations", "Anomaly", "Repeated Anomaly", "Unexpected Anomaly");
        } else {
            headRowText = Arrays.asList("AppName", "Operation", "Level", "time", "Total Injected Operations", "Anomaly", "Repeat Anomalies", "Unexpected Anomaly");
        }
        createHeadRow(sheet, headRowText);
        int rowNum = 1;
        for(Map<String, String> rowText : data) {
            createRow(sheet, headRowText, rowText, rowNum);
            rowNum++;
        }
    }

    private void createHeadRow(Sheet sheet, List<String> headRowText) {
        Row row = sheet.createRow(0);
        int index = 0;
        for(String text : headRowText) {
            row.createCell(index).setCellValue(text);
            index++;
        }
    }

    private void createRow(Sheet sheet, List<String> headRowText, Map<String, String> rowText, int rowNum) {
        Row row = sheet.createRow(rowNum);
        int index = 0;
        for(String headText : headRowText) {
            row.createCell(index).setCellValue(rowText.get(headText));
            index++;
        }
    }

    private void writeFile(String fileName) {
        try {
            File xlsxFile = new File(fileName);
            workbook.write(new FileOutputStream(xlsxFile, false));
            System.out.println("File path: " + xlsxFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("An error occurred when create xlsx file.");
            e.printStackTrace();
        }
    }

    private void sortData(List<Map<String, String>> datas) {
        List<Map<String, String>> sortedData = new ArrayList<>();
        Set<String> appNames = new HashSet<>();
        Set<String> operations = new HashSet<>();

        datas.forEach(data -> {
            appNames.add(data.get("AppName"));
            operations.add(data.get("Operation"));
        });

        appNames.forEach(appName -> {
            operations.forEach(operation -> {
                List<Map<String, String>> sortDataBlock = new ArrayList<>(
                        datas.stream()
                                .filter(data -> data.get("AppName").equals(appName) && data.get("Operation").equals(operation))
                                .collect(Collectors.toList())
                );
                Collections.sort(sortDataBlock, levelComparator);
                sortedData.addAll(sortDataBlock);
            });
        });

        xlsxData = sortedData;
    }

    public Comparator<Map<String, String>> levelComparator = new Comparator<Map<String, String>>() {
        public int compare(Map<String, String> m1, Map<String, String> m2) {
            return m1.get("Level").compareTo(m2.get("Level"));
        }
    };

}
