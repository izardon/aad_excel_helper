import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AADLogParser {
    private Map<String, String> result;

    public void parse(String path, List<String> logContent) {
        result = new HashMap<>();
        parsePath(path);

        logContent.forEach(line -> {
            String key = line.split(":")[0].trim();
            String value = line.split(":")[1].trim();
            if(key.equals("time")) {
                if(value.contains("min")) {
                    value = value.split("min")[0].trim();
                } else if(value.contains("h")) {
                    value = String.valueOf(Integer.parseInt(value.split("h")[0].trim()) * 60);
                }
            }
            result.put(key, value);
        });
    }

    private void parsePath(String path) {
        String folderName = path.split("\\\\")[path.split("\\\\").length - 2];
        String appName = folderName.split("_")[2];
        String operation = folderName.split("_")[folderName.split("_").length - 1];
        String level = String.valueOf(folderName.split("_")[3].charAt(1));
        if(folderName.split("_")[4].equals("cross")) {
            level = level.concat(".5");
        }

        result.put("AppName", appName);
        result.put("Operation", operation);
        result.put("Level", level);
    }

    public Map<String, String> getResult() {
        return result;
    }
}
