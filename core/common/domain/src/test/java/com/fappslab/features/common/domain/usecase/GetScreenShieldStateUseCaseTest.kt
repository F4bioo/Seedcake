package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.repository.StorageRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

internal class GetScreenShieldStateUseCaseTest {

    private val repository: StorageRepository = mockk()
    private val subject = GetScreenShieldStateUseCase(repository)

    @Test
    fun `getScreenShieldCheckBoxState Should return true When Shield check box is checked`() {
        // Given
        val expectedResult = true
        every { repository.getScreenShieldCheckBoxState() } returns true

        // When
        val result = subject()

        // Then
        assertEquals(expectedResult, result)
    }
}
