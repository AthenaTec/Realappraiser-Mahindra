package com.realappraiser.gharvalue.alarm;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.O)
public class LogOutScheduler implements LogOutSessionScheduler {
    private static Context context;
    private static AlarmManager alarmManager;

    private static PendingIntent pendingIntent;

    public LogOutScheduler(Context mContext) {
        context = mContext;
        alarmManager = context.getSystemService(AlarmManager.class);
    }

    @Override
    public void schedule() {
        Intent intent = new Intent(context, LogOutReceiver.class);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        /*calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 0);*/

        pendingIntent = PendingIntent.getBroadcast(
                context,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | FLAG_IMMUTABLE
        );

//        alarmManager.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(),
//                pendingIntent
//        );

//        // Check and request permission if needed
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            if (!alarmManager.canScheduleExactAlarms()) {
//                // Navigate the user to the settings screen to grant the permission
//                Intent permissionIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
//                context.startActivity(permissionIntent);
//            }else{
//                // Schedule the exact alarm with a try-catch block
//                try {
//                    alarmManager.setExactAndAllowWhileIdle(
//                            AlarmManager.RTC_WAKEUP,
//                            calendar.getTimeInMillis(),
//                            pendingIntent
//                    );
//                } catch (SecurityException e) {
//                    e.printStackTrace();
//                    // Notify the user or log the error
//                }
//            }
//        }else{
//
//
//        }

        try {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        } catch (SecurityException e) {
            e.printStackTrace();
            // Notify the user or log the error
        }


    }

//    private void showPermissionDialog() {
//        new AlertDialog.Builder(context)
//                .setTitle("Permission Required")
//                .setMessage("To schedule exact alarms, the app needs permission to schedule exact alarms. Please grant this permission in the settings.")
//                .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent permissionIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
//                        context.startActivity(permissionIntent);
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .setCancelable(false)
//                .show();
//    }

    public static void cancelAlarm() {
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

}
