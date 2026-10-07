package org.example.policies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FifoPolicyTest {

    private FifoPolicy<String> policy;

    @BeforeEach
    void setUp() {
        policy = new FifoPolicy<>();
    }

    @Test
    @DisplayName("seleccionarClave retorna null cuando la cola está vacía")
    void seleccionarClave_colaVacia_retornaNull() {
        assertNull(policy.seleccionarClave());
    }

    @Test
    @DisplayName("seleccionarClave retorna la primera clave insertada sin eliminarla")
    void seleccionarClave_retornaPrimeroSinEliminar() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");
        policy.registrarEntrada("C");

        assertEquals("A", policy.seleccionarClave());
        // Verificar que peek no consumió el elemento
        assertEquals("A", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarEntrada agrega las claves en orden FIFO")
    void registrarEntrada_mantieneOrdenFifo() {
        policy.registrarEntrada("primero");
        policy.registrarEntrada("segundo");
        policy.registrarEntrada("tercero");

        assertEquals("primero", policy.seleccionarClave());
        assertEquals(3, policy.cola.size());
    }

    @Test
    @DisplayName("registrarEntrada con una sola clave — esa clave es la candidata")
    void registrarEntrada_unaClaveEsCandidataExpulsion() {
        policy.registrarEntrada("X");
        assertEquals("X", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarEliminacion elimina la clave indicada de la cola")
    void registrarEliminacion_eliminaClave() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");
        policy.registrarEntrada("C");

        policy.registrarEliminacion("A");

        assertEquals("B", policy.seleccionarClave());
        assertEquals(2, policy.cola.size());
    }

    @Test
    @DisplayName("registrarEliminacion de clave inexistente no lanza excepción")
    void registrarEliminacion_claveInexistente_noLanzaExcepcion() {
        policy.registrarEntrada("A");

        assertDoesNotThrow(() -> policy.registrarEliminacion("Z"));
        assertEquals(1, policy.cola.size());
    }

    @Test
    @DisplayName("registrarEliminacion elimina solo la primera ocurrencia")
    void registrarEliminacion_eliminaPrimeraOcurrencia() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("A");

        policy.registrarEliminacion("A");

        assertEquals(1, policy.cola.size());
        assertEquals("A", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarAcceso no altera el orden FIFO")
    void registrarAcceso_noAlteraOrden() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");

        // No debe lanzar excepción ni cambiar el orden
        assertDoesNotThrow(() -> policy.registrarAcceso("A"));
        assertEquals("A", policy.seleccionarClave());
    }

    @Test
    @DisplayName("limpiar vacía la cola completamente")
    void limpiar_vaciaLaCola() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");

        policy.limpiar();

        assertNull(policy.seleccionarClave());
        assertTrue(policy.cola.isEmpty());
    }

    @Test
    @DisplayName("limpiar sobre cola vacía no lanza excepción")
    void limpiar_colaVacia_noLanzaExcepcion() {
        assertDoesNotThrow(() -> policy.limpiar());
        assertTrue(policy.cola.isEmpty());
    }
}
