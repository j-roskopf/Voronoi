package voronoi

// represents the beach line
// can either be a site that is the center of a parabola
// or can be a vertex that bisects two sites
class Parabola {

    internal var type: Int = 0
    internal lateinit var point: Point // if is focus
    internal var edge: Edge? = null // if is vertex
    internal var event: Event? = null // a parabola with a focus can disappear in a circle event

    internal var parent: Parabola? = null
    internal var child_left: Parabola? = null
    internal var child_right: Parabola? = null

    constructor() {
        type = IS_VERTEX
    }

    constructor(p: Point) {
        point = p
        type = IS_FOCUS
    }

    fun setLeftChild(p: Parabola) {
        child_left = p
        p.parent = this
    }

    fun setRightChild(p: Parabola) {
        child_right = p
        p.parent = this
    }

    override fun toString(): String {
        return if (type == IS_FOCUS) {
            "Focus at $point"
        } else {
            "Vertex/Edge beginning at " + edge!!.start
        }
    }

    companion object {

        var IS_FOCUS = 0
        var IS_VERTEX = 1

        // returns the closest left site (focus of parabola)
        fun getLeft(p: Parabola): Parabola? {
            return getLeftChild(getLeftParent(p))
        }

        // returns closest right site (focus of parabola)
        fun getRight(p: Parabola): Parabola? {
            return getRightChild(getRightParent(p))
        }

        // returns the closest parent on the left
        fun getLeftParent(p: Parabola?): Parabola? {
            var parent: Parabola? = p?.parent ?: return null
            var last = p
            while (parent!!.child_left === last) {
                if (parent!!.parent == null) return null
                last = parent
                parent = parent.parent
            }
            return parent
        }

        // returns the closest parent on the right
        fun getRightParent(p: Parabola?): Parabola? {
            var parent: Parabola? = p?.parent ?: return null
            var last = p
            while (parent!!.child_right === last) {
                if (parent!!.parent == null) return null
                last = parent
                parent = parent.parent
            }
            return parent
        }

        // returns closest site (focus of another parabola) to the left
        fun getLeftChild(p: Parabola?): Parabola? {
            if (p == null) return null
            var child = p.child_left
            while (child?.type == IS_VERTEX) child = child.child_right
            return child
        }

        // returns closest site (focus of another parabola) to the right
        fun getRightChild(p: Parabola?): Parabola? {
            if (p == null) return null
            var child = p.child_right
            while (child?.type == IS_VERTEX) child = child.child_left
            return child
        }
    }


}