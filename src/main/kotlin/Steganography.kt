package cryptography

import java.awt.Color
import java.awt.image.BufferedImage

object Steganography {

    fun hideMessageInImage(image: BufferedImage, messageInBits: List<Int>): BufferedImage {
        val columns = image.width
        val rows = image.height
        var bitCounter = 0
        rowLoop@ for (row in 0 until rows) {
            for (col in 0 until columns) {
                if (bitCounter > messageInBits.lastIndex) break@rowLoop
                val pixelColor = Color(image.getRGB(col, row))
                val blueColor = lastBitTo(pixelColor.blue, messageInBits[bitCounter])
                image.setRGB(col, row, Color(pixelColor.red, pixelColor.green, blueColor).rgb)
                bitCounter++
            }
        }
        return image
    }

    fun getHiddenBitsStringFromImage(image: BufferedImage): List<String> {
        val bits = mutableListOf<Int>()
        val bytes = mutableListOf<String>()
        var byteCounter = 0

        rowLoop@ for (row in 0 until image.height) {
            for (col in 0 until image.width) {
                val pixelColor = Color(image.getRGB(col, row))
                if (pixelColor.blue % 2 == 0) bits.add(0) else bits.add(1)
                byteCounter++
                if (byteCounter == BYTE_LENGTH) {
                    bytes.add(bits.joinToString(""))
                    bits.clear()
                    byteCounter = 0
                    if (bytes.size > END_SIGN_BYTES_LENGTH && verifyLastBytes(bytes.takeLast(END_SIGN_BYTES_LENGTH))) {
                        repeat(END_SIGN_BYTES_LENGTH) { bytes.removeLast() }
                        break@rowLoop
                    }
                }
            }
        }

        return bytes
    }

    fun textToBitList(message: String): List<Int> {
        val byteMessage = message.encodeToByteArray().toList()
        return byteListToBitList(byteMessage).toMutableList()
    }

    fun addEndMarker(byteList: List<Int>): List<Int> {
        return buildList {
            addAll(byteList)
            addAll(getBytesEndMarker())
        }
    }

    fun bitListToStringByteList(bits: List<Int>): List<String> {
        val resultList = mutableListOf<String>()
        var tempByte = ""
        var byteCounter = 0
        for (bit in bits) {
            tempByte += bit.toString()
            byteCounter++
            if(byteCounter == BYTE_LENGTH){
                resultList.add(tempByte)
                tempByte = ""
                byteCounter = 0
            }
        }
        return resultList
    }

    fun byteStringListToString(bitList: List<String>): String {
        val byteArray = ByteArray(bitList.size)
        for (index in byteArray.indices) {
            byteArray[index] = Integer.parseInt(bitList[index], BINARY_RADIX).toByte()
        }
        return byteArray.toString(Charsets.UTF_8)
    }

    fun binaryStringListToBitList(list: List<String>) : List<Int> {
        val resultList = mutableListOf<Int>()
        for (item in list){
            resultList.addAll(binaryStringToBitList(item))
        }
        return resultList
    }

    private fun verifyLastBytes(bytes: List<String>): Boolean {
        return bytes[0] == BINARY_ZERO && bytes[1] == BINARY_ZERO && bytes[2] == BINARY_THREE
    }

    private fun binaryStringToBitList(string: String): List<Int> {
        val bits = mutableListOf<Int>()
        val zeroes = BYTE_LENGTH - string.length

        repeat(zeroes) { bits.add(0) }
        for (bit in string) { bits.add(bit.digitToInt()) }

        return bits
    }

    private fun lastBitTo(integer: Int, lastBit: Int): Int {
        return if (integer.takeLowestOneBit() == 1) {
            if (lastBit == 0) integer - 1 else integer
        } else {
            if (lastBit == 0) integer else integer + 1
        }
    }

    private fun byteListToBitList(bitMessage: List<Byte>): List<Int> {
        val bits = mutableListOf<Int>()

        for (byte in bitMessage) {
            val binaryString = byteToBitString(byte)
            bits.addAll(binaryStringToBitList(binaryString))
        }

        return bits.toList()
    }

    private fun getBytesEndMarker(): List<Int> {
        return buildList {
            addAll(binaryStringToBitList(BINARY_ZERO))
            addAll(binaryStringToBitList(BINARY_ZERO))
            addAll(binaryStringToBitList(BINARY_THREE))
        }
    }

    private fun byteToBitString(byte: Byte): String = Integer.toBinaryString(byte.toInt())

}