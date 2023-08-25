package demo.model;

public enum UserProfile {

    OWNER("O"),
    ADMINISTRATOR("A"),
    WRITER("W"),
    READER("R")
    ;
    

    private final String profile;

    private UserProfile(String profile) {
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
    }
    
}
