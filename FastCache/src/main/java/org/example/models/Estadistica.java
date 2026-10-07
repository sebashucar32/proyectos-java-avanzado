package org.example.models;

public class Estadistica {
    private Long hits;
    private Long misses;
    private Long tiempoAcceso;
    private Long expulsados;
    private Long expirados;

    public Estadistica(Long hits, Long misses, Long tiempoAcceso,
                       Long expulsados, Long expirados) {
        this.hits = hits;
        this.misses = misses;
        this.tiempoAcceso = tiempoAcceso;
        this.expulsados = expulsados;
        this.expirados = expirados;
    }

    public double getHitRatio() {
        long total = hits + misses;

        if (total == 0) {
            return 0;
        }

        return (double) hits / total;
    }

    public double getTiempoPromedio() {
        long totalAccesos = hits + misses;

        if (totalAccesos == 0) {
            return 0;
        }

        return (double) tiempoAcceso / totalAccesos;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public Long getMisses() {
        return misses;
    }

    public void setMisses(Long misses) {
        this.misses = misses;
    }

    public Long getTiempoAcceso() {
        return tiempoAcceso;
    }

    public void setTiempoAcceso(Long tiempoAcceso) {
        this.tiempoAcceso = tiempoAcceso;
    }

    public Long getExpulsados() {
        return expulsados;
    }

    public void setExpulsados(Long expulsados) {
        this.expulsados = expulsados;
    }

    public Long getExpirados() {
        return expirados;
    }

    public void setExpirados(Long expirados) {
        this.expirados = expirados;
    }
}