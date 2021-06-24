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
        createSheet(AADSheetName.STATE_IMMUTABLE_ANOMALY.getSheetName(), xlsxData);
        createSheet(AADSheetName.STATE_IMMUTABLE_CRASH.getSheetName(), xlsxData);
        createSheet(AADSheetName.EVENT_CHANGING_ANOMALY.getSheetName(), xlsxData);
        writeFile(fileName);
    }

    private void createSheet(String sheetName, List<Map<String, String>> data) {
        Sheet sheet = workbook.createSheet(sheetName);
        List<String> headRowText;
        headRowText = createSheetHeadRow(sheetName);
        
        setHeadRowText(sheet, headRowText);
        int rowNum = 1;
        for(Map<String, String> rowText : data) {
            setRowText(sheet, headRowText, rowText, rowNum);
            rowNum++;
        }
    }

    private List<String> createSheetHeadRow(String sheetName) {
        List<String> headRowText = null;

        if(sheetName.equals(AADSheetName.STATE_IMMUTABLE_ANOMALY.getSheetName())) {
            headRowText = Arrays.asList("AppName", "Operation", "Level", "time", "Total Injected Operations", "Unique Anomaly");
        } else if(sheetName.equals(AADSheetName.STATE_IMMUTABLE_CRASH.getSheetName())) {
            headRowText = Arrays.asList("AppName", "Operation", "Level", "time", "Total TestRun", "Total Injected Operations", "Crash", "Repeated Crash", "Unique Crash");
        } else if(sheetName.equals(AADSheetName.EVENT_CHANGING_ANOMALY.getSheetName())) {
            headRowText = Arrays.asList("AppName", "Operation", "Level", "time", "Total TestRun", "Total Injected Operations", "Crash", "Repeated Crash", "Unique Crash");
        }
        return headRowText;
    }

    private void setHeadRowText(Sheet sheet, List<String> headRowText) {
        Row row = sheet.createRow(0);
        int index = 0;
        for(String text : headRowText) {
            row.createCell(index).setCellValue(text);
            index++;
        }
    }

    private void setRowText(Sheet sheet, List<String> headRowText, Map<String, String> rowText, int rowNum) {
        if(Arrays.asList(AADSheetName.STATE_IMMUTABLE_ANOMALY.getSheetName(), AADSheetName.STATE_IMMUTABLE_CRASH.getSheetName()).contains(sheet.getSheetName())) {
            if (!Arrays.asList("RotateOperation","ExitAndReenterOperation").contains(rowText.get("Operation"))) {
                return;
            }
        } else if(Arrays.asList(AADSheetName.EVENT_CHANGING_ANOMALY.getSheetName()).contains(sheet.getSheetName())) {
            if (!Arrays.asList("EmptyStringOperation","InputZeroOperation", "RandomAsciiOperation", "RandomUTFOperation", "RandomLongStringOperation").contains(rowText.get("Operation"))) {
                return;
            }
        }

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

        appNames.forEach(appName -> operations.forEach(operation -> {
            List<Map<String, String>> sortDataBlock = datas.stream()
                    .filter(data -> data.get("AppName").equals(appName) && data.get("Operation").equals(operation)).sorted(levelComparator).collect(Collectors.toList());
            sortedData.addAll(sortDataBlock);
        }));

        xlsxData = sortedData;
    }

    public Comparator<Map<String, String>> levelComparator = Comparator.comparing(m -> m.get("Level"));
}
