package at.thelegend27.timemanagementtool;

/**
 * Created by markusfriedl on 16/05/2017.
 */

public class UserProfile {
    private String header;

    private String profileContent;

    public UserProfile(String header, String profileContent) {
        this.header = header;
        this.profileContent = profileContent;
    }

    public String getHeader() {
        return header;
    }

    public String getProfileContent() {
        return profileContent;
    }
}
