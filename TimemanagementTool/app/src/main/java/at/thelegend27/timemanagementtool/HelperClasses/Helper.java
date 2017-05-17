package at.thelegend27.timemanagementtool.HelperClasses;

/**
 * Created by markusfriedl on 16/05/2017.
 */

public class Helper {
    public static final String NAME = "Name";

    public static final String EMAIL = "Email";


    public static boolean isValidEmail(String email){
        if(email.contains("@")){
            return true;
        }
        return false;
    }
}
