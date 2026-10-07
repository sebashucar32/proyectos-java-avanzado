package org.example.controllers;

import org.example.models.Estadistica;
import org.example.services.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CacheController - Tests unitarios")
class CacheControllerTest {
    @Mock
    private Scanner teclado;

    @Mock
    private CacheService<String, String> cacheService;

    private CacheController cacheController;

    // ─── Helpers ────────────────────────────────────────────────────────────────

    /** Captura la salida estándar durante la ejecución de una acción. */
    private String capturarSalida(Runnable accion) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(buffer));
        try {
            accion.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString();
    }

    @BeforeEach
    void setUp() {
        cacheController = new CacheController(teclado, cacheService);
    }

    @Nested
    @DisplayName("guardarCache()")
    class GuardarCache {
        @Test
        @DisplayName("Éxito - lee clave y valor, delega en el servicio e imprime confirmación")
        void guardarCacheExitosoImprimeConfirmacion() {
            // Arrange
            when(teclado.nextLine()).thenReturn("clave1", "valor1");

            // Act
            String salida = capturarSalida(() -> cacheController.guardarCache());

            // Assert
            verify(cacheService, times(1)).guardar("clave1", "valor1");
            assertTrue(salida.contains("Elemento guardado correctamente."));
        }

        @Test
        void guardarCachErrorServicioLanzaExcepcionImprimeMensajeError() {
            // Arrange
            when(teclado.nextLine()).thenReturn("claveX", "valorX");
            doThrow(new IllegalArgumentException("La clave 'claveX' ya existe en la caché."))
                .when(cacheService).guardar("claveX", "valorX");

            // Act
            String salida = capturarSalida(() -> cacheController.guardarCache());

            // Assert
            assertTrue(salida.contains("ya existe en la caché"));
        }

        @Test
        void guardarCacheExcepcionSinMensajeImprimeMensajeGenerico() {
            // Arrange
            when(teclado.nextLine()).thenReturn("c", "v");
            doThrow(new RuntimeException((String) null))
                .when(cacheService).guardar("c", "v");

            // Act
            String salida = capturarSalida(() -> cacheController.guardarCache());

            // Assert
            assertTrue(salida.contains("Ocurrió un error inesperado."));
        }
    }

    @Nested
    @DisplayName("buscarElementoCache()")
    class BuscarElementoCache {
        @Test
        void buscarElementoCacheEntoncesimprimeValor() {
            // Arrange
            when(teclado.nextLine()).thenReturn("clave1");
            when(cacheService.buscarElemento("clave1")).thenReturn("valor1");

            // Act
            String salida = capturarSalida(() -> cacheController.buscarElementoCache());

            // Assert
            assertTrue(salida.contains("Valor encontrado: valor1"));
        }

        @Test
        void buscarElementoCacheErrorClaveInexistenteImprimeMensajeError() {
            // Arrange
            when(teclado.nextLine()).thenReturn("noExiste");
            when(cacheService.buscarElemento("noExiste"))
                .thenThrow(new IllegalArgumentException("La clave 'noExiste' no existe en la caché."));

            // Act
            String salida = capturarSalida(() -> cacheController.buscarElementoCache());

            // Assert
            assertTrue(salida.contains("no existe en la caché"));
        }

        @Test
        void buscarElementoCacheErrorExcepcionSinMensajeImprimeMensajeGenerico() {
            // Arrange
            when(teclado.nextLine()).thenReturn("c");
            when(cacheService.buscarElemento("c"))
                    .thenThrow(new RuntimeException((String) null));

            // Act
            String salida = capturarSalida(() -> cacheController.buscarElementoCache());

            // Assert
            assertTrue(salida.contains("Ocurrió un error inesperado."));
        }
    }

    @Nested
    @DisplayName("actualizarElementoCache()")
    class ActualizarElementoCache {
        @Test
        void actualizarElementoCacheExitosoImprimeConfirmacion() {
            // Arrange
            when(teclado.nextLine()).thenReturn("clave1", "nuevoValor");

            // Act
            String salida = capturarSalida(() -> cacheController.actualizarElementoCache());

            // Assert
            verify(cacheService, times(1)).actualizar("clave1", "nuevoValor");
            assertTrue(salida.contains("Elemento guardado correctamente."));
        }

        @Test
        void actualizarElementoCacheClaveInexistenteImprimeMensajeError() {
            // Arrange
            when(teclado.nextLine()).thenReturn("noExiste", "valor");
            doThrow(new IllegalArgumentException("La clave 'noExiste' debe existir en la caché."))
                    .when(cacheService).actualizar("noExiste", "valor");

            // Act
            String salida = capturarSalida(() -> cacheController.actualizarElementoCache());

            // Assert
            assertTrue(salida.contains("debe existir en la caché"));
        }

        @Test
        void actualizarElementoCacheErrorExcepcionSinMensajeImprimeMensajeGenerico() {
            // Arrange
            when(teclado.nextLine()).thenReturn("c", "v");
            doThrow(new RuntimeException((String) null))
                    .when(cacheService).actualizar("c", "v");

            // Act
            String salida = capturarSalida(() -> cacheController.actualizarElementoCache());

            // Assert
            assertTrue(salida.contains("Ocurrió un error inesperado."));
        }
    }

    @Nested
    @DisplayName("eliminarElementoCache()")
    class EliminarElementoCache {

        @Test
        void eliminarElementoCacheExitosoImprimeConfirmacion() {
            // Arrange
            when(teclado.nextLine()).thenReturn("clave1");

            // Act
            String salida = capturarSalida(() -> cacheController.eliminarElementoCache());

            // Assert
            verify(cacheService, times(1)).eliminar("clave1");
            assertTrue(salida.contains("Elemento eliminado correctamente."));
        }

        @Test
        void eliminarElementoCacheClaveInexistenteImprimeMensajeError() {
            // Arrange
            when(teclado.nextLine()).thenReturn("noExiste");
            doThrow(new IllegalArgumentException("La clave 'noExiste' debe existir en la caché."))
                    .when(cacheService).eliminar("noExiste");

            // Act
            String salida = capturarSalida(() -> cacheController.eliminarElementoCache());

            // Assert
            assertTrue(salida.contains("debe existir en la caché"));
        }

        @Test
        void eliminarElementoCacheErrorExcepcionSinMensajeImprimeMensajeGenerico() {
            // Arrange
            when(teclado.nextLine()).thenReturn("c");
            doThrow(new RuntimeException((String) null))
                    .when(cacheService).eliminar("c");

            // Act
            String salida = capturarSalida(() -> cacheController.eliminarElementoCache());

            // Assert
            assertTrue(salida.contains("Ocurrió un error inesperado."));
        }
    }

    @Nested
    @DisplayName("verificarExistenciaCache()")
    class VerificarExistenciaCache {
        @Test
        void verificarExistenciaCacheExisteImprimeMensajeExiste() {
            // Arrange
            when(teclado.nextLine()).thenReturn("clave1");
            when(cacheService.verificarExistencia("clave1")).thenReturn(true);

            // Act
            String salida = capturarSalida(() -> cacheController.verificarExistenciaCache());

            // Assert
            assertTrue(salida.contains("existe en la cache"));
        }

        @Test
        void verificarExistenciaCacheNoExisteImprimeMensajeNoExiste() {
            // Arrange
            when(teclado.nextLine()).thenReturn("noExiste");
            when(cacheService.verificarExistencia("noExiste")).thenReturn(false);

            // Act
            String salida = capturarSalida(() -> cacheController.verificarExistenciaCache());

            // Assert
            assertTrue(salida.contains("No existe el elemento solicitado"));
        }

        @Test
        void verificarExistenciaCacheServicioLanzaExcepcionImprimeMensajeError() {
            // Arrange
            when(teclado.nextLine()).thenReturn("claveRota");
            when(cacheService.verificarExistencia("claveRota"))
                    .thenThrow(new IllegalArgumentException("La clave no puede ser nula"));

            // Act
            String salida = capturarSalida(() -> cacheController.verificarExistenciaCache());

            // Assert
            assertTrue(salida.contains("La clave no puede ser nula"));
        }

        @Test
        void verificarExistenciaCacheErrorExcepcionSinMensajeImprimeMensajeGenerico() {
            // Arrange
            when(teclado.nextLine()).thenReturn("c");
            when(cacheService.verificarExistencia("c"))
                    .thenThrow(new RuntimeException((String) null));

            // Act
            String salida = capturarSalida(() -> cacheController.verificarExistenciaCache());

            // Assert
            assertTrue(salida.contains("Ocurrió un error inesperado."));
        }
    }

    @Nested
    @DisplayName("tamanioElementosCache()")
    class TamanioElementosCache {
        @Test
        void tamanioElementosCacheConElementosImprimeTotalElementos() {
            // Arrange
            when(cacheService.verTamanioCache()).thenReturn(5);

            // Act
            String salida = capturarSalida(() -> cacheController.tamanioElementosCache());

            // Assert
            assertTrue(salida.contains("Total de elementos en cache: 5"));
        }

        @Test
        void tamanioElementosCacheCachVaciaImprimeSinElementos() {
            // Arrange
            when(cacheService.verTamanioCache()).thenReturn(0);

            // Act
            String salida = capturarSalida(() -> cacheController.tamanioElementosCache());

            // Assert
            assertTrue(salida.contains("No existen elementos en cache en este momento"));
        }
    }

    @Nested
    @DisplayName("vaciarElementosCache()")
    class VaciarElementosCache {
        @Test
        void vaciarElementosCacheExitosoImprimeConfirmacion() {
            // Arrange – sin configuración adicional, vaciarCache() no lanza excepción

            // Act
            String salida = capturarSalida(() -> cacheController.vaciarElementosCache());

            // Assert
            verify(cacheService, times(1)).vaciarCache();
            assertTrue(salida.contains("Se ha limpiado toda la cache disponible"));
        }
    }

    @Nested
    @DisplayName("estadisticas()")
    class Estadisticas {
        @Test
        void estadisticasExitosoImprimeTodosLosCampos() {
            // Arrange
            Estadistica stats = new Estadistica(10L, 2L, 5000L, 1L, 3L);
            when(cacheService.estadisticas()).thenReturn(stats);

            // Act
            String salida = capturarSalida(() -> cacheController.estadisticas());

            // Assert
            assertAll(
                () -> assertTrue(salida.contains("Hits: 10")),
                () -> assertTrue(salida.contains("Misses: 2")),
                () -> assertTrue(salida.contains("Expulsados: 1")),
                () -> assertTrue(salida.contains("Expirados: 3")),
                () -> assertTrue(salida.contains("Hit Ratio:")),
                () -> assertTrue(salida.contains("Tiempo promedio:"))
            );
        }

        @Test
        void estadisticasTodoEnCeroNoLanzaExcepcion() {
            // Arrange
            Estadistica statsVacias = new Estadistica(0L, 0L, 0L, 0L, 0L);
            when(cacheService.estadisticas()).thenReturn(statsVacias);

            // Act & Assert
            assertDoesNotThrow(() -> {
                String salida = capturarSalida(() -> cacheController.estadisticas());
                assertTrue(salida.contains("Hits: 0"));
                assertTrue(salida.contains("Misses: 0"));
            });
        }
    }
}
