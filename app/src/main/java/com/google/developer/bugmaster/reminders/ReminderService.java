package com.google.developer.bugmaster.reminders;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.developer.bugmaster.QuizActivity;
import com.google.developer.bugmaster.R;
import com.google.developer.bugmaster.data.Insect;
import com.google.developer.bugmaster.data.InsectContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.google.developer.bugmaster.QuizActivity.EXTRA_ANSWER;
import static com.google.developer.bugmaster.QuizActivity.EXTRA_INSECTS;
import static com.google.developer.bugmaster.data.InsectContract.MAIN_INSECTS_PROJECTION;


public class ReminderService extends IntentService {

    private static final String TAG = ReminderService.class.getSimpleName();
    private ArrayList<Insect> mRandomInsectList = new ArrayList<>();

    private static final int NOTIFICATION_ID = 42;

    public ReminderService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Quiz reminder event triggered");

        //Present a notification to the user
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        Uri forecastQueryUri = InsectContract.InsectEntry.CONTENT_URI;
        Cursor cursor = getContentResolver().query(forecastQueryUri, MAIN_INSECTS_PROJECTION, null, null, null);
        ArrayList<Insect> tempList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Insect insect = new Insect(cursor);
                tempList.add(insect);
            } while (cursor.moveToNext());
            Collections.shuffle(tempList);
            mRandomInsectList = tempList;

            //Create action intent
            Intent action = new Intent(this, QuizActivity.class);
            //TODO: Add data elements to quiz launch
            action.putParcelableArrayListExtra(EXTRA_INSECTS, mRandomInsectList);
            action.putExtra(EXTRA_ANSWER, mRandomInsectList.get(new Random().nextInt(mRandomInsectList.size())));

            PendingIntent operation =
                    PendingIntent.getActivity(this, 0, action, PendingIntent.FLAG_CANCEL_CURRENT);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String CHANNEL_ID = "my_channel_01";// The id of the channel.
                Notification note = new Notification.Builder(this)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notification_text))
                        .setSmallIcon(R.drawable.ic_bug_empty)
                        .setContentIntent(operation)
                        .setChannelId(CHANNEL_ID)
                        .setAutoCancel(true)
                        .build();
                manager.notify(NOTIFICATION_ID, note);
            } else {
                Notification note = new NotificationCompat.Builder(this)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notification_text))
                        .setSmallIcon(R.drawable.ic_bug_empty)
                        .setContentIntent(operation)
                        .setAutoCancel(true)
                        .build();
                manager.notify(NOTIFICATION_ID, note);
            }


        } else {
            mRandomInsectList.clear();
        }

        AlarmReceiver.scheduleAlarm(this);
    }
}
