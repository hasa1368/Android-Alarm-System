#include "SmsReceiver.h"
#include <QDebug>
#include <QJniObject>
#include <QtCore/qjniobject.h>
#include <QtCore/qcoreapplication.h>
SmsReceiver::SmsReceiver(QObject *parent)
    : QObject{parent}
{
    connect(&m_timer, &QTimer::timeout, this, [this]() {
        qDebug() << "Checking new messages...";
        QString lastMessage =
            QJniObject::callStaticMethod<jstring>("com/ScytheStudio/SmsReceiver", "getLastMessage"
).toString();

        if  (!lastMessage.isEmpty()) {
            m_smsMessagesList.append(lastMessage);
            emit smsMessagesListChanged();
        }
    });

    m_timer.setInterval(1000);
    m_timer.start();
}
