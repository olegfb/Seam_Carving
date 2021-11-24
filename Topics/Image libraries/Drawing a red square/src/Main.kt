import java.awt.Color
import java.awt.image.BufferedImage
//import java.io.File
//import javax.imageio.ImageIO
//
//fun main() {
//    val imagefile = File("MyImage.png")
//    ImageIO.write(drawSquare(), "png", imagefile)
//}

fun drawSquare(): BufferedImage {
    val image = BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB)
    val graphic = image.createGraphics()
    graphic.color = Color.RED
    graphic.drawPolygon(intArrayOf(100, 400, 400, 100), intArrayOf(100, 100, 400, 400), 4)
    return  image
}