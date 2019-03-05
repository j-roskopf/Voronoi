import voronoi.Edge
import voronoi.Point
import voronoi.Voronoi
import java.awt.Color
import java.awt.Polygon
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import java.util.ArrayList

class VoronoiImageMain {
    companion object {
        private val points = ArrayList<Point>()
        private const val cells = 10000

        @JvmStatic
        fun main(args: Array<String>) {
            val fileName = "input/sf.jpg"

            val file = File(fileName)
            val stopwatch = Stopwatch()

            if (file.exists()) {
                val start = stopwatch.elapsedTime()

                val img = ImageIO.read(file)
                val toWrite = BufferedImage(img.width, img.height, BufferedImage.TYPE_4BYTE_ABGR)


                val gen = Random()

                for (i in 0 until cells) {
                    val x = gen.nextDouble() * img.width
                    val y = gen.nextDouble() * img.height
                    val point = Point(x, y)
                    points.add(point)
                }

                val diagram = Voronoi(points, img)

               val g = toWrite.createGraphics()
                g.color = Color.BLACK

                //draw points
                points.forEach { point  ->
                    g.color = Color(img.getRGB(point.x.toInt(), point.y.toInt()))
                    g.fill(Ellipse2D.Double(point.x - 2.5, point.y - 2.5, 5.0, 5.0))
                }

                //draw edges
                for (e in diagram.edges)  {
                    if(e.start.x.toInt() > 0 && e.start.x.toInt() < img.width &&
                        e.start.y.toInt() > 0 && e.start.y.toInt() < img.height) {
                        g.color = Color(img.getRGB(e.start.x.toInt(), e.start.y.toInt()))
                    }

                    g.drawLine(e.start.x.toInt(), e.start.y.toInt(), e.end!!.x.toInt(), e.end!!.y.toInt())
                }

                val outputFile = File("out/voronoi.png")
                ImageIO.write(toWrite, "png", outputFile)

                print("Finished! ${stopwatch.elapsedTime() - start}")
            }
        }
    }
}