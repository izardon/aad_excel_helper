public enum AADSheetName {
    STATE_IMMUTABLE_ANOMALY("StateImmutableAnomaly"),
    STATE_IMMUTABLE_CRASH("StateImmutableCrash"),
    EVENT_CHANGING_ANOMALY("EventChangingCrash");

    private String sheetName;

    AADSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

}
