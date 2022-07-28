package cryptography

object Cryptography {

    fun encryptDecryptBits(bytesMessage: List<Int>, bytesPassword: List<Int>): MutableList<Int> {
        return bytesMessage.mapIndexed { index, element ->
            element.xor(bytesPassword[index % bytesPassword.size])
        }.toMutableList()
    }

}