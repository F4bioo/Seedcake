package com.fappslab.libraries.security.cryptography

import com.fappslab.features.common.domain.usecase.DecryptParams
import com.fappslab.features.common.domain.usecase.EncryptParams
import com.fappslab.libraries.security.cryptography.model.extension.toMetadata
import com.fappslab.libraries.security.model.ValidationType
import com.fappslab.seedcake.libraries.extension.METADATA_TAG
import com.fappslab.seedcake.libraries.extension.blankString
import com.fappslab.seedcake.libraries.extension.splitToList
import kotlinx.coroutines.withTimeout
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

private const val TIMEOUT_DURATION = 10_000L

class CryptoManagerImpl(
    private val wordList: List<String>
) : CryptoManager {

    private val validator by lazy { CryptoValidator(wordList) }

    override suspend fun encrypt(params: EncryptParams): String {
        validator.encryptValidation(params.readableSeedPhrase, params.passphrase)

        return runCatching {
            val metadata = params.cipherSpec.toMetadata()
            val salt = generateRandomBytes(metadata.saltSize)
            val iv = generateRandomBytes(metadata.ivSize)
            val seedString = params.readableSeedPhrase.joinToString(blankString())
            val secretKey = generateSecretKey(params.passphrase.toCharArray(), salt, metadata)

            val cipher = createCipher(metadata, Cipher.ENCRYPT_MODE, secretKey, iv)
            val encryptedData = cipher.doFinal(seedString.toByteArray())

            val source = MetadataPreset.toJson(metadata).toByteArray()
            val metadataBase64 = "$METADATA_TAG${Base64.getEncoder().encodeToString(source)}"

            "${Base64.getEncoder().encodeToString(salt + iv + encryptedData)}$metadataBase64"
        }.orParseError(defType = ValidationType.ENCRYPTION_FAILED)
    }

    override suspend fun decrypt(params: DecryptParams): String {
        val (unreadableSeedPhrase, passphrase) = params
        validator.decryptValidation(unreadableSeedPhrase, passphrase)

        return runCatching {
            val (encryptedData64, metadataBase64) = unreadableSeedPhrase.splitToList(METADATA_TAG)
            val encryptedData = Base64.getDecoder().decode(encryptedData64)
            val metadataJson = String(Base64.getDecoder().decode(metadataBase64))

            val metadata = MetadataPreset.fromJson(metadataJson)
            val salt = slicedSalt(encryptedData, metadata)
            val iv = slicedIV(encryptedData, metadata)

            withTimeout(TIMEOUT_DURATION) {
                val secretKey = generateSecretKey(passphrase.toCharArray(), salt, metadata)
                val cipher = createCipher(metadata, Cipher.DECRYPT_MODE, secretKey, iv)
                val decryptedData = cipher.doFinal(slicedData(encryptedData, metadata))

                String(decryptedData)
            }
        }.orParseError(defType = ValidationType.DECRYPTION_FAILED)
    }

    private fun generateSecretKey(
        passphrase: CharArray,
        salt: ByteArray,
        metadata: MetadataPreset
    ): SecretKey {
        val spec = PBEKeySpec(passphrase, salt, metadata.keyIterations, metadata.keyBits)
        val factory = SecretKeyFactory.getInstance(metadata.keyDerivation.type)
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, metadata.keyAlgorithm)
    }

    private fun createCipher(
        metadata: MetadataPreset,
        mode: Int,
        key: SecretKey,
        iv: ByteArray
    ): Cipher {
        val params = metadata.cipherSpec.parameterSpec(iv)
        return Cipher.getInstance(metadata.cipherSpec.type)
            .apply { init(mode, key, params) }
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
