package com.ScytheStudio;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import java.util.List;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.app.PendingIntent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.media.AudioAttributes;

public class SmsReceiver extends BroadcastReceiver {
    public static List<String> messagesList = new ArrayList<String>();
    private static final String CHANNEL_ID = "SMS_NOTIFICATION_CHANNEL";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");

                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        String messageBody = smsMessage.getMessageBody();
                        String strMsgSrc = smsMessage.getOriginatingAddress();
                        String sender = smsMessage.getDisplayOriginatingAddress();
                        if(strMsgSrc.equals("+989107624656"))
                          {
                        messagesList.add(messageBody);
                        showNotification(context, sender, messageBody);
                        }
                    }
                }
            }
        }
    }

    public static String getLastMessage() {
      if  (!messagesList.isEmpty()) {
        return messagesList.remove(0);
      } else {
        return "";
      }
    }

private void showNotification(Context context, String sender, String messageBody) {
    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    // Define the URI of the sound file
    Uri alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/raw/alarm");

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel channel = new NotificationChannel(
            CHANNEL_ID,
            "SMS Notifications",
            NotificationManager.IMPORTANCE_HIGH
        );

        // Set custom sound for the notification channel
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build();

        channel.setSound(alarmSound, audioAttributes);
        notificationManager.createNotificationChannel(channel);
    }

    // Create an intent to open the Qt app
    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    PendingIntent pendingIntent = PendingIntent.getActivity(
        context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
    );

    Notification notification = new Notification.Builder(context, CHANNEL_ID)
        .setContentTitle("New SMS from " + sender)
        .setContentText(messageBody)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setSound(alarmSound)  // Set the custom sound
        .setContentIntent(pendingIntent) // Open app when clicked
        .setAutoCancel(true) // Dismiss notification after clicking
        .build();

    notificationManager.notify(1, notification);
}


}
