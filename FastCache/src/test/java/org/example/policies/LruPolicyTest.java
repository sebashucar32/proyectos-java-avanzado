package org.example.policies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LruPolicyTest {

    private LruPolicy<String> policy;

    @BeforeEach
    void setUp() {
        policy = new LruPolicy<>();
    }

    @Test
    @DisplayName("seleccionarClave retorna la primera clave insertada sin accesos posteriores")
    void seleccionarClave_sinAccesos_retornaMasAntigua() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");
        policy.registrarEntrada("C");

        assertEquals("A", policy.seleccionarClave());
    }

    @Test
    @DisplayName("seleccionarClave retorna la clave con menor uso reciente tras accesos")
    void seleccionarClave_conAccesos_retornaLRU() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");
        policy.registrarEntrada("C");

        // Acceder a A y B: C queda como el más antiguo sin acceso reciente,
        // pero el orden de acceso convierte a A y B en más recientes que C
        // => C debería ser el LRU si no se le accede... en realidad A fue el primero
        // Accedemos a A para que pase al final; B y C permanecen
        policy.registrarAcceso("A"); // A pasa al final → orden: B, C, A
        // LRU ahora es B
        assertEquals("B", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarEntrada con una sola clave — esa clave es candidata")
    void registrarEntrada_unaClaveEsCandidataExpulsion() {
        policy.registrarEntrada("única");
        assertEquals("única", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarEntrada de clave ya existente la actualiza sin duplicar")
    void registrarEntrada_claveExistente_actualiza() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");

        // Re-insertar A: en LRU con accessOrder=true, put también mueve al final
        policy.registrarEntrada("A"); // A pasa al final → orden: B, A

        assertEquals("B", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarEliminacion elimina la clave del seguimiento")
    void registrarEliminacion_eliminaClave() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");

        policy.registrarEliminacion("A");

        // Ahora la única candidata debe ser B
        assertEquals("B", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarEliminacion de clave inexistente no lanza excepción")
    void registrarEliminacion_claveInexistente_noLanzaExcepcion() {
        policy.registrarEntrada("A");

        assertDoesNotThrow(() -> policy.registrarEliminacion("Z"));
        assertEquals("A", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarAcceso mueve la clave al final, dejándola como más reciente")
    void registrarAcceso_mueveClaveAlFinal() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");
        policy.registrarEntrada("C");

        // Acceder a A: orden pasa a ser B → C → A
        policy.registrarAcceso("A");
        assertEquals("B", policy.seleccionarClave());

        // Acceder a B: orden pasa a ser C → A → B
        policy.registrarAcceso("B");
        assertEquals("C", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarAcceso repetido a la misma clave sigue dejándola como más reciente")
    void registrarAcceso_repetido_clavePermaneceAlFinal() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");

        policy.registrarAcceso("A");
        policy.registrarAcceso("A");

        // B es LRU
        assertEquals("B", policy.seleccionarClave());
    }

    @Test
    @DisplayName("limpiar vacía el seguimiento completamente")
    void limpiar_vaciaTodoElSeguimiento() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");

        policy.limpiar();

        // Después de limpiar, el mapa interno debe estar vacío;
        // seleccionarClave lanzaría NoSuchElementException porque no hay claves —
        // esto es un comportamiento esperado del mapa vacío.
        assertThrows(Exception.class, () -> policy.seleccionarClave());
    }

    @Test
    @DisplayName("limpiar seguido de nuevas entradas funciona correctamente")
    void limpiar_luegoNuevasEntradas_funcionaCorrectamente() {
        policy.registrarEntrada("A");
        policy.limpiar();

        policy.registrarEntrada("X");
        policy.registrarEntrada("Y");

        assertEquals("X", policy.seleccionarClave());
    }

    @Test
    @DisplayName("ciclo completo: insertar, acceder, eliminar y seleccionar correctamente")
    void ciclioCompleto_expulsaLRU() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");
        policy.registrarEntrada("C");

        policy.registrarAcceso("A"); // orden: B → C → A
        policy.registrarAcceso("B"); // orden: C → A → B

        // C es el LRU
        String victima = policy.seleccionarClave();
        assertEquals("C", victima);

        policy.registrarEliminacion(victima);

        // Siguiente LRU es A
        assertEquals("A", policy.seleccionarClave());
    }
}
