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

    public static UserProfile profileFromString(String profile){
        if( profile != null ){
            for (UserProfile userProfile : UserProfile.values()) {
                if(userProfile.getProfile().equals(profile)) return userProfile;
            }
        }
        return null;
    }

    public boolean isAllowedFor(UserProfile profileToCompare){
        if(this == UserProfile.ADMINISTRATOR){
            return true;
        }

        if(this == UserProfile.OWNER && profileToCompare != UserProfile.ADMINISTRATOR){
            return true;
        }

        if(this == UserProfile.WRITER && (profileToCompare == UserProfile.WRITER ||  profileToCompare == UserProfile.READER)){
            return true;
        }

        return this == UserProfile.READER && profileToCompare == UserProfile.READER;
    }
    
}
