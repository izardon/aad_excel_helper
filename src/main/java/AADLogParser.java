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
            } else if (key.equals("Repeat Anomalies")) {// AAD 新舊 Log 的內容文字不一樣
                key = "Repeated Anomaly";
            } else if (key.equals("Repeat Crashes")) {
                key = "Repeated Crash";
            }

            result.put(key, value);
        });

        if(result.containsKey("Repeated Anomaly")) {
            String uniqueAnomaly = String.valueOf(Integer.parseInt(result.get("Anomaly")) - Integer.parseInt(result.get("Repeated Anomaly")));
            result.put("Unique Anomaly", uniqueAnomaly);
        }
        if(result.containsKey("Repeated Crash")) {
            String uniqueCrash = String.valueOf(Integer.parseInt(result.get("Crash")) - Integer.parseInt(result.get("Repeated Crash")));
            result.put("Unique Crash", uniqueCrash);
        }

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
