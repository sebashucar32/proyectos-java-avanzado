package org.example.util;

import org.example.policies.FifoPolicy;
import org.example.policies.LfuPolicy;
import org.example.policies.LruPolicy;
import org.example.repositories.PoliticaExpulsion;
import org.example.services.CacheService;

public class CacheBenchmark {

    private static final int WARMUP_OPS   = 5_000;
    private static final int MEDICION_OPS = 50_000;
    private static final int CAPACIDAD    = 500;

    public static void main(String[] args) {
        System.out.println("=== FastCache Benchmark ===\n");
        System.out.printf("%-6s | %-14s | %-24s | %s%n",
                "Policy", "Total (ms)", "Promedio por op (ns)", "Hit ratio");
        System.out.println("-".repeat(65));

        ejecutar("FIFO", new FifoPolicy<>());
        ejecutar("LRU",  new LruPolicy<>());
        ejecutar("LFU",  new LfuPolicy<>());
    }

    private static void ejecutar(String nombre, PoliticaExpulsion<String> politica) {
        CacheService<String, String> cache = new CacheService<>(politica, CAPACIDAD);

        // Warmup: permite que el JIT compile el código caliente antes de medir
        correrOperaciones(cache, WARMUP_OPS);
        cache.vaciarCache();

        // Medición real
        long inicio = System.nanoTime();
        correrOperaciones(cache, MEDICION_OPS);
        long fin = System.nanoTime();

        double totalMs    = (fin - inicio) / 1_000_000.0;
        double promedioNs = (double)(fin - inicio) / MEDICION_OPS;

        System.out.printf("%-6s | %14.2f | %24.1f | %.4f%n",
                nombre, totalMs, promedioNs, cache.estadisticas().getHitRatio());
    }

    /**
     * Simula una carga mixta: ~33% misses (ratio clave/capacidad = 1.5).
     * Para cada iteración intenta buscar; si no existe, guarda.
     */
    private static void correrOperaciones(CacheService<String, String> cache, int ops) {
        for (int i = 0; i < ops; i++) {
            // El módulo sobre 1.5x la capacidad garantiza que no todas las claves caben
            String clave = "key-" + (i % (int)(CAPACIDAD * 1.5));
            try {
                if (!cache.verificarExistencia(clave)) {
                    cache.guardar(clave, "valor-" + i);
                } else {
                    cache.buscarElemento(clave);
                }
            } catch (Exception ignored) {}
        }
    }
}
