import java.awt.Color
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

class BruteMain {
    companion object {
        private var cells = 1000

        private val random = Random()

        @JvmStatic
        fun main(args: Array<String>) {
            val fileName = "input/sf.jpg"
            val file = File(fileName)
            val stopwatch = Stopwatch()
            if(file.exists()) {
                val start = stopwatch.elapsedTime()
                val img = ImageIO.read(file)

                val px = IntArray(cells) { random.nextInt(img.width) }
                val py = IntArray(cells) { random.nextInt(img.height) }

                val toWrite = BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_RGB)

                for (x in 0 until img.width) {
                    for (y in 0 until img.height) {
                        var n = 0
                        for (i in 0 until cells) {
                            if (distSq(px[i], x, py[i], y) < distSq(px[n], x, py[n], y)) n = i
                        }
                        toWrite.setRGB(x, y, img.getRGB(px[n], py[n]))
                    }
                }

                val g = toWrite.createGraphics()
                g.color = Color.BLACK
                for (i in 0 until cells) {
                    g.fill(Ellipse2D.Double(px[i] - 2.5, py[i] - 2.5, 5.0, 5.0))
                }

                val outputFile = File("out/brute.png")
                ImageIO.write(toWrite, "png", outputFile)

                print("Finished! ${stopwatch.elapsedTime() - start}")
            } else  {
                println("File doesn't exist!")
            }
        }

        private fun distSq(x1: Int, x2: Int, y1: Int, y2: Int): Int {
            val x = x1 - x2
            val y = y1 - y2
            return x * x + y * y
        }
    }
}