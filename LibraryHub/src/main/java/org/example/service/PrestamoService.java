package org.example.service;

import org.example.enums.EstadoLibro;
import org.example.enums.TipoUsuario;
import org.example.exception.*;
import org.example.models.Libro;
import org.example.models.Prestamo;
import org.example.models.Usuario;
import org.example.records.Multa;
import org.example.repository.LibroRepository;
import org.example.repository.MultaRepository;
import org.example.repository.PrestamoRepository;
import org.example.repository.UsuarioRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PrestamoService {
    private static final String NO_ENCONTRADO = "Usuario no encontrado: ";
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;
    private final MultaRepository multaRepository;
    private final PrestamoRepository prestamoRepository;

    public PrestamoService(UsuarioRepository usuarioRepository, LibroRepository libroRepository,
           MultaRepository multaRepository, PrestamoRepository prestamoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
        this.multaRepository = multaRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public Usuario calcularUsuario(String nombreUsuario) {
        return usuarioRepository.listar().stream()
            .filter(u -> u.getNombre().equals(nombreUsuario))
            .findFirst()
            .orElseThrow(() -> new UsuarioNoEncontrado(NO_ENCONTRADO + nombreUsuario));
    }

    public Libro calcularLibro(String libro) {
        return this.libroRepository.listar().stream()
            .filter(l -> l.getTitulo().equalsIgnoreCase(libro))
            .findFirst()
            .orElseThrow(() -> new LibroNoEncontrado("Libro no encontrado no esta registrado"));
    }

    private List<Prestamo> buscarPrestamosPorNombreUsuario(String nombreUsuario) {
        return prestamoRepository.listar().stream()
            .filter(prestamo -> prestamo.getUsuario().getNombre().equalsIgnoreCase(nombreUsuario))
            .toList();
    }

    private void validarCantidadPrestamosPorUsuario(Usuario usuario) {
        TipoUsuario tipoUsuario = usuario.getTipoUsuario();
        int cantidadPrestamos = buscarPrestamosPorNombreUsuario(usuario.getNombre()).size();

        int limite = switch (tipoUsuario) {
            case Estudiante -> 3;
            case Profesor -> 10;
        };

        if (cantidadPrestamos >= limite) {
            throw new LimitePrestamosException(
                "El usuario " + usuario.getNombre()
                + " no puede solicitar más de "
                + limite + " libros."
            );
        }
    }

    public void validarLibrosDisponibles(Libro libro) {
        boolean noDisponible = libro.getEstado().equals(EstadoLibro.PRESTADO);

        if (noDisponible) {
            throw new LibroNoDisponibleException(
                "No se pueden prestar libros que ya se encuentren en prestamo"
            );
        }
    }

    public void registrar(int id, String libro, String nombreUsuario, LocalDate fechaIngreso,
                          LocalDate fechaVencimiento) {
        var prestamo = new Prestamo();

        Usuario usuario = calcularUsuario(nombreUsuario);
        Libro libroDisponible = calcularLibro(libro);

        validarCantidadPrestamosPorUsuario(usuario);
        validarLibrosDisponibles(libroDisponible);

        libroDisponible.setEstado(EstadoLibro.PRESTADO);
        libroRepository.editar(libroDisponible.getIsbn(), libroDisponible);

        prestamo.setId(id);
        prestamo.setLibros(libroDisponible);
        prestamo.setUsuario(usuario);
        prestamo.setFechaInicio(fechaIngreso);
        prestamo.setFechaVencimiento(fechaVencimiento);
        prestamo.setFechaDevolucion(null);

        prestamoRepository.registrar(prestamo);
    }

    public Prestamo buscarPrestamoPorUsuarioYLibro(Usuario usuario, Libro libro) {
        return prestamoRepository.listar().stream()
            .filter(u -> Objects.equals(u.getUsuario().getId(), usuario.getId()))
            .filter(l -> Objects.equals(l.getLibro().getIsbn(), libro.getIsbn()))
            .findFirst()
            .orElseThrow(() -> new PrestamoNoEncontrado("No existe un prestamo que tenga un libro y un usuario asociado"));
    }

    public void validarSiHayMultas(Usuario usuario, Libro libro) {
        Prestamo prestamo = buscarPrestamoPorUsuarioYLibro(usuario, libro);

        if (prestamo.getFechaDevolucion().isAfter(prestamo.getFechaVencimiento())) {
            long diferencia = ChronoUnit.DAYS.between(prestamo.getFechaVencimiento(), prestamo.getFechaDevolucion());
            double valorMulta = 5000 * diferencia;
            String nombreUsuario = prestamo.getUsuario().getNombre();

            Multa multa = new Multa(diferencia, valorMulta, nombreUsuario);

            this.multaRepository.registrar(multa);
        }
    }

    public void devolver(String nombreUsuario, String nombreLibro, LocalDate fechaDevolucion) {
        Usuario usuario = calcularUsuario(nombreUsuario);
        Libro libro = calcularLibro(nombreLibro);
        Prestamo prestamo = buscarPrestamoPorUsuarioYLibro(usuario, libro);

        libro.setEstado(EstadoLibro.DISPONIBLE);
        libroRepository.editar(libro.getIsbn(), libro);
        prestamo.setLibros(libro);

        prestamoRepository.devolver(prestamo, fechaDevolucion);
        validarSiHayMultas(usuario, libro);
    }

    public void renovar(String nombreUsuario, String nombreLibro, LocalDate fechaRenovacion) {
        Usuario usuario = calcularUsuario(nombreUsuario);
        Libro libro = calcularLibro(nombreLibro);
        Prestamo prestamo = buscarPrestamoPorUsuarioYLibro(usuario, libro);

        prestamoRepository.renovar(prestamo, fechaRenovacion);
    }

    public List<Prestamo> consultarActivos() {
        return prestamoRepository.listar().stream()
            .filter(prestamo -> prestamo.getFechaDevolucion() == null).toList();
    }

    public List<Prestamo> consultarHistorial() {
        return prestamoRepository.listar();
    }

    public List<Multa> obtenerMultas() {
        return multaRepository.listar();
    }

    public List<Prestamo> obtenerVencidos() {
        return prestamoRepository.listar().stream()
            .filter(p -> p.getFechaDevolucion() != null)
            .filter(p -> p.getFechaDevolucion().isAfter(p.getFechaVencimiento()))
            .toList();
    }

    public List<Usuario> usuarioConMasPrestamos() {
        var cantidadUsuarios = prestamoRepository.listar().stream()
            .map(Prestamo::getUsuario)
            .collect(Collectors.groupingBy(
                usuario -> usuario,
                Collectors.counting()
            ));

        long maxRepeticiones = cantidadUsuarios.values()
            .stream()
            .max(Long::compare)
            .orElse(0L);

        return cantidadUsuarios.entrySet()
            .stream()
            .filter(entry -> entry.getValue() == maxRepeticiones)
            .map(Map.Entry::getKey)
            .toList();
    }

    public List<Libro> libroMasSolicitado() {
        var cantidadLibros = prestamoRepository.listar().stream()
            .map(Prestamo::getLibro)
            .collect(Collectors.groupingBy(
                titulo -> titulo,
                Collectors.counting()
            ));

        long maxRepeticiones = cantidadLibros.values()
            .stream()
            .max(Long::compare)
            .orElse(0L);

        return cantidadLibros.entrySet()
            .stream()
            .filter(entry -> entry.getValue() == maxRepeticiones)
            .map(Map.Entry::getKey)
            .toList();
    }
}
