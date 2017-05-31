package at.thelegend27.timemanagementtool;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by a1 on 20.05.17.
 */

public class  Task implements Parcelable{
    String author_id, date, task_name, task_description, user_id, task_id;


    public Task(String author_id, String date, String task_name, String task_description, String user_id, String task_id) {
        this.author_id = author_id;
        this.date = date;
        this.task_name = task_name;
        this.task_description = task_description;
        this.user_id = user_id;
        this.task_id = task_id;
    }

    public Task() {
    }

    public Task (Parcel in) {
        author_id = in.readString();
        date = in.readString();
        task_name = in.readString();
        task_description = in.readString();
        user_id = in.readString();
        task_id = in.readString();
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
        result.put("author_id", author_id);
        result.put("date", date);
        result.put("task_name", task_name);
        result.put("user_id", user_id);
        result.put("task_description", task_description);
        result.put("task_id", task_id);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author_id);
        dest.writeString(date);
        dest.writeString(task_name);
        dest.writeString(task_description);
        dest.writeString(user_id);
        dest.writeString(task_id);
    }



    public String getAuthor_id() {
        return author_id;
    }

    public String getDate() {
        return date;
    }

    public String getTask_name() {
        return task_name;
    }

    public String getTask_description() {
        return task_description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_id() {
        return task_id;
    }
}
