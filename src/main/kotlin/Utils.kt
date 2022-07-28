package cryptography

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun printAndRead(message: String): String {
    println(message)
    return readln()
}

fun getBufferedImage(fileName: String): BufferedImage? {
    val file = getFile(fileName)
    if (verifyFile(file).not()) return null
    return ImageIO.read(file)
}

fun getFile(fileName: String): File? {
    val file = File("${getPath()}$fileName")
    return if(file.exists().not()) null else file
}

fun saveFile(image: BufferedImage, filaName: String) {
    ImageIO.write(image, "png", File("${getPath()}$filaName"))
}

fun verifyFile(file: File?): Boolean {
    return if (file == null) {
        println("Can't read input file!")
        false
    } else {
        true
    }
}

fun getPath() = "${System.getProperty("user.dir")}/src/main/resources/"