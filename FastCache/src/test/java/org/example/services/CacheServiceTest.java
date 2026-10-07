package org.example.services;

import org.example.models.CacheEntry;
import org.example.models.Estadistica;
import org.example.repositories.PoliticaExpulsion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CacheService - Tests unitarios")
class CacheServiceTest {
    @Mock
    private PoliticaExpulsion<String> politicaExpulsion;

    private CacheService<String, String> cacheService;

    // ─── Helpers ────────────────────────────────────────────────────────────────

    /** Inserta una entrada expirada directamente en el mapa interno via reflexión. */
    @SuppressWarnings("unchecked")
    private void insertarEntradaExpirada(String clave, String valor) throws Exception {
        CacheEntry<String> entradaExpirada = new CacheEntry<>(valor,
                LocalDateTime.now().minusSeconds(1));
        Field campoCache = CacheService.class.getDeclaredField("cache");
        campoCache.setAccessible(true);
        Map<String, CacheEntry<String>> mapaInterno =
                (Map<String, CacheEntry<String>>) campoCache.get(cacheService);
        mapaInterno.put(clave, entradaExpirada);
    }

    @Nested
    @DisplayName("guardar()")
    class Guardar {
        @BeforeEach
        void setUp() {
            cacheService = new CacheService<>(politicaExpulsion, 3);
        }

        @Test
        void guardarCuandoEsExitoso() {
            // Arrange
            String clave = "usuario";
            String valor = "Sebastián";

            // Act
            cacheService.guardar(clave, valor);

            // Assert
            verify(politicaExpulsion, times(1)).registrarEntrada(clave);
            assertEquals(1, cacheService.verTamanioCache());
        }

        @Test
        void guardarCuandoEsClaveNulaLanzaExcepcion() {
            // Arrange
            String clave = null;
            String valor = "valor";

            // Act & Assert
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cacheService.guardar(clave, valor));
            assertEquals("La clave no puede ser nula", ex.getMessage());
            verifyNoInteractions(politicaExpulsion);
        }

        @Test
        void guardarCuandoEsValorNulolanzaExcepcion() {
            // Arrange
            String clave = "clave";
            String valor = null;

            // Act & Assert
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cacheService.guardar(clave, valor));
            assertEquals("El valor no puede ser nulo", ex.getMessage());
            verifyNoInteractions(politicaExpulsion);
        }

        @Test
        void guardarCuandoClaveEsDuplicadalanzaExcepcion() {
            // Arrange
            cacheService.guardar("clave1", "valor1");

            // Act & Assert
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cacheService.guardar("clave1", "valorNuevo"));
            assertTrue(ex.getMessage().contains("ya existe en la caché"));
        }

        @Test
        void guardarCuandoCapacidadEstaLlenaExpulsaElemento() {
            // Arrange
            cacheService.guardar("c1", "v1");
            cacheService.guardar("c2", "v2");
            cacheService.guardar("c3", "v3");
            when(politicaExpulsion.seleccionarClave()).thenReturn("c1");

            // Act
            cacheService.guardar("c4", "v4");

            // Assert
            verify(politicaExpulsion, times(1)).seleccionarClave();
            verify(politicaExpulsion, atLeast(1)).registrarEliminacion("c1");
            assertEquals(3, cacheService.verTamanioCache());
        }

        @Test
        @DisplayName("Éxito - elementos expirados se limpian antes de evaluar capacidad")
        void guardarCuandoHayElementoaExpirados() throws Exception {
            // Arrange
            cacheService.guardar("c1", "v1");
            cacheService.guardar("c2", "v2");
            insertarEntradaExpirada("c3exp", "vExp");   // tercero expirado, no activa expulsión

            // Act – la capacidad aún no se alcanza tras limpiar el expirado
            cacheService.guardar("c4", "v4");

            // Assert
            verify(politicaExpulsion, never()).seleccionarClave();
        }
    }

    @Nested
    @DisplayName("buscarElemento()")
    class BuscarElemento {
        @BeforeEach
        void setUp() {
            cacheService = new CacheService<>(politicaExpulsion, 3);
        }

        @Test
        void buscarElementoExitosoEntoncesRegistraHit() {
            // Arrange
            cacheService.guardar("clave", "valor");

            // Act
            String resultado = cacheService.buscarElemento("clave");

            // Assert
            assertEquals("valor", resultado);
            assertEquals(1L, cacheService.estadisticas().getHits());
            assertEquals(0L, cacheService.estadisticas().getMisses());
            verify(politicaExpulsion, times(1)).registrarAcceso("clave");
        }

        @Test
        void buscarElementoErrorClaveInexistenteEntoncesRegistraMiss() {
            // Arrange
            String clave = "noExiste";

            // Act & Assert
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> cacheService.buscarElemento(clave));
            assertTrue(ex.getMessage().contains("no existe en la caché"));
            assertEquals(1L, cacheService.estadisticas().getMisses());
            assertEquals(0L, cacheService.estadisticas().getHits());
        }

        @Test
        void buscarElementoErrorClaveNulaEntonceslanzaExcepcion() {
            assertThrows(IllegalArgumentException.class,
                () -> cacheService.buscarElemento(null));
        }

        @Test
        void buscarElementoErrorElementoExpiradoRegistraMissYExpirado() throws Exception {
            // Arrange
            insertarEntradaExpirada("claveExp", "valorExp");

            // Act & Assert
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cacheService.buscarElemento("claveExp"));
            assertTrue(ex.getMessage().contains("ha expirado"));
            assertEquals(1L, cacheService.estadisticas().getMisses());
            assertEquals(1L, cacheService.estadisticas().getExpirados());
            verify(politicaExpulsion, times(1)).registrarEliminacion("claveExp");
        }

        @Test
        @DisplayName("Éxito - tiempoAcceso se acumula también en miss")
        void buscarElementoTiempoAccesoAcumulaEnMiss() {
            // Arrange – no hay elemento guardado

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                () -> cacheService.buscarElemento("fantasma"));
            assertTrue(cacheService.estadisticas().getTiempoAcceso() > 0);
        }
    }

    @Nested
    @DisplayName("actualizar()")
    class Actualizar {
        @BeforeEach
        void setUp() {
            cacheService = new CacheService<>(politicaExpulsion, 3);
        }

        @Test
        void actualizarCuandoEsExitoso() {
            // Arrange
            cacheService.guardar("clave", "valorViejo");

            // Act
            cacheService.actualizar("clave", "valorNuevo");

            // Assert
            assertEquals("valorNuevo", cacheService.buscarElemento("clave"));
        }

        @Test
        void actualizarErrorClaveInexistenteLanzaExcepcion() {
            // Arrange
            String clave = "noExiste";

            // Act & Assert
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cacheService.actualizar(clave, "valor"));
            assertTrue(ex.getMessage().contains("debe existir en la caché"));
        }

        @Test
        void actualizarErrorClaveNulaLanzaExcepcion() {
            // Arrange / Act / Assert
            assertThrows(IllegalArgumentException.class,
                () -> cacheService.actualizar(null, "valor"));
        }

        @Test
        void actualizarErrorValorNuloLanzaExcepcion() {
            // Arrange
            cacheService.guardar("clave", "valorInicial");

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> cacheService.actualizar("clave", null));
        }
    }

    @Nested
    @DisplayName("eliminar()")
    class Eliminar {
        @BeforeEach
        void setUp() {
            cacheService = new CacheService<>(politicaExpulsion, 3);
        }

        @Test
        void eliminarCuandoEsexitoso() {
            // Arrange
            cacheService.guardar("clave", "valor");

            // Act
            cacheService.eliminar("clave");

            // Assert
            assertEquals(0, cacheService.verTamanioCache());
            assertEquals(1L, cacheService.estadisticas().getExpulsados());
            verify(politicaExpulsion, times(1)).registrarEliminacion("clave");
        }

        @Test
        void eliminarErrorClaveInexistenteLanzaExcepcion() {
            // Arrange
            String clave = "noExiste";

            // Act & Assert
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cacheService.eliminar(clave));
            assertTrue(ex.getMessage().contains("debe existir en la caché"));
        }

        @Test
        void eliminarErrorClaveNulaLanzaExcepcion() {
            // Arrange / Act / Assert
            assertThrows(IllegalArgumentException.class,
                () -> cacheService.eliminar(null));
        }
    }

    @Nested
    @DisplayName("verificarExistencia()")
    class VerificarExistencia {

        @BeforeEach
        void setUp() {
            cacheService = new CacheService<>(politicaExpulsion, 3);
        }

        @Test
        void verificarExistenciaCuandoRetornaTrue() {
            // Arrange
            cacheService.guardar("clave", "valor");

            // Act
            boolean resultado = cacheService.verificarExistencia("clave");

            // Assert
            assertTrue(resultado);
        }

        @Test
        void verificarExistenciaCuandoClaveInexistenteRetornaFalse() {
            // Arrange – caché vacía

            // Act
            boolean resultado = cacheService.verificarExistencia("noExiste");

            // Assert
            assertFalse(resultado);
        }

        @Test
        void verificarExistenciaEntradaExpiradaRetornaFalseYRegistraExpirado() throws Exception {
            // Arrange
            insertarEntradaExpirada("claveExp", "valorExp");

            // Act
            boolean resultado = cacheService.verificarExistencia("claveExp");

            // Assert
            assertFalse(resultado);
            assertEquals(1L, cacheService.estadisticas().getExpirados());
            verify(politicaExpulsion, times(1)).registrarEliminacion("claveExp");
        }

        @Test
        @DisplayName("Error - clave nula lanza IllegalArgumentException")
        void verificarExistenciaClaveNulaLanzaExcepcion() {
            // Arrange / Act / Assert
            assertThrows(IllegalArgumentException.class,
                () -> cacheService.verificarExistencia(null));
        }
    }

    @Nested
    @DisplayName("verTamanioCache()")
    class VerTamanioCache {

        @BeforeEach
        void setUp() {
            cacheService = new CacheService<>(politicaExpulsion, 3);
        }

        @Test
        void verTamanioCacheVaciaRetornaCero() {
            // Arrange – caché recién creada

            // Act
            int tamanio = cacheService.verTamanioCache();

            // Assert
            assertEquals(0, tamanio);
        }

        @Test
        void verTamanioConElementosRetornaTotalVigentes() {
            // Arrange
            cacheService.guardar("c1", "v1");
            cacheService.guardar("c2", "v2");

            // Act
            int tamanio = cacheService.verTamanioCache();

            // Assert
            assertEquals(2, tamanio);
        }

        @Test
        void verTamanioDescuentaExpirados() throws Exception {
            // Arrange
            cacheService.guardar("c1", "v1");
            insertarEntradaExpirada("cExp", "vExp");

            // Act
            int tamanio = cacheService.verTamanioCache();

            // Assert
            assertEquals(1, tamanio);
            assertEquals(1L, cacheService.estadisticas().getExpirados());
        }
    }

    @Nested
    @DisplayName("vaciarCache()")
    class VaciarCache {
        @BeforeEach
        void setUp() {
            cacheService = new CacheService<>(politicaExpulsion, 3);
        }

        @Test
        void vaciarCacheEliminaTodoYLimpiaPolitica() {
            // Arrange
            cacheService.guardar("c1", "v1");
            cacheService.guardar("c2", "v2");

            // Act
            cacheService.vaciarCache();

            // Assert
            assertEquals(0, cacheService.verTamanioCache());
            verify(politicaExpulsion, times(1)).limpiar();
        }

        @Test
        void vaciarCacheYaVaciaNoLanzaExcepcion() {
            // Act & Assert
            assertDoesNotThrow(() -> cacheService.vaciarCache());
            verify(politicaExpulsion, times(1)).limpiar();
        }
    }

    @Nested
    @DisplayName("estadisticas()")
    class EstadisticasTest {

        @BeforeEach
        void setUp() {
            cacheService = new CacheService<>(politicaExpulsion, 3);
        }

        @Test
        void estadisticasValoresInicialesEnCero() {
            // Arrange – servicio recién creado

            // Act
            Estadistica stats = cacheService.estadisticas();

            // Assert
            assertNotNull(stats);
            assertEquals(0L, stats.getHits());
            assertEquals(0L, stats.getMisses());
            assertEquals(0L, stats.getExpulsados());
            assertEquals(0L, stats.getExpirados());
            assertEquals(0.0, stats.getHitRatio());
            assertEquals(0.0, stats.getTiempoPromedio());
        }

        @Test
        void estadisticasHitRatioCorrecto() {
            // Arrange
            cacheService.guardar("c1", "v1");
            cacheService.buscarElemento("c1");                         // hit
            assertThrows(Exception.class,
                    () -> cacheService.buscarElemento("noExiste"));    // miss

            // Act
            Estadistica stats = cacheService.estadisticas();

            // Assert
            assertEquals(1L, stats.getHits());
            assertEquals(1L, stats.getMisses());
            assertEquals(0.5, stats.getHitRatio(), 0.001);
        }

        @Test
        void estadisticasTiempoPromedioCorrecto() {
            // Arrange
            cacheService.guardar("c1", "v1");
            cacheService.buscarElemento("c1");

            // Act
            Estadistica stats = cacheService.estadisticas();

            // Assert
            assertTrue(stats.getTiempoPromedio() > 0);
        }

        @Test
        void estadisticasExpulsadosIncrementaAlSuperar() {
            // Arrange
            cacheService.guardar("c1", "v1");
            cacheService.guardar("c2", "v2");
            cacheService.guardar("c3", "v3");
            when(politicaExpulsion.seleccionarClave()).thenReturn("c1");

            // Act
            cacheService.guardar("c4", "v4");

            // Assert
            assertEquals(1L, cacheService.estadisticas().getExpulsados());
        }

        @Test
        void constructorUnArgumentoCapacidad3() {
            // Arrange
            CacheService<String, String> servicio = new CacheService<>(politicaExpulsion);

            // Act
            servicio.guardar("c1", "v1");
            servicio.guardar("c2", "v2");
            servicio.guardar("c3", "v3");
            when(politicaExpulsion.seleccionarClave()).thenReturn("c1");
            servicio.guardar("c4", "v4");   // debe expulsar

            // Assert
            verify(politicaExpulsion, times(1)).seleccionarClave();
        }
    }
}
