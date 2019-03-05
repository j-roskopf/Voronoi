package voronoi


import java.awt.Polygon
import java.awt.image.BufferedImage
import java.util.*


class Voronoi(private val sites: List<Point>, val img: BufferedImage) {

    var edges = ArrayList<Edge>()
    private lateinit var events: PriorityQueue<Event> // priority queue represents sweep line
    var root: Parabola? = null // binary search tree represents beach line

    // size of StdDraw window
    var width = 1.0
    var height = 1.0

    var ycurr: Double = 0.toDouble() // current y-coord of sweep line

    init {
        generateVoronoi()
    }

    private fun generateVoronoi() {
        events = PriorityQueue()
        for (p in sites) {
            events.add(Event(p, Event.SITE_EVENT))
        }

        // process events (sweep line)
        var count = 0
        while (!events.isEmpty()) {
            //System.out.println();
            val e = events.remove()
            ycurr = e.p.y



            count++
            if (e.type == Event.SITE_EVENT) {
                //System.out.println(count + ". SITE_EVENT " + e.p);
                handleSite(e.p)
            } else {
                //System.out.println(count + ". CIRCLE_EVENT " + e.p);
                handleCircle(e)
            }
        }

        ycurr = width + height


        val shouldNull = endEdges(root) // close off any dangling edges
        if(shouldNull) {
            root = null
        }

        // get rid of those crazy inifinte lines
        for (e in edges) {
            if (e.neighbor != null && e.neighbor?.end != null) {
                e.start = e.neighbor!!.end!!
                e.neighbor = null
            }
        }

    }

    // end all unfinished edges
    private fun endEdges(p: Parabola?): Boolean {
        if (p?.type == Parabola.IS_FOCUS) {
            return true
        }

        val x = getXofEdge(p!!)
        val y = p.edge!!.slope * x + p.edge!!.yint
        p.edge!!.end = Point(x, y)
        //edges.add(p.edge!!)

        var shouldEnd = endEdges(p.child_left)
        if(shouldEnd) {
            p.child_left = null
        }
        shouldEnd = endEdges(p.child_right)
        if(shouldEnd) {
            p.child_right = null
        }
        return true
    }

    // processes site event
    private fun handleSite(p: Point) {
        // base case
        if (root == null) {
            root = Parabola(p)
            return
        }

        // find parabola on beach line right above p
        val par = getParabolaByX(p.x)
        if (par.event != null) {
            events.remove(par.event)
            par.event = null
        }

        // create new dangling edge; bisects parabola focus and p
        val start = Point(p.x, getY(par.point, p.x))
        val el = Edge(start, par.point, p)
        val er = Edge(start, p, par.point)
        el.neighbor = er
        er.neighbor = el
        par.edge = el
        par.type = Parabola.IS_VERTEX

        // replace original parabola par with p0, p1, p2
        val p0 = Parabola(par.point)
        val p1 = Parabola(p)
        val p2 = Parabola(par.point)

        par.setLeftChild(p0)
        par.setRightChild(Parabola())
        par.child_right!!.edge = er
        par.child_right!!.setLeftChild(p1)
        par.child_right!!.setRightChild(p2)

        checkCircleEvent(p0)
        checkCircleEvent(p2)
    }

    // process circle event
    private fun handleCircle(e: Event) {

        // find p0, p1, p2 that generate this event from left to right
        var p1 = e.arc
        val xl = Parabola.getLeftParent(p1)
        val xr = Parabola.getRightParent(p1)
        val p0 = Parabola.getLeftChild(xl)
        val p2 = Parabola.getRightChild(xr)

        // remove associated events since the points will be altered
        if (p0?.event != null) {
            events.remove(p0.event)
            p0.event = null
        }
        if (p2?.event != null) {
            events.remove(p2.event)
            p2.event = null
        }

        val p = Point(e.p.x, getY(p1!!.point, e.p.x)) // new vertex

        // end edges!
        xl?.edge!!.end = p
        xr?.edge!!.end = p

        edges.add(xl.edge!!)
        edges.add(xr.edge!!)

        // start new bisector (edge) from this vertex on which ever original edge is higher in tree
        var higher = Parabola()
        var par = p1
        while (par != root) {
            par = par!!.parent
            if (par == xl) higher = xl
            if (par == xr) higher = xr
        }
        higher.edge = Edge(p, p0?.point!!, p2?.point!!)

        // delete p1 and parent (boundary edge) from beach line
        val grandParent = p1.parent!!.parent
        if (p1.parent!!.child_left == p1) {
            if (grandParent!!.child_left == p1.parent) grandParent.setLeftChild(p1.parent!!.child_right!!)
            if (grandParent.child_right == p1.parent) grandParent.setRightChild(p1.parent!!.child_right!!)
        } else {
            if (grandParent!!.child_left == p1.parent) grandParent.setLeftChild(p1.parent!!.child_left!!)
            if (grandParent.child_right == p1.parent) grandParent.setRightChild(p1.parent!!.child_left!!)
        }

        val op = p1.point
        p1.parent = null
        p1 = null

        checkCircleEvent(p0)
        checkCircleEvent(p2)
    }

    // adds circle event if foci a, b, c lie on the same circle
    private fun checkCircleEvent(b: Parabola?) {
        if(b == null) return

        val lp = Parabola.getLeftParent(b)
        val rp = Parabola.getRightParent(b)

        if (lp == null || rp == null) return

        val a = Parabola.getLeftChild(lp)
        val c = Parabola.getRightChild(rp)

        if (a == null || c == null || a.point == c.point) return

        if (ccw(a.point, b.point, c.point) != 1) return

        // edges will intersect to form a vertex for a circle event
        val start = getEdgeIntersection(lp.edge, rp.edge) ?: return

        // compute radius
        val dx = b.point.x - start.x
        val dy = b.point.y - start.y
        val d = Math.sqrt(dx * dx + dy * dy)
        if (start.y + d < ycurr) return  // must be after sweep line

        val ep = Point(start.x, start.y + d)
        //System.out.println("added circle event "+ ep);

        // add circle event
        val e = Event(ep, Event.CIRCLE_EVENT, d)
        e.arc = b
        b.event = e
        events.add(e)
    }

    fun ccw(a: Point, b: Point, c: Point): Int {
        val area2 = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)
        return if (area2 < 0)
            -1
        else if (area2 > 0)
            1
        else
            0
    }


    // returns intersection of the lines of with vectors a and b
    private fun getEdgeIntersection(a: Edge?, b: Edge?): Point? {
        if(a == null || b == null) return null

        if (b.slope == a.slope && b.yint != a.yint) return null

        val x = (b.yint - a.yint) / (a.slope - b.slope)
        val y = a.slope * x + a.yint

        return Point(x, y)
    }

    // returns current x-coordinate of an unfinished edge
    private fun getXofEdge(par: Parabola): Double {
        //find intersection of two parabolas

        val left = Parabola.getLeftChild(par)
        val right = Parabola.getRightChild(par)

        val p = left?.point
        val r = right?.point

        if(p == null || r == null) {
            return 0.0
        }

        val dp = 2 * (p.y - ycurr)
        val a1 = 1 / dp
        val b1 = -2 * p.x / dp
        val c1 = (p.x * p.x + p.y * p.y - ycurr * ycurr) / dp

        val dp2 = 2 * (r.y - ycurr)
        val a2 = 1 / dp2
        val b2 = -2 * r.x / dp2
        val c2 = (r.x * r.x + r.y * r.y - ycurr * ycurr) / dp2

        val a = a1 - a2
        val b = b1 - b2
        val c = c1 - c2

        val disc = b * b - 4.0 * a * c
        val x1 = (-b + Math.sqrt(disc)) / (2 * a)
        val x2 = (-b - Math.sqrt(disc)) / (2 * a)

        val ry: Double
        if (p.y > r.y)
            ry = Math.max(x1, x2)
        else
            ry = Math.min(x1, x2)

        return ry
    }

    // returns parabola above this x coordinate in the beach line
    private fun getParabolaByX(xx: Double): Parabola {
        var par: Parabola? = root
        var x = 0.0
        while (par!!.type == Parabola.IS_VERTEX) {
            x = getXofEdge(par)
            if (x > xx)
                par = par.child_left
            else
                par = par.child_right
        }
        return par
    }

    // find corresponding y-coordinate to x on parabola with focus p
    private fun getY(p: Point, x: Double): Double {
        // determine equation for parabola around focus p
        val dp = 2 * (p.y - ycurr)
        val a1 = 1 / dp
        val b1 = -2 * p.x / dp
        val c1 = (p.x * p.x + p.y * p.y - ycurr * ycurr) / dp
        return a1 * x * x + b1 * x + c1
    }
}