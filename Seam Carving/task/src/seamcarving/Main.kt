package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.system.exitProcess

class MeasuredPath (coordinate: Pair<Int, Int>, val Weight: Double) {
    val fArrPath =  arrayListOf(coordinate)

    constructor(coordinate: Pair<Int, Int>, Weight: Double, arrPath: ArrayList<Pair<Int, Int>>): this(coordinate, Weight){
        fArrPath.addAll(arrPath)
    }
}

class MyImageEditor(private var myImage: BufferedImage) {
    private var arrEnergy = Array(myImage.width) { DoubleArray(myImage.height) { 0.0 } }
    private var arrMeasuredPath = Array(myImage.width) { arrayOfNulls<MeasuredPath>(myImage.height) }

    private fun initArr(){
        arrEnergy = Array(myImage.width) { DoubleArray(myImage.height) { 0.0 } }
        arrMeasuredPath = Array(myImage.width) { arrayOfNulls(myImage.height) }
    }

    private fun calcGradient (pPix1: Color, pPix2: Color) =
        (pPix1.red - pPix2.red).toDouble().pow(2) + (pPix1.green - pPix2.green).toDouble().pow(2) + (pPix1.blue - pPix2.blue).toDouble().pow(2)

    fun getImage() = this.myImage

    private fun calcEnergy() {
        var x1: Int
        var x2: Int
        var y1: Int
        var y2: Int
        for (i in 0 until myImage.width) {
            when (i) {
                0 -> {
                    x1 = 0
                    x2 = 2
                }
                myImage.width - 1 -> {
                    x1 = myImage.width - 3
                    x2 = myImage.width - 1
                }
                else -> {
                    x1 = i - 1
                    x2 = i + 1
                }
            }
            for (j in 0 until myImage.height) {
                when(j) {
                    0 -> {
                        y1 = 0
                        y2 = 2
                    }
                    myImage.height - 1 -> {
                        y1 = myImage.height - 3
                        y2 = myImage.height - 1
                    }
                    else -> {
                        y1 = j - 1
                        y2 = j + 1
                    }
                }
                arrEnergy[i][j] = sqrt(calcGradient(Color(myImage.getRGB(x1, j)), Color(myImage.getRGB(x2, j))) + calcGradient(Color(myImage.getRGB(i,y1)), Color(myImage.getRGB(i, y2))))
            }
        }
    }

    private fun seamCarving(cnt: Int) {
        var nj: Int
        repeat(cnt) {
            val newImage = BufferedImage(myImage.width, myImage.height - 1, BufferedImage.TYPE_INT_RGB)
            calcEnergy()
            calcPath()
            val obj = getMinPath(arrMeasuredPath[myImage.width - 1])
//            val obj = arrMeasuredPath[myImage.width - 1].sort()
            for (i in 0 until myImage.width) {
                nj = 0
                for (j in 0 until myImage.height) {
                    if (Pair(i, j) in obj.fArrPath) continue
                    newImage.setRGB(i, nj, myImage.getRGB(i, j))
                    nj += 1
                }
            }
            myImage = newImage
            initArr()
        }
    }

    private fun getMinPath(arrObj: Array<MeasuredPath?>): MeasuredPath{
        arrObj.sortBy { it!!.Weight }
        return arrObj[0]!!
    }

    private fun calcPath(){
        for (i in 0 until myImage.width) {
            for (j in 0 until myImage.height) {
                if (i == 0) {
                    arrMeasuredPath[0][j] = MeasuredPath(Pair(0,j), arrEnergy[0][j])
                    continue
                }
                val obj = when (j){
                    0 -> getMinPath(arrayOf(arrMeasuredPath[i - 1][0],arrMeasuredPath[i - 1][1]))
                    myImage.height - 1 -> getMinPath(arrayOf(arrMeasuredPath[i - 1][j -1 ],arrMeasuredPath[i - 1][j]))
                    else -> getMinPath(arrayOf(arrMeasuredPath[i - 1][j - 1],arrMeasuredPath[i - 1][j], arrMeasuredPath[i - 1][j + 1]))
                }
                arrMeasuredPath[i][j] = MeasuredPath(Pair(i,j), obj.Weight + arrEnergy[i][j], obj.fArrPath)
            }
        }
    }

    private fun transpositionImage(){
        val newImage = BufferedImage(myImage.height, myImage.width, BufferedImage.TYPE_INT_RGB)
        for (i in 0 until myImage.width) for (j in 0 until myImage.height) {
            newImage.setRGB(j, i, myImage.getRGB(i, j))
        }
        myImage = newImage
        initArr()
    }

    fun resize(pWidth: Int, pHeight: Int) {
        if (pWidth > 0) { // уменьшаем по ширине
            transpositionImage()
            seamCarving(pWidth)
            transpositionImage()
        }
        if (pHeight > 0) {//уменьшаем по высоте
            seamCarving(pHeight)
        }
    }
}

fun main(args: Array<String>) {
    val st = System.currentTimeMillis()
    val argsMap = checkArgs(args)
    if (argsMap["msg"] != "ok") {
        println(argsMap["msg"])
        exitProcess(1)
    }
    val picture = MyImageEditor(ImageIO.read(File(argsMap["-in"]!!)))
    picture.resize(argsMap["-width"]!!.toInt(), argsMap["-height"]!!.toInt())
    ImageIO.write(picture.getImage(), "png", File(argsMap["-out"]!!))
    println(System.currentTimeMillis() - st)
}

fun checkArgs(args: Array<String>): MutableMap<String, String> {
    val argsMap = mutableMapOf("msg" to "ok")
    if (args.size != 8) {
        argsMap["msg"] = "Неверное число аргументов"
        return argsMap
    }
    for (i in 0..7 step 2) {
        argsMap[args[i].lowercase()] = args[i + 1]
    }
    if ("-in" !in argsMap.keys || "-out" !in argsMap.keys || "-width" !in argsMap.keys || "-height" !in argsMap.keys) {
        argsMap["msg"] = "Отсутствуют ключевые параметы -in -out -width -height"
    }
    return argsMap
}
