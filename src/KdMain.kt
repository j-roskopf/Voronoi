
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

class KdMain {
    companion object {
        private var cells = 6000

        private const val fileName = "input/space.jpg"

        private val random = Random()

        @JvmStatic
        fun main(args: Array<String>) {

            val file = File(fileName)

            val stopwatch = Stopwatch()
            if(file.exists()) {
                val start = stopwatch.elapsedTime()

                val img = ImageIO.read(file)

                val points = mutableListOf<DoubleArray>()

                for(i in 0 until cells) {
                    val xCord = random.nextInt(img.width)
                    val yCord = random.nextInt(img.height)
                    points.add(doubleArrayOf(xCord.toDouble(), yCord.toDouble()))
                }

                val kdTree = KdTree(points, HyperRect(doubleArrayOf(0.0,0.0), doubleArrayOf(img.width.toDouble(), img.height.toDouble())))

                val toWrite = BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_RGB)

                for (x in 0 until img.width) {
                    for (y in 0 until img.height) {
                        val pairForColor = kdTree.nearestNeighbor(doubleArrayOf(x.toDouble(),y.toDouble()))
                        toWrite.setRGB(x, y, img.getRGB(pairForColor.nearest!!.first().toInt(), pairForColor.nearest[1].toInt()))
                    }
                }

                val outputFile = File("out/space.png")
                ImageIO.write(toWrite, "png", outputFile)

                print("Finished! ${stopwatch.elapsedTime() - start}")
            } else  {
                println("File doesn't exist!")
            }
        }
    }
}