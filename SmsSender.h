#ifndef SMSSENDER_H
#define SMSSENDER_H
#include <QObject>
#include <QMap>
class SmsSender : public QObject
{
public:
   // ~SmsSender();
    Q_OBJECT
   Q_INVOKABLE void sendText(const QString &destMobile, const QString &msg);

   //  SmsSender();
};
#endif // SMSSENDER_H
