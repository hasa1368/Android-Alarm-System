#ifndef SMSRECEIVER_H
#define SMSRECEIVER_H

#include <QObject>
#include <QTimer>

class SmsReceiver : public QObject
{
    Q_OBJECT
    Q_PROPERTY(QStringList smsMessagesList MEMBER m_smsMessagesList NOTIFY smsMessagesListChanged)

public:
    explicit SmsReceiver(QObject *parent = 0);

signals:
    void smsMessagesListChanged();

private:
    QStringList m_smsMessagesList;
    QTimer m_timer;
};

#endif // SMSRECEIVER_H
