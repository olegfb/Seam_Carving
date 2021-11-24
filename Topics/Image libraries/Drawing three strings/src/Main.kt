import java.awt.Color
import java.awt.image.BufferedImage

fun drawStrings(): BufferedImage {
    val vStr = "Hello, images!"
  val image = BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB)
  val graphic = image.createGraphics()
  graphic.color = Color.RED
  graphic.drawString(vStr, 50, 50)
  graphic.color = Color.GREEN
  graphic.drawString(vStr, 51, 51)
  graphic.color = Color.BLUE
  graphic.drawString(vStr, 52, 52)
  return  image
}