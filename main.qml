import QtQuick
import QtQuick.Window
import QtQuick.Controls
import QtMultimedia
import QtQuick.Controls.Material
import QtLocation
import QtPositioning
import Qt5Compat.GraphicalEffects
import QtQuick.Layouts
import QtQuick.Dialogs

 Window {
      id:mainwindow
      width: 640
      height: 480
      visible: true
      title: qsTr("SmsReceiver")
      color: "#10403f"

      function addMarker(latitude, longitude)
      {
          var Component = Qt.createComponent("qrc:/Marker.qml")
          var item = Component.createObject(mapwin, {
                                                coordinate: QtPositioning.coordinate(latitude, longitude)
                                            })
          map1.addMapItem(item)
      }
      function addMarker2(latitude, longitude)
      {
          var Component = Qt.createComponent("qrc:/Marker.qml")
          var item = Component.createObject(mapwin, {
                                                coordinate: QtPositioning.coordinate(latitude, longitude)
                                            })
          map2.addMapItem(item)
      }

      function mapcenter(x, y)
      {
          map1.center=QtPositioning.coordinate(x,y)
      }

      function mapcenter2(x, y)
      {
          map2.center=QtPositioning.coordinate(x,y)
      }

      ListView {
        id: messagesListView

        anchors {
          fill: parent
          margins: 10
        }

        model: smsReceiver.smsMessagesList
        spacing: 5

        delegate:
            Rectangle {
          id: messageDelegate

          visible: true
          width: messagesListView.width
          height: Math.max(50, messageText.height + 20)
          radius: 15
          color:"blue"

      Text {
          id: finaltext2
       property variant  stringList:finaltext.text.split(',');
          anchors {
            left: parent.left
            right: parent.right
            verticalCenter: parent.verticalCenter
            margins: 10
          }
          font.pixelSize: 18
          color: "white"
          wrapMode: Text.WordWrap
          horizontalAlignment: Text.AlignHCenter
          verticalAlignment: Text.AlignVCenter
          text:' سلام لطفا این رویداد را بررسی بفرمایید
             '+finaltext.text

          Component.onCompleted:{addMarker(stringList[2], stringList[3])
          mapcenter(stringList[2],stringList[3])
              addMarker2(stringList[2], stringList[3])
                        mapcenter2(stringList[2],stringList[3])

          }
      Text {
        id: finaltext
        anchors {
          left: parent.left
          right: parent.right
          verticalCenter: parent.verticalCenter
          margins: 10
        }
        text:messageText.text
        elide: Text.ElideRight
        width: parent.width - 30
        font.pixelSize: 18
        color: "white"
        wrapMode: Text.WordWrap
        horizontalAlignment: Text.AlignHCenter
        verticalAlignment: Text.AlignVCenter
        visible: false
       Text {
        id: messageText
        visible: false
        anchors {
          left: parent.left
          right: parent.right
          verticalCenter: parent.verticalCenter
          margins: 10
             }
        text: modelData
        font.pixelSize: 18
        color: "white"
        wrapMode: Text.WordWrap
        horizontalAlignment: Text.AlignHCenter
        verticalAlignment: Text.AlignVCenter
            }
         }
    }
    }
  }
  Button {
      text: "Exit"
      x:280
      y:650
      Material.background: Material.Blue
      onPressed:  Qt.callLater(Qt.quit)
  }
  SoundEffect {
        id: playSound
      source: "qrc:/Sound/Alarm01.wav"
      loops: 5
      volume: 200
      }
      MouseArea {
       id: playArea
      Component.onCompleted: playSound.play()
      }
  Button {
      width:250
      height:100
      anchors.centerIn: parent
      text: "Event Veiw"
      onPressed:{
        mapwin.visible=true
          playSound.stop()

      }
      Material.background: Material.Green
      font.bold: true
      font.pixelSize: 36
  }
  Window{
      id:mapwin
      visible: false
      width: 640
      height: 480
      title: qsTr("Event Map")
      color: "#10403f"


      Plugin{
          id: gmap
          name: "osm"
          PluginParameter { name: "osm.mapping.providersrepository.address"; value: Qt.resolvedUrl("./providers/map/") }
          PluginParameter { name: "osm.mapping.cache.directory"; value: "cache" }
          PluginParameter { name: "osm.mapping.cache.disk.size"; value: 0 }
      }

      Plugin{
          id: gsat
          name: "osm"
          PluginParameter { name: "osm.mapping.providersrepository.address"; value: Qt.resolvedUrl("./providers/satellite/") }
          PluginParameter { name: "osm.mapping.cache.directory"; value: "cache" }
          PluginParameter { name: "osm.mapping.cache.disk.size"; value: 0 }
      }

      Map{
          id: map1
          anchors.fill: layers
          plugin: gsat
          center: QtPositioning.coordinate(36.709392, 52.687782)
          tilt: map2.tilt
          bearing: map2.bearing
          zoomLevel: 17
          Component.onCompleted:addMarker(36.709392, 52.687782)
           property geoCoordinate startCentroid

                   PinchHandler {
                       id: pinch
                       target: null
                       onActiveChanged: if (active) {
                           map1.startCentroid = map1.toCoordinate(pinch.centroid.position, false)
                       }
                       onScaleChanged: (delta) => {
                           map1.zoomLevel += Math.log2(delta)
                           map1.alignCoordinateToPoint(map1.startCentroid, pinch.centroid.position)
                       }
                       onRotationChanged: (delta) => {
                           map1.bearing -= delta
                           map1.alignCoordinateToPoint(map1.startCentroid, pinch.centroid.position)
                       }
                       grabPermissions: PointerHandler.TakeOverForbidden
                   }
                   WheelHandler {
                       id: wheel
                       acceptedDevices: Qt.platform.pluginName === "cocoa" || Qt.platform.pluginName === "wayland"
                                        ? PointerDevice.Mouse | PointerDevice.TouchPad
                                        : PointerDevice.Mouse
                       rotationScale: 1/120
                       property: "zoomLevel"
                   }
                   DragHandler {
                       id: drag
                       target: null
                       onTranslationChanged: (delta) => map1.pan(-delta.x, -delta.y)
                   }
                   Shortcut {
                       enabled: map1.zoomLevel < map1.maximumZoomLevel
                       sequence: StandardKey.ZoomIn
                       onActivated: map1.zoomLevel = Math.round(map1.zoomLevel + 1)
                   }
                   Shortcut {
                       enabled: map1.zoomLevel > map1.minimumZoomLevel
                       sequence: StandardKey.ZoomOut
                       onActivated: map1.zoomLevel = Math.round(map1.zoomLevel - 1)
                   }



          Binding on anchors.fill{
              when: mouseArea1.clicked
              value: mouseArea1.swapped ? map1.parent : layers
          }

          Binding on z{
              when: mouseArea1.clicked
              value: mouseArea1.swapped ? 0 : 1
          }

          Binding on copyrightsVisible{
              when: mouseArea1.clicked
              value: mouseArea1.swapped
          }

      }


      Map{
          id: map2
          anchors.fill: parent
          plugin: gmap
          center: QtPositioning.coordinate(36.709392, 52.687782)
          tilt: map1.tilt
          bearing: map1.bearing
          Component.onCompleted:addMarker2(36.709392, 52.687782)
           zoomLevel: 17
           property geoCoordinate startCentroid

                   PinchHandler {
                       id: pinch2
                       target: null
                       onActiveChanged: if (active) {
                           map2.startCentroid = map2.toCoordinate(pinch.centroid.position, false)
                       }
                       onScaleChanged: (delta) => {
                           map2.zoomLevel += Math.log2(delta)
                           map2.alignCoordinateToPoint(map2.startCentroid, pinch.centroid.position)
                       }
                       onRotationChanged: (delta) => {
                           map2.bearing -= delta
                           map2.alignCoordinateToPoint(map2.startCentroid, pinch.centroid.position)
                       }
                       grabPermissions: PointerHandler.TakeOverForbidden
                   }
                   WheelHandler {
                       id: wheel2
                       acceptedDevices: Qt.platform.pluginName === "cocoa" || Qt.platform.pluginName === "wayland"
                                        ? PointerDevice.Mouse | PointerDevice.TouchPad
                                        : PointerDevice.Mouse
                       rotationScale: 1/120
                       property: "zoomLevel"
                   }
                   DragHandler {
                       id: drag2
                       target: null
                       onTranslationChanged: (delta) => map2.pan(-delta.x, -delta.y)
                   }
                   Shortcut {
                       enabled: map2.zoomLevel < map2.maximumZoomLevel
                       sequence: StandardKey.ZoomIn
                       onActivated: map2.zoomLevel = Math.round(map2.zoomLevel + 1)
                   }
                   Shortcut {
                       enabled: map2.zoomLevel > map2.minimumZoomLevel
                       sequence: StandardKey.ZoomOut
                       onActivated: map2.zoomLevel = Math.round(map2.zoomLevel - 1)
                   }
          Binding on anchors.fill{
              when: mouseArea1.clicked
              value: !mouseArea1.swapped ? map2.parent : layers
          }

          Binding on z{
              when: mouseArea1.clicked
              value: !mouseArea1.swapped ? 0 : 1
          }

          Binding on copyrightsVisible{
              when: mouseArea1.clicked
              value: !mouseArea1.swapped
          }

      }

      Item {
          id: layers
          width: 100
          height: width
          x: 15
          y: parent.height - 115
          z: 100
          property real zoomLevel: 0
          property real zoomFactor: 4
          property int zoomAnimationDuration: 1000
          property int panAnimationDuration: 1000
          property real targetZoomLevel: 20




          MouseArea{
              id: mouseArea1
              anchors.fill: parent
              property bool swapped: false
              onClicked: {
                  swapped = !swapped
                  if(swapped){
                      map1.zoomLevel = Math.max(map2.zoomLevel, layers.zoomLevel)
                      map2.zoomLevel = Math.max(map1.zoomLevel, layers.zoomLevel) - layers.zoomFactor
                  }
                  else{
                      map2.zoomLevel = Math.max(map1.zoomLevel, layers.zoomLevel)
                      map1.zoomLevel = Math.max(map2.zoomLevel, layers.zoomLevel) - layers.zoomFactor
                  }
              }
          }

      }

      Button {
          id: reply
          y:620
          x:200
          width: 150
          font.bold: true
          Material.background: Material.Blue
          text:"Reply"
          onPressed: {replywin.visible=true

          }

      }


  }
  Window{
      id:replywin
      color:"#10403f"
      visible: false
      TextEdit {
          id: msgnumber
          text: qsTr("Destination phone number")
          x:200
          y:200
          visible: false
      }
      Button{
          x:50
          y:2
          width: 100
          text:"Send"
          font.bold: true
          onPressed:  smsSender.sendText(msgnumber.text,txtmsg.text)
          Material.background: Material.Blue
       }

      Button {
          text: "Exit"
          x:280
          y:2
          Material.background: Material.Blue
          onPressed:  Qt.callLater(Qt.quit)
      }
      TextArea {
          id:txtmsg
          x:5
          y:100
          width: 350
          height:500
          color: "black"
          Material.background:"red"
          placeholderText: "Report writing..."
          font.pixelSize: 16
          font.bold: true
          background: Rectangle {
                     radius: 10
                     implicitWidth: 100
                     implicitHeight: 24
                     border.color: "white"
                     border.width: 1
                 }
      }

  }

}

