package com.fappslab.libraries.security.cryptography

import com.fappslab.libraries.security.cryptography.model.MetadataPreset
import com.fappslab.libraries.security.cryptography.model.StrongGCM
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
import com.fappslab.seedcake.libraries.extension.METADATA_TAG
import com.fappslab.seedcake.libraries.extension.isValidPassphrase
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

private const val TIMEOUT_DURATION = 10_000L
private const val MIN_ENTROPY_LENGTH = 12
private const val MAX_ENTROPY_LENGTH = 24

class CryptoManagerImpl(
    private val wordList: List<String>,
    private val metadata: MetadataPreset = StrongGCM()
) : CryptoManager {

    override suspend fun encrypt(seed: List<String>, passphrase: String): String {
        if (seed.isEmpty()) throw BlankSeedException()
        val validEntropyLengths = listOf(MIN_ENTROPY_LENGTH, MAX_ENTROPY_LENGTH)
        if (seed.size !in validEntropyLengths) throw InvalidEntropyLengthException()
        if (!seed.none { it !in wordList }) throw InvalidMnemonicSeedException()
        passphrase.toCommonValidation()

        return runCatching {
            val salt = generateRandomBytes(metadata.saltSize)
            val iv = generateRandomBytes(metadata.ivSize)
            val seedString = seed.joinToString(" ")
            val secretKey = generateSecretKey(passphrase, salt, metadata)

            val cipher = createCipher(metadata, Cipher.ENCRYPT_MODE, secretKey, iv)
            val encryptedData = cipher.doFinal(seedString.toByteArray())

            val source = metadata.toJson().toByteArray()
            val metadataBase64 = "$METADATA_TAG${Base64.getEncoder().encodeToString(source)}"

            "${Base64.getEncoder().encodeToString(salt + iv + encryptedData)}$metadataBase64"
        }.getOrElse { throw EncryptionFailedException() }
    }

    override suspend fun decrypt(encryptedSeed: String, passphrase: String): String {
        if (encryptedSeed.isEmpty()) throw BlankEncryptedSeedException()
        passphrase.toCommonValidation()

        val (encryptedData64, metadataBase64) = encryptedSeed.split(METADATA_TAG)
        val encryptedData = Base64.getDecoder().decode(encryptedData64)
        val metadataJson = String(Base64.getDecoder().decode(metadataBase64))

        val metadata = metadata.fromJson(metadataJson, metadata::class.java)
        val salt = slicedSalt(encryptedData, metadata)
        val iv = slicedIV(encryptedData, metadata)

        if (encryptedData.size < metadata.saltSize + metadata.ivSize)
            throw InvalidEncryptedSeedException()

        return runCatching {
            withTimeout(TIMEOUT_DURATION) {
                val secretKey = generateSecretKey(passphrase, salt, metadata)
                val cipher = createCipher(metadata, Cipher.DECRYPT_MODE, secretKey, iv)
                val decryptedData = cipher.doFinal(slicedData(encryptedData, metadata))

                String(decryptedData)
            }
        }.getOrElse { error ->
            if (error is TimeoutCancellationException) {
                throw DecryptionTimeoutException()
            } else throw DecryptionFailedException()
        }
    }

    private fun generateSecretKey(
        passphrase: String,
        salt: ByteArray,
        metadata: MetadataPreset
    ): SecretKey {
        val spec = PBEKeySpec(
            passphrase.toCharArray(),
            salt,
            metadata.keyIterations,
            metadata.keyBits
        )
        val factory = SecretKeyFactory.getInstance(metadata.keyDerivation.type)
        val tmp = factory.generateSecret(spec)

        return SecretKeySpec(tmp.encoded, metadata.keyAlgorithm)
    }

    private fun createCipher(
        metadata: MetadataPreset,
        mode: Int,
        secretKey: SecretKey,
        iv: ByteArray
    ): Cipher {
        return Cipher.getInstance(metadata.cipherSpec.type)
            .apply { init(mode, secretKey, metadata.createCipherSpec(iv, metadata.cipherSpec)) }
    }

    private fun String.toCommonValidation() {
        if (isBlank()) throw BlankPassphraseException()
        if (!isValidPassphrase()) throw InvalidPassphraseException()
    }

    private fun generateRandomBytes(size: Int): ByteArray {
        val bytes = ByteArray(size)
        SecureRandom().nextBytes(bytes)
        return bytes
    }

    private fun slicedSalt(encryptedData: ByteArray, metadata: MetadataPreset): ByteArray {
        return encryptedData.sliceArray(0 until metadata.saltSize)
    }

    private fun slicedIV(encryptedData: ByteArray, metadata: MetadataPreset): ByteArray {
        return encryptedData.sliceArray(
            metadata.saltSize until metadata.saltSize + metadata.ivSize
        )
    }

    private fun slicedData(encryptedData: ByteArray, metadata: MetadataPreset): ByteArray {
        return encryptedData.sliceArray(
            metadata.saltSize + metadata.ivSize until encryptedData.size
        )
    }
}
