#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <SmsReceiver.h>
#include <QQmlContext>
#include <SmsSender.h>
#include <QtWidgets/QMessageBox>
#include <QtWidgets/QApplication>
#include <QQuickWindow>
#include <QQuickStyle>

int main(int argc, char *argv[])
{
    QGuiApplication app(argc, argv);

    QQmlApplicationEngine engine;
    SmsReceiver smsReceiver;
    engine.rootContext()->setContextProperty("smsReceiver", &smsReceiver);
    SmsSender smsSender;
    engine.rootContext()->setContextProperty("smsSender", &smsSender);

    engine.addImportPath("qrc:/");
    const QUrl url(u"qrc:/main.qml"_qs);
    QObject::connect(&engine, &QQmlApplicationEngine::objectCreated,
        &app, [url](QObject *obj, const QUrl &objUrl) {
            if (!obj && url == objUrl)
                QCoreApplication::exit(-1);
        }, Qt::QueuedConnection);
    engine.load(url);

    return app.exec();
}
