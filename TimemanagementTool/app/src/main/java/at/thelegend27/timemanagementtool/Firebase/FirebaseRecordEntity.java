package at.thelegend27.timemanagementtool.Firebase;

/**
 * Created by toedtli2 on 25.05.2017.
 */

public class FirebaseRecordEntity {

    String userID;
    String date;
    String cinTime;
    String coutTime;

    public FirebaseRecordEntity() {
    }

    public FirebaseRecordEntity(String userID, String date, String cinTime, String coutTime) {
        this.userID = userID;
        this.date = date;
        this.cinTime = cinTime;
        this.coutTime = coutTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCinTime() {
        return cinTime;
    }

    public void setCinTime(String cinTime) {
        this.cinTime = cinTime;
    }

    public String getCoutTime() {
        return coutTime;
    }

    public void setCoutTime(String coutTime) {
        this.coutTime = coutTime;
    }
}

