package com.fappslab.libraries.security.cryptography

import com.fappslab.seedcake.libraries.arch.exceptions.BlankEncryptedSeedException
import com.fappslab.seedcake.libraries.arch.exceptions.BlankPassphraseException
import com.fappslab.seedcake.libraries.arch.exceptions.BlankSeedException
import com.fappslab.seedcake.libraries.arch.exceptions.DecryptionFailedException
import com.fappslab.seedcake.libraries.arch.exceptions.DecryptionTimeoutException
import com.fappslab.seedcake.libraries.arch.exceptions.EncryptionFailedException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidEncryptedSeedException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidEntropyLengthException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidMnemonicSeedException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidPassphraseException
import com.fappslab.seedcake.libraries.extension.isValidPassphrase
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

private const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding"
private const val KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA256"
private const val SECRET_KEY_ALGORITHM = "AES"
private const val KEY_DERIVATION_ITERATION_COUNT = 65536
private const val SECRET_KEY_BIT_LENGTH = 256
private const val IV_BYTE_ARRAY_SIZE = 16
private const val TIMEOUT_DURATION = 10_000L
private const val MIN_ENTROPY_LENGTH = 12
private const val MAX_ENTROPY_LENGTH = 24

class CryptoManagerImpl(private val wordList: List<String>) : CryptoManager {

    override suspend fun encrypt(seed: List<String>, passphrase: String): String {
        if (seed.isEmpty()) throw BlankSeedException()
        val validEntropyLengths = listOf(MIN_ENTROPY_LENGTH, MAX_ENTROPY_LENGTH)
        if (seed.size !in validEntropyLengths) throw InvalidEntropyLengthException()
        if (!seed.none { it !in wordList }) throw InvalidMnemonicSeedException()
        passphrase.toCommonValidation()

        return runCatching {
            val seedString = seed.joinToString(separator = " ")
            val secretKey = generateSecretKey(passphrase)
            val iv = ByteArray(IV_BYTE_ARRAY_SIZE)
            SecureRandom().nextBytes(iv)
            val cipher = createCipher(Cipher.ENCRYPT_MODE, secretKey, iv)
            val encryptedData = cipher.doFinal(seedString.toByteArray())

            val encryptedSeed = iv + encryptedData
            Base64.getEncoder().encodeToString(encryptedSeed)
        }.getOrElse { throw EncryptionFailedException() }
    }

    override suspend fun decrypt(encryptedSeed: String, passphrase: String): String {
        if (encryptedSeed.isEmpty()) throw BlankEncryptedSeedException()
        passphrase.toCommonValidation()

        val encryptedData = runCatching { Base64.getDecoder().decode(encryptedSeed) }
            .getOrElse { throw InvalidEncryptedSeedException() }

        if (encryptedData.size < IV_BYTE_ARRAY_SIZE) throw InvalidEncryptedSeedException()

        return runCatching {
            withTimeout(TIMEOUT_DURATION) {
                val iv = encryptedData.sliceArray(indices = 0 until IV_BYTE_ARRAY_SIZE)
                val data = encryptedData.sliceArray(IV_BYTE_ARRAY_SIZE until encryptedData.size)
                val secretKey = generateSecretKey(passphrase)
                val cipher = createCipher(Cipher.DECRYPT_MODE, secretKey, iv)
                val decryptedData = cipher.doFinal(data)

                String(decryptedData)
            }
        }.getOrElse { error ->
            if (error is TimeoutCancellationException) {
                throw DecryptionTimeoutException()
            } else throw DecryptionFailedException()
        }
    }

    private fun generateSecretKey(passphrase: String): SecretKey {
        val factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM)
        val spec = PBEKeySpec(
            passphrase.toCharArray(),
            passphrase.toByteArray(),
            KEY_DERIVATION_ITERATION_COUNT,
            SECRET_KEY_BIT_LENGTH
        )
        val tmp = factory.generateSecret(spec)

        return SecretKeySpec(tmp.encoded, SECRET_KEY_ALGORITHM)
    }

    private fun createCipher(mode: Int, secretKey: SecretKey, iv: ByteArray): Cipher {
        return Cipher.getInstance(CIPHER_TRANSFORMATION).apply {
            init(mode, secretKey, IvParameterSpec(iv))
        }
    }

    private fun String.toCommonValidation() {
        if (isBlank()) throw BlankPassphraseException()
        if (!isValidPassphrase()) throw InvalidPassphraseException()
    }
}
