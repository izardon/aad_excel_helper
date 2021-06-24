import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AADLogFinder {
    private List<String> logfilePaths = new ArrayList<>();
    boolean isAADOutputFolder = false;

    public void findLogFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                if(isAADOutputFolder) {
                    continue;
                }
                // AAD output folder example: 20210623_201405_trigger_L4_RandomAsciiOperation
                if(fileEntry.getName().split("_").length >= 5) {
                    isAADOutputFolder = true;
                }
                findLogFilesForFolder(fileEntry);
            } else {
                if(isAADOutputFolder && fileEntry.getName().equals("Log.txt")) {
                    isAADOutputFolder = false;
                    logfilePaths.add(fileEntry.getAbsolutePath());
                }
            }
        }
    }

    public List<String> getResult() {
        return logfilePaths;
    }
}
