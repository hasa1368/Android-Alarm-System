// src/com/kdab/training/MyActivity.java
package com.mbruel.test;

import org.qtproject.qt.android.bindings.QtActivity;
import org.qtproject.qt.android.bindings.QtService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
// http://supertos.free.fr/supertos.php?page=1198
public class MyActivity extends QtActivity {

	private static final String SMS_DELIVERED = "SMS_DELIVERED";
	private static final String SMS_SENT      = "SMS_SENT";
	private static final int    SMS_MAX_SIZE  = 160;

	BroadcastReceiver smsSentReceiver;
	BroadcastReceiver smsDeliveredReceiver;

	public void popup(String msg) {
		Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
	}


	public void sendMessage(int msgId, String destMobile, String msg) {
		SmsManager sms = SmsManager.getDefault();

		if (msg.length() < SMS_MAX_SIZE)
		{
			Log.d("sendMessage", "sending single SMS using sendTextMessage API, msgId: " + msgId);

			Intent sentItent       = new Intent(SMS_SENT);
			Intent deliveredIntent = new Intent(SMS_DELIVERED);

			sentItent.putExtra("EXTRA_MESSAGE_ID", msgId);
			deliveredIntent.putExtra("EXTRA_MESSAGE_ID", msgId);

			PendingIntent sentPI      = PendingIntent.getBroadcast(this, msgId, sentItent, PendingIntent.FLAG_UPDATE_CURRENT);
			PendingIntent deliveredPI = PendingIntent.getBroadcast(this, msgId, deliveredIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        NativeFunctions.smsNbParts(msgId, 1);
			sms.sendTextMessage(destMobile, null, msg, sentPI, deliveredPI);
		}
		else
		{
			ArrayList<String> parts = sms.divideMessage(msg);
			int nbParts = parts.size();
			NativeFunctions.smsNbParts(msgId, nbParts);
			Log.d("sendMessage", "sending multiple parts SMS using sendMultipartTextMessage API (nb parts: "+nbParts+") msgId: " + msgId);

			ArrayList<PendingIntent> sentIntents     = new ArrayList<PendingIntent> ();
			ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent> ();
			for (int i = 0 ; i < parts.size() ; ++i)
			{
				Intent sentItent       = new Intent(SMS_SENT);
				Intent deliveredIntent = new Intent(SMS_DELIVERED);

				sentItent.putExtra("EXTRA_MESSAGE_ID", msgId);
				deliveredIntent.putExtra("EXTRA_MESSAGE_ID", msgId);

				int requestId = msgId*32 + i; // we suppose that there are less than 32 parts...
				sentIntents.add(PendingIntent.getBroadcast(this, requestId, sentItent, PendingIntent.FLAG_UPDATE_CURRENT));
				deliveryIntents.add(PendingIntent.getBroadcast(this, requestId, deliveredIntent, PendingIntent.FLAG_UPDATE_CURRENT));
			}
			sms.sendMultipartTextMessage(destMobile, null, parts, sentIntents, deliveryIntents);
		}
	}


	@Override
	public void onResume() {
		super.onResume();
		// BroadcastReceiver for SMS_SENT
		smsSentReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				int msgId = intent.getIntExtra("EXTRA_MESSAGE_ID", 0);
				String msg = "msg_"+msgId+": ";
//				Log.d("SMS_SENT", "BroadcastReceiver for SMS_SENT: "+msgId);
				NativeFunctions.smsSent(msgId, getResultCode());
				switch (getResultCode())
				{
					case Activity.RESULT_OK:
						msg   += "SMS SENT";
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						msg   += "Error sending SMS: GENERIC_FAILURE";
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						msg   += "Error sending SMS: NO_SERVICE";
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						msg   += "Error sending SMS: NULL_PDU";
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						msg   += "Error sending SMS: RADIO_OFF";
						break;
					default:
						msg   += "Error sending SMS: UNKNOW "+getResultCode();
						break;

				}
				Log.d("SMS_SENT", msg);
//				Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
			}
		};

		// BroadcastReceiver for SMS_DELIVERED
		smsDeliveredReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				int msgId = intent.getIntExtra("EXTRA_MESSAGE_ID", 0);
				String msg = "msg_"+msgId+": ";
//				Log.d("SMS_DELIVERED", "BroadcastReceiver for SMS_DELIVERED: "+msgId);
				NativeFunctions.smsDelivered(msgId, getResultCode());
				switch (getResultCode())
				{
					case Activity.RESULT_OK:
						msg   += "SMS DELIVERED";
						break;
					case Activity.RESULT_CANCELED:
						msg   += "Error Delivering SMS";
						break;
					default:
						msg   += "Error Delivering SMS (unknown: "+getResultCode()+")";
						break;
				}
				Log.d("SMS_DELIVERED", msg);
//				Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
			}
		};

		// register receivers
		registerReceiver(smsSentReceiver, new IntentFilter(SMS_SENT));
		registerReceiver(smsDeliveredReceiver, new IntentFilter(SMS_DELIVERED));

	}

	@Override
	public void onPause() {
		super.onPause();
		// unregister receivers
		unregisterReceiver(smsSentReceiver);
		unregisterReceiver(smsDeliveredReceiver);
        }
    private static final int SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onStart() {
        super.onStart();

        // Check if the app has the required permission to send SMS
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted
            sendSMS();
        }
    }

    // This method will handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send the SMS
                sendSMS();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to send SMS
    private void sendSMS() {
        // Use SmsManager or other relevant APIs to send an SMS here
    }
// Exposed method to request permission from Qt
  public void requestSmsPermission() {
      onStart(); // Call the actual permission handling
  }
}
