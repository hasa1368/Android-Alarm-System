#include "SmsSender.h"
#include <QDebug>
#include <QJniObject>
#include <QJniEnvironment>
#include <QtCore/private/qandroidextras_p.h>
#include <QtCore/qjniobject.h>
#include <QtCore/qcoreapplication.h>

void SmsSender::sendText(const QString &destMobile, const QString &msg){

    QJniObject activity = QJniObject::callStaticObjectMethod("org/qtproject/qt/android/QtNative",
                                                             "activity",
                                                             "()Landroid/app/Activity;");
    if (activity.isValid()){
        //get the default SmsManager
        QJniObject mySmsManager = QJniObject::callStaticObjectMethod("android/telephony/SmsManager",
                                                                     "getDefault",
                                                                     "()Landroid/telephony/SmsManager;");

        if (mySmsManager.isValid()) {
            // get phone number & text from UI and convert to Java String
            QJniObject myPhoneNumber = QJniObject::fromString(destMobile);
            QJniObject myTextMessage = QJniObject::fromString(msg);
            QJniObject scAddress = nullptr;
            //QAndroidJniObject sentIntent = NULL;
            //QAndroidJniObject deliveryIntent = NULL;

            // call the java function:
            // public void SmsManager.sendTextMessage(String destinationAddress,
            //                                        String scAddress, String text,
            //                                        PendingIntent sentIntent, PendingIntent deliveryIntent)
            // see: http://developer.android.com/reference/android/telephony/SmsManager.html

            mySmsManager.callMethod<void>("sendTextMessage",
                                          "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/app/PendingIntent;Landroid/app/PendingIntent;)V",
                                          myPhoneNumber.object<jstring>(),
                                          scAddress.object<jstring>(),
                                          myTextMessage.object<jstring>(), NULL, NULL );
            mySmsManager.callMethod<void>("requestSmsPermission");
        }

    }


    // If you have a specific method in your Java Activity to request SMS permission, call it

}

