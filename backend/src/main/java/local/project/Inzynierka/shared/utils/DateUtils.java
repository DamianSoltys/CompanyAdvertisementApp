package local.project.Inzynierka.shared.utils;

import java.sql.Timestamp;
import java.util.Calendar;

public class DateUtils {

    private DateUtils() {}

    public static Timestamp getNowTimestamp(){
       return new Timestamp(Calendar.getInstance().getTime().getTime());
    }
}
