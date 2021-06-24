import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AADLogReader {
    private List<String> lines;
    private boolean isValidLog;

    public void read(String path) {
        lines = new ArrayList<>();
        isValidLog = false;

        try {
            File log = new File(path);
            Scanner myReader = new Scanner(log);

            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                if(line.equals("-------------------------------------")) {
                    isValidLog = true;
                    break;
                }

                lines.add(line);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("An error occurred when AADLogReader open file.");
        }
    }

    public List<String> getResult() {
        return isValidLog? lines : null;
    }
}
