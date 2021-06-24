import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AADLogReader {
    private List<String> lines;

    public void read(String path) {
        lines = new ArrayList<>();
        int lineNumber = 1;

        try {
            File log = new File(path);
            Scanner myReader = new Scanner(log);

            while (lineNumber < 15 && myReader.hasNextLine()) {
                String line = myReader.nextLine();
                lines.add(line);
                lineNumber ++;

                // 舊log有14行，新log有13行，最後一行都是time
                if(line.contains("time")) {
                    break;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("An error occurred when AADLogReader open file.");
        }
    }

    public List<String> getResult() {
        return lines;
    }
}
