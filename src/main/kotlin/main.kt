package cryptography

/**
 * SOLO USAR IMAGEN PNG
 * La imagen a usar debe estar previamente guardada en la carpeta de 'resources'
 * La imagen con el texto oculto se genera en la carpeta de 'resources'
 */

fun main() {
    var run = true
    while (run) {
        println("Task (hide, show, exit):")
        when (readln()) {
            "hide" -> hide()
            "show" -> show()
            "exit" -> {
                println("Bye!")
                run = false
            }
            else -> {
                println("Wrong task: [input String]")
            }
        }
    }
}

fun show() {
    val fileName = printAndRead("Input image file:")
    val image = getBufferedImage(fileName) ?: return

    val password = printAndRead("Password:")
    val passwordBytes = Steganography.textToBitList(password)
    val hiddenBits = Steganography.getHiddenBitsStringFromImage(image)
    val hiddenBitsJoinList = Steganography.binaryStringListToBitList(hiddenBits)

    val decryptedMessageBits = Cryptography.encryptDecryptBits(hiddenBitsJoinList, passwordBytes)
    val decryptedBitsString = Steganography.bitListToStringByteList(decryptedMessageBits)
    val message = Steganography.byteStringListToString(decryptedBitsString)
    println("Message:")
    println(message)
}

fun hide() {

    val fileName = printAndRead("Input image file:")
    val image = getBufferedImage(fileName) ?: return

    val fileNameOutput = printAndRead("Output image file:")
    val message = printAndRead("Message to hide:")

    if (image.width * image.height < message.length * BYTE_LENGTH) {
        println("The input image is not large enough to hold this message.")
        return
    }

    val password = printAndRead("Password:")
    val messageBytes = Steganography.textToBitList(message)
    val passwordBytes = Steganography.textToBitList(password)
    val encryptedMessageBytes = Cryptography.encryptDecryptBits(messageBytes, passwordBytes)
    val finalBytes = Steganography.addEndMarker(encryptedMessageBytes)
    val concealedImage = Steganography.hideMessageInImage(image, finalBytes)
    saveFile(concealedImage, fileNameOutput)

    println("Message saved in $fileNameOutput image.")
}