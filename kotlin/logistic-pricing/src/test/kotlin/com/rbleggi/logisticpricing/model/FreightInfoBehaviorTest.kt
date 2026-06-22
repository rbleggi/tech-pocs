package com.rbleggi.logisticpricing.model

import kotlin.reflect.KMutableProperty1
import kotlin.test.*

class FreightInfoBehaviorTest {

    // val/var generate a private backing field plus accessors: val only a getter, var a getter and a setter.
    // Accessing obj.x calls the generated getter, so encapsulation is kept without writing get/set by hand.
    private fun sample() = FreightInfo(10.0, 5.0, 100.0, TransportType.TRUCK)

    @Test
    fun `two freight infos with the same values are equal`() {
        assertEquals(sample(), sample())
    }

    @Test
    fun `two freight infos with the same values have the same hashCode`() {
        assertEquals(sample().hashCode(), sample().hashCode())
    }

    @Test
    fun `freight info prints its values as text`() {
        assertEquals("FreightInfo(volume=10.0, size=5.0, distance=100.0, transportType=TRUCK)", sample().toString())
    }

    @Test
    fun `freight info can be split into its values`() {
        val (volume, size, distance, transportType) = sample()

        assertEquals(10.0, volume)
        assertEquals(5.0, size)
        assertEquals(100.0, distance)
        assertEquals(TransportType.TRUCK, transportType)
    }

    @Test
    fun `copy makes a new freight info and keeps the old one`() {
        val original = sample()
        val changed = original.copy(distance = 999.0)

        assertEquals(999.0, changed.distance)
        assertEquals(100.0, original.distance)
        assertNotSame(original, changed)
    }

    @Test
    fun `a val property cannot be reassigned while a var property can`() {
        assertFalse(FreightInfo::volume is KMutableProperty1)
    }
}
