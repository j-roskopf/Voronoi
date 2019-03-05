package voronoi

import com.sun.org.apache.xpath.internal.operations.Bool
import javafx.scene.shape.Circle

data class Edge(internal var start: Point, var site_left: Point, var site_right: Point) {
    internal var end: Point? = null
    internal var direction: Point // edge is really a vector normal to left and right points
    internal var neighbor: Edge? = null // the same edge, but pointing in the opposite direction
    var mid: Point
    internal var slope: Double = 0.toDouble()
    internal var yint: Double = 0.toDouble()

    init {
        direction = Point(site_right.y - site_left.y, -(site_right.x - site_left.x))
        end = null
        slope = (site_right.x - site_left.x) / (site_left.y - site_right.y)
        mid = Point((site_right.x + site_left.x) / 2, (site_left.y + site_right.y) / 2)
        yint = mid.y - slope * mid.x
    }

    override fun toString(): String {
        return "$start, $end"
    }


}