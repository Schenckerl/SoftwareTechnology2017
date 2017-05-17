package at.thelegend27.timemanagementtool.Firebase;


public class FirebaseUserEntity {

    private String uId;

    private String email;

    private String name;

    private String password;

    public FirebaseUserEntity(){
    }

    public FirebaseUserEntity(String uId, String email, String name, String password) {
        this.uId = uId;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getuId() {
        return uId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() { return password; }
}
