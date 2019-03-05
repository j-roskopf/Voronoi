package voronoi


// an event is either a site or circle event for the sweep line to process
class Event(internal var p: Point, internal var type: Int, var diameter: Double = 0.0) : Comparable<Event> {
    internal var arc: Parabola? = null // only if circle event

    init {
        arc = null
    }

    override fun compareTo(other: Event): Int {
        return this.p.compareTo(other.p)
    }

    companion object {

        // a site event is when the point is a site
        var SITE_EVENT = 0

        // a circle event is when the point is a vertex of the voronoi diagram/parabolas
        var CIRCLE_EVENT = 1
    }

}