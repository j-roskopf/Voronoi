package voronoi

data class Point(internal var x: Double, internal var y: Double) : Comparable<Point> {

    override fun compareTo(other: Point): Int {
        return when {
            this.y == other.y -> when {
                this.x == other.x -> 0
                this.x > other.x -> 1
                else -> -1
            }
            this.y > other.y -> 1
            else -> -1
        }
    }

    override fun toString(): String {
        return "($x, $y)"
    }

}