QT += quick core-private gui qml quickcontrols2 location widgets

SOURCES += \
        SmsReceiver.cpp \
        SmsSender.cpp \
        main.cpp

resources.files = main.qml 
resources.prefix = /$${TARGET}
RESOURCES += resources \
    BppFa.qrc \
    BppTable.qrc \
    qml.qrc


# Additional import path used to resolve QML modules in Qt Creator's code model
QML_IMPORT_PATH =

# Additional import path used to resolve QML modules just for Qt Quick Designer
QML_DESIGNER_IMPORT_PATH =

# Default rules for deployment.
qnx: target.path = /tmp/$${TARGET}/bin
else: unix:!android: target.path = /opt/$${TARGET}/bin
!isEmpty(target.path): INSTALLS += target

contains(ANDROID_TARGET_ARCH,arm64-v8a) {
    ANDROID_PACKAGE_SOURCE_DIR = \
        $$PWD/android
}

DISTFILES += \
    Icon/Icon1.png \
    Icon/icon.png \
    Icon/marker.png \
    Line.qml \
    Marker.qml \
    MyActivity.java \
    My_Java_Procedures.java \
    Sound/Alarm01.wav \
    Sound/Alarm02.wav \
    Sound/Alarm03.wav \
    Sound/Alarm04.wav \
    Sound/Alarm05.wav \
    Sound/Alarm06.wav \
    Sound/Alarm07.wav \
    Sound/Alarm08.wav \
    Sound/Alarm09.wav \
    Sound/Alarm10.wav \
    android/AndroidManifest.xml \
    android/AndroidManifest.xml \
    android/build.gradle \
    android/build.gradle \
    android/gradle.properties \
    android/gradle.properties \
    android/gradle/wrapper/gradle-wrapper.jar \
    android/gradle/wrapper/gradle-wrapper.jar \
    android/gradle/wrapper/gradle-wrapper.jar \
    android/gradle/wrapper/gradle-wrapper.jar \
    android/gradle/wrapper/gradle-wrapper.jar \
    android/gradle/wrapper/gradle-wrapper.properties \
    android/gradle/wrapper/gradle-wrapper.properties \
    android/gradle/wrapper/gradle-wrapper.properties \
    android/gradle/wrapper/gradle-wrapper.properties \
    android/gradle/wrapper/gradle-wrapper.properties \
    android/gradlew \
    android/gradlew \
    android/gradlew \
    android/gradlew \
    android/gradlew \
    android/gradlew.bat \
    android/gradlew.bat \
    android/gradlew.bat \
    android/gradlew.bat \
    android/gradlew.bat \
    android/res/values/libs.xml \
    android/res/values/libs.xml \
    android/res/values/libs.xml \
    android/res/values/libs.xml \
    android/res/values/libs.xml \
    android/res/xml/qtprovider_paths.xml \
    android/res/xml/qtprovider_paths.xml \
    android/res/xml/qtprovider_paths.xml \
    android/res/xml/qtprovider_paths.xml \
    android/res/xml/qtprovider_paths.xml \
    android/src/com/ScytheStudio/MyActivity.java \
    android/src/com/ScytheStudio/NativeFunctions.java \
    android/src/com/ScytheStudio/SmsReceiver.java

HEADERS += \
    SmsReceiver.h \
    SmsSender.h
    INCLUDEPATH += $$PWD
    DEPENDPATH += $$PWD

contains(ANDROID_TARGET_ARCH,armeabi-v7a) {
    ANDROID_PACKAGE_SOURCE_DIR = \
        $$PWD/android
}
