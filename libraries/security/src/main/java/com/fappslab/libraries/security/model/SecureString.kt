package com.fappslab.libraries.security.model

/**
 * A class that securely handles strings by storing their characters in a char array.
 * This approach aims to mitigate the risks associated with immutable string objects
 * in Java and Kotlin, where sensitive data remains in memory until garbage collected.
 * The SecureString class provides a way to manually clear the stored data from memory,
 * offering an extra layer of security for handling sensitive information such as passwords.
 *
 * @property value The char array used to store the sensitive string data securely.
 */
class SecureString private constructor(
    private val value: CharArray
) {

    /**
     * Executes a given action with the secure data and clears the data from memory
     * after the action has been completed.
     *
     * This method provides a secure way to access and manipulate the sensitive data stored in the
     * `SecureString` instance. It accepts a lambda function (`action`) as a parameter, which allows
     * the calling code to work with the sensitive data encapsulated within a `CharArray`.
     * The `clear` method is called immediately after the `action` lambda is executed to overwrite
     * and clean the sensitive data from memory, minimizing the risk of sensitive data exposure.
     *
     * @param action A lambda function that takes a `CharArray` as its parameter, allowing the calling code
     * to use the sensitive data stored within the `SecureString`. The `action` is expected to perform operations
     * with the data securely and promptly.
     */
    fun use(action: (CharArray) -> Unit) {
        try {
            action(value)
        } finally {
            clear()
        }
    }

    /**
     * Clears the stored sensitive data by overwriting the char array with zeros.
     * This method should be called as soon as the sensitive data is no longer needed, or it can be
     * automatically triggered by the `use()` method.
     */
    private fun clear() {
        value.fill(element = '\u0000')
    }

    companion object {
        /**
         * Factory method to create a SecureString instance from a regular String.
         * This is the recommended way to instantiate SecureString objects as it
         * ensures the sensitive data is immediately stored in a secure manner.
         *
         * @param input The sensitive string data to be secured.
         * @return A SecureString instance containing the secured data.
         */
        fun from(input: String): SecureString {
            return SecureString(input.toCharArray())
        }
    }
}
