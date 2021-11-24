import java.awt.Color
import java.awt.image.BufferedImage
//import java.io.File
//import javax.imageio.ImageIO
//
//fun main() {
//  val imagefile = File("MyImage.png")
//  ImageIO.write(drawLines(), "png", imagefile)
//}

fun drawLines(): BufferedImage {
  val image = BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB)
  val graphics = image.createGraphics()
  graphics.color = Color.RED
  graphics.drawLine(0,0, 200, 200)
  graphics.color = Color.GREEN
  graphics.drawLine(200,0, 0, 200)
  return image
}