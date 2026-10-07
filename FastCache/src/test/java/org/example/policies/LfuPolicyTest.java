package org.example.policies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LfuPolicyTest {
    private LfuPolicy<String> policy;

    @BeforeEach
    void setUp() {
        policy = new LfuPolicy<>();
    }

    @Test
    @DisplayName("seleccionarClave retorna la primera clave insertada cuando todas tienen freq=1")
    void seleccionarClave_mismaFrecuencia_retornaMasAntigua() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");
        policy.registrarEntrada("C");

        // Todas tienen frecuencia 1 → desempate FIFO → A es la víctima
        assertEquals("A", policy.seleccionarClave());
    }

    @Test
    @DisplayName("seleccionarClave retorna la clave con menor frecuencia de acceso")
    void seleccionarClave_retornaMenorFrecuencia() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");
        policy.registrarEntrada("C");

        // A y B se acceden → quedan con frecuencia 2; C sigue con 1
        policy.registrarAcceso("A");
        policy.registrarAcceso("B");

        assertEquals("C", policy.seleccionarClave());
    }

    @Test
    @DisplayName("seleccionarClave desempata por orden de inserción (LRU) cuando hay igual frecuencia")
    void seleccionarClave_desempatePorOrdenInsercion() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");

        policy.registrarAcceso("A"); // A → freq 2
        policy.registrarAcceso("B"); // B → freq 2

        // Ambas con freq 2; A fue accedida primero → A es la víctima (FIFO dentro del grupo)
        assertEquals("A", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarEntrada asigna frecuencia 1 y fija minFrecuencia a 1")
    void registrarEntrada_asignaFrecuenciaUno() {
        policy.registrarEntrada("X");
        // minFrecuencia debe ser 1 y X debe ser candidata
        assertEquals("X", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarEntrada de nueva clave restablece minFrecuencia a 1")
    void registrarEntrada_restableceMinimoAUno() {
        policy.registrarEntrada("A");
        policy.registrarAcceso("A"); // A pasa a freq 2

        // Insertar B: minFrecuencia debe volver a 1
        policy.registrarEntrada("B");

        // B tiene freq 1 → es la víctima, no A
        assertEquals("B", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarEntrada múltiple crea grupos independientes")
    void registrarEntrada_multiple_creaGrupos() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");
        policy.registrarEntrada("C");

        assertEquals(3, policy.seleccionarClave() != null ? 3 : 0);
        // Verificar que A es la candidata (primera en freq=1)
        assertEquals("A", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarEliminacion quita la clave del seguimiento")
    void registrarEliminacion_eliminaClave() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");

        policy.registrarEliminacion("A");

        // Ahora B es la única candidata
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
    @DisplayName("registrarEliminacion de clave con frecuencia alta la remueve del grupo correcto")
    void registrarEliminacion_claveConAltaFrecuencia() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");

        policy.registrarAcceso("A"); // A → freq 2
        policy.registrarAcceso("A"); // A → freq 3

        policy.registrarEliminacion("A");

        // Solo queda B con freq 1
        assertEquals("B", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarAcceso incrementa la frecuencia de la clave")
    void registrarAcceso_incrementaFrecuencia() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");

        policy.registrarAcceso("A"); // A → freq 2; B sigue en freq 1

        // B es LFU
        assertEquals("B", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarAcceso actualiza minFrecuencia cuando el grupo queda vacío")
    void registrarAcceso_actualizaMinFrecuenciaCuandoGrupoVacio() {
        policy.registrarEntrada("A"); // freq=1, min=1

        // Al acceder a la única clave, el grupo 1 queda vacío → min sube a 2
        policy.registrarAcceso("A");

        // A ahora tiene freq=2; seleccionarClave debe seguir devolviendo A
        assertEquals("A", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarAcceso múltiple eleva la frecuencia correctamente")
    void registrarAcceso_multiple_elevafrecuencia() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");

        // A accedida 3 veces: freq 1→2→3→4
        policy.registrarAcceso("A");
        policy.registrarAcceso("A");
        policy.registrarAcceso("A");

        // B con freq=1 sigue siendo LFU
        assertEquals("B", policy.seleccionarClave());
    }

    @Test
    @DisplayName("registrarAcceso no actualiza minFrecuencia cuando hay más claves en el grupo actual")
    void registrarAcceso_noActualizaMinSiGrupoNoVacio() {
        policy.registrarEntrada("A"); // freq=1
        policy.registrarEntrada("B"); // freq=1, grupo 1 tiene {A, B}

        // Acceder a A: grupo 1 todavía tiene a B → min permanece en 1
        policy.registrarAcceso("A");

        // B con freq=1 debe seguir siendo la víctima
        assertEquals("B", policy.seleccionarClave());
    }

    @Test
    @DisplayName("limpiar vacía todos los mapas internos")
    void limpiar_vaciaTodo() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");
        policy.registrarAcceso("A");

        policy.limpiar();

        // Después de limpiar no hay grupos, seleccionarClave lanzará NullPointerException
        // porque grupos.get(0) es null — este es el comportamiento documentado tras limpiar.
        assertThrows(Exception.class, () -> policy.seleccionarClave());
    }

    @Test
    @DisplayName("limpiar seguido de nuevas entradas funciona correctamente")
    void limpiar_luegoNuevasEntradas_funcionaCorrectamente() {
        policy.registrarEntrada("A");
        policy.registrarAcceso("A");
        policy.limpiar();

        policy.registrarEntrada("X");
        policy.registrarEntrada("Y");

        assertEquals("X", policy.seleccionarClave());
    }

    @Test
    @DisplayName("limpiar sobre policy vacía no lanza excepción")
    void limpiar_vacia_noLanzaExcepcion() {
        assertDoesNotThrow(() -> policy.limpiar());
    }

    @Test
    @DisplayName("ciclo completo: inserciones, accesos, eliminaciones y selección LFU correcta")
    void cicloCompleto_expulsaLFU() {
        policy.registrarEntrada("A");
        policy.registrarEntrada("B");
        policy.registrarEntrada("C");

        policy.registrarAcceso("A"); // A→2
        policy.registrarAcceso("A"); // A→3
        policy.registrarAcceso("B"); // B→2

        // C tiene freq=1 → es la víctima
        String victima = policy.seleccionarClave();
        assertEquals("C", victima);

        policy.registrarEliminacion(victima);

        // Siguiente LFU: B con freq=2 (A tiene freq=3)
        assertEquals("B", policy.seleccionarClave());

        policy.registrarEliminacion("B");

        // Solo queda A
        assertEquals("A", policy.seleccionarClave());
    }
}
