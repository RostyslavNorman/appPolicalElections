package models;

public enum ElectionType{
    GENERAL("General Election"),
    LOCAL("Local Election"),
    EUROPEAN("European Election"),
    PRESIDENTIAL("Presidential Election");

    private final String displayName;
    ElectionType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
    @Override
    public String toString(){
        return displayName;
    }
}
