package at.thelegend27.timemanagementtool;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by a1 on 20.05.17.
 */

public class  Task implements Parcelable{
    String author, date, taskname;
    int id;



    public Task(String author, String date, String taskname, int id) {
        this.author = author;
        this.date = date;
        this.taskname = taskname;
        this.id = id;
    }

    public Task() {
        author = "";
        date = "";
        taskname = "";
        id = 0;
    }

    public Task (Parcel in) {
        author = in.readString();
        date = in.readString();
        taskname = in.readString();
        id = in.readInt();

    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public Map<String, Object> toMap() {                   // convert object user in Map<> form
        HashMap<String, Object> result = new HashMap<>();
        result.put("author", author);
        result.put("date", date);
        result.put("taskname", taskname);
        result.put("id", id);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(date);
        dest.writeString(taskname);
        dest.writeInt(id);
    }



    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {

        return author;
    }

    public String getDate() {
        return date;
    }

    public String getTaskname() {
        return taskname;
    }

    public int getId() {

        return id;
    }
}
