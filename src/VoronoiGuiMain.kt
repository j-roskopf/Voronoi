import voronoi.Point
import voronoi.Voronoi
import java.awt.image.BufferedImage
import java.util.*
import java.util.ArrayList

class VoronoiGuiMain {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val cells = 5000

            val s = Stopwatch()

            val points = ArrayList<Point>()

            val gen = Random()

            for (i in 0 until cells) {
                val x = gen.nextDouble()
                val y = gen.nextDouble()
                points.add(Point(x, y))
            }

            val start = s.elapsedTime()
            val diagram = Voronoi(points, BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR))
            val stop = s.elapsedTime()

            println(stop - start)

            // draw results
            StdDraw.setPenRadius(.005)
            for (p in points) {
                StdDraw.point(p.x, p.y)
            }

            StdDraw.setPenRadius(.002)
            for (e in diagram.edges) {
                StdDraw.line(e.start.x, e.start.y, e.end!!.x, e.end!!.y)
            }
        }
    }
}