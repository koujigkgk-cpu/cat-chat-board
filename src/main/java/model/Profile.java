package model;
import java.io.Serializable;

public class Profile implements Serializable {
    private String userId;
    private String name;
    private String address;
    private String personality;
    private String introduction;
    private String iconFilename; // 追加

    public Profile() {}
    public Profile(String userId, String name, String address, String personality, String introduction, String iconFilename) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.personality = personality;
        this.introduction = introduction;
        this.iconFilename = iconFilename;
    }

    // Getter/Setter
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPersonality() { return personality; }
    public String getIntroduction() { return introduction; }
    public String getIconFilename() { return iconFilename; }
    public void setIconFilename(String iconFilename) { this.iconFilename = iconFilename; }
}