package org.qtproject.qt.android.bindings;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import android.content.Intent;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;

import android.telephony.SmsManager; // For sending sms

public class My_Java_Procedures {

    // Call Instantiation
    // **********************************
    public My_Java_Procedures My_Java_Procedures_instance;
    {
        My_Java_Procedures_instance = this;
    }
    // **********************************

    public int sms_send(int n) {
        // Remember to add your permisions to Android Manifest file
        System.out.println("Eureka Send");

        // http://pulse7.net/android/send-sms-message-android/
        // Use import android.telephony.SmsManager;

        // Initialize SmsManager Object
        SmsManager smsManager = SmsManager.getDefault();

        // Send Message using method of SmsManager object
        smsManager.sendTextMessage("your cell number", null,"Message Body", null, null);
        return n+10;
    }


    //public int sms_receive(Context context,Intent intent,int n)
    public int sms_receive(int n) {
        System.out.println("Debug message Receive SMS");

        // http://stackoverflow.com/questions/19856338/using-new-telephony-content-provider-to-read-sms
        Activity act = new Activity();
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        Cursor cursor = act.getApplicationContext().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        System.out.println("Debug 2");
        //ContentResolver resolver = getContentResolver();
        //Cursor cursor = resolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

// http://www.learn-android-easily.com/2013/04/accessing-inbox-in-android.html
//    String senderNumber=cursor.getString(cursor.getColumnIndex("address"));
//    String smsBody=cursor.getString(cursor.getColumnIndex("body"));
//    String dateTime=cursor.getString(cursor.getColumnIndex("date"));

        if (cursor.moveToFirst()) { // must check the result to prevent exception
        System.out.println("Debug 3");
            do {
               String msgData = "";
               for(int idx=0;idx<cursor.getColumnCount();idx++) {
                   msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                   System.out.println(msgData);
               }
               // use msgData
            } while (cursor.moveToNext());
        }
        else {
           // empty box, no SMS
        }

    return n+100;
    }
} // End Class

