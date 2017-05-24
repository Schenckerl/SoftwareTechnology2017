package at.thelegend27.timemanagementtool.HelperClasses;

/**
 * Created by Eileen on 24.05.2017.
 */

public class StoredData {
    private static StoredData instance = null;
    private String startTime;
    private String[] breakTimes;
    private boolean arrived;
    protected StoredData() {
        // Exists only to defeat instantiation.
    }
    public static StoredData getInstance() {
        if(instance == null) {
            instance = new StoredData();
        }
        return instance;
    }

    public void setStartTime(String time)
    {
        startTime = time;
    }
    public String getStartTime(String time)
    {
        return startTime;
    }
    public boolean getArrivedStatus()
    {
        return arrived;
    }
    public void getArrivedStatus(boolean status)
    {
        arrived = status;
    }
}