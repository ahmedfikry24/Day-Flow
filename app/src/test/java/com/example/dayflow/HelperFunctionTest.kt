package com.example.dayflow

import com.example.dayflow.ui.utils.validateRequireField
import org.junit.Assert
import org.junit.Test

class HelperFunctionTest {

    @Test
    fun `given empty string when validate require field then return false`() {
        val field = ""
        Assert.assertFalse(field.validateRequireField())
    }

    @Test
    fun `given valid string when validate require field then return false`() {
        val field = "ahmed"
        Assert.assertTrue(field.validateRequireField())
    }

}