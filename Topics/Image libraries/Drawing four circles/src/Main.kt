import java.awt.Color
import java.awt.image.BufferedImage
//import java.io.File
//import javax.imageio.ImageIO
//
//fun main() {
//    val imagefile = File("MyImage.png")
//    ImageIO.write(drawCircles(), "png", imagefile)
//}

fun drawCircles(): BufferedImage {
    val image = BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB)
    val graphic = image.createGraphics()
    graphic.color = Color.RED
    graphic.drawOval(50, 50, 100, 100)
    graphic.color = Color.YELLOW
    graphic.drawOval(50, 75, 100, 100)
    graphic.color = Color.GREEN
    graphic.drawOval(75, 50, 100, 100)
    graphic.color = Color.BLUE
    graphic.drawOval(75, 75, 100, 100)
    return  image
}