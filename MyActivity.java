
import org.qtproject.qt.android.bindings.QtActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import java.lang.String;

import android.app.Activity;


public class MyActivity extends QtActivity // This is the class
{

    // Instantiation of this class
    private static MyActivity m_instance;
    public MyActivity() {
        m_instance = this;
    }

    public static int JAVA_Send_SMS(int n) {
        int Answer1;

        Log.d(QtApplication.QtTAG, "JAVA_Send_SMS() entered");
        My_Java_Procedures java_procedures = new My_Java_Procedures();
        Answer1 = java_procedures.sms_send(n);
        return Answer1;
    }

    public static int JAVA_Receive_SMS(int n) {
        int Answer2;

        Log.d(QtApplication.QtTAG, "JAVA_Receive_SMS() entered");
        My_Java_Procedures java_procedures = new My_Java_Procedures();
        Answer2 = java_procedures.sms_receive(n);
        return Answer2;
    }
}

