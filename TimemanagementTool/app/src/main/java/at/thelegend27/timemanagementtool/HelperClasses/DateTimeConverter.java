package at.thelegend27.timemanagementtool.HelperClasses;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by toedtli2 on 03.06.2017.
 */

public class DateTimeConverter {

    String actualDate;
    String actualTime;



    public String getActualDate() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        actualDate = df.format(new Date());

        return actualDate;
    }

    public String getActualTime() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        actualTime = df.format(new Date());

        return actualTime;
    }
}
