package com.realappraiser.gharvalue.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.O)
public class LogOutScheduler implements LogutSessionScheduler {
    private final Context context;
    private static  AlarmManager alarmManager;

    private static PendingIntent pendingIntent;

    public LogOutScheduler(Context context) {
        this.context = context;
        alarmManager = context.getSystemService(AlarmManager.class);
    }

    @Override
    public void schedule() {
        Intent intent = new Intent(context, LogOutReceiver.class);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        /*calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);*/
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 0);

         pendingIntent  =  PendingIntent.getBroadcast(
                context,
                101,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );
    }

    public static void cancelAlarm(){
        if(alarmManager!=null && pendingIntent != null ){
            alarmManager.cancel(pendingIntent);
        }
    }
}