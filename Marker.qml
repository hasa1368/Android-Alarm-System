import QtQuick
import QtLocation

MapQuickItem{
    id: marker
    anchorPoint.x: marker.width / 4
    anchorPoint.y: marker.height
    sourceItem: Image{
        id: icon
        source: "qrc:/Icon/marker.png"
        sourceSize.width: 40
        sourceSize.height: 40
    }
}
