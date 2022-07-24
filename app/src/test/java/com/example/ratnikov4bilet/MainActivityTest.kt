package com.example.ratnikov4bilet

import junit.framework.TestCase
import org.junit.Test

class MainActivityTest : TestCase() {

    public fun Slojnaya(categoryRemont: MutableList<CategoryRemont>): Int {
        categoryRemont.forEach(){
            if (it.Name == "Сложная"){
                return it.Id
            }
        }
        return -1
    }

    var first = mutableListOf<CategoryRemont>()

    @Test
    fun testSlojnaya() {
        assertEquals(Slojnaya(first),-1)
    }
}