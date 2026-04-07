package com.sleon.biblium.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BookValidatorTest {

    // Usamos nombres de funciones entre comillas invertidas (backticks)
    // para que se lean como frases. ¡Es mucho más profesional!

    @Test
    fun `cuando el titulo esta vacio debe devolver falso`() {
        val resultado = BookValidator.isBookValid("", "Melissa Landers")
        // "assertFalse" comprueba que el resultado sea FALSE. Si es así, el test PASA.
        assertFalse(resultado)
    }

    @Test
    fun `cuando el autor es demasiado corto debe devolver falso`() {
        val resultado = BookValidator.isBookValid("Rey entre Sombras", "Me")
        assertFalse(resultado)
    }

    @Test
    fun `cuando los datos son correctos debe devolver verdadero`() {
        val resultado = BookValidator.isBookValid("Rey entre Sombras", "Melissa Landers")
        // "assertTrue" comprueba que el resultado sea TRUE.
        assertTrue(resultado)
    }
}