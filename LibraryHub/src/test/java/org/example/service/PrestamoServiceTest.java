package org.example.service;

import org.example.enums.EstadoLibro;
import org.example.enums.TipoUsuario;
import org.example.exception.LibroNoDisponibleException;
import org.example.exception.LibroNoEncontrado;
import org.example.exception.LimitePrestamosException;
import org.example.exception.PrestamoNoEncontrado;
import org.example.exception.UsuarioNoEncontrado;
import org.example.models.Libro;
import org.example.models.Prestamo;
import org.example.models.Usuario;
import org.example.records.Multa;
import org.example.repository.LibroRepository;
import org.example.repository.MultaRepository;
import org.example.repository.PrestamoRepository;
import org.example.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private LibroRepository libroRepository;
    @Mock
    private MultaRepository multaRepository;
    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private PrestamoService prestamoService;

    @Test
    void calcularUsuarioCuandoExiste() {
        Usuario usuario = crearUsuario(1, "Ana", TipoUsuario.Estudiante);
        when(usuarioRepository.listar()).thenReturn(List.of(usuario));

        Usuario encontrado = prestamoService.calcularUsuario("Ana");

        assertEquals(usuario, encontrado);
    }

    @Test
    void calcularUsuarioCuandoNoExiste() {
        when(usuarioRepository.listar()).thenReturn(List.of());

        assertThrows(UsuarioNoEncontrado.class, () -> prestamoService.calcularUsuario("Inexistente"));
    }

    @Test
    void calcularLibroCuandoExisteIgnorandoMayusculas() {
        Libro libro = crearLibro(10, "Don Quijote", EstadoLibro.DISPONIBLE);
        when(libroRepository.listar()).thenReturn(List.of(libro));

        Libro encontrado = prestamoService.calcularLibro("don quijote");

        assertEquals(libro, encontrado);
    }

    @Test
    void calcularLibroCuandoNoExiste() {
        when(libroRepository.listar()).thenReturn(List.of());

        assertThrows(LibroNoEncontrado.class, () -> prestamoService.calcularLibro("Titulo inexistente"));
    }

    @Test
    void validarLibrosDisponiblesCuandoYaEstaPrestado() {
        Libro libro = crearLibro(10, "Don Quijote", EstadoLibro.PRESTADO);

        assertThrows(LibroNoDisponibleException.class, () -> prestamoService.validarLibrosDisponibles(libro));
    }

    @Test
    void registrarCuandoSeRegistraCorrectamente() {
        Usuario usuario = crearUsuario(1, "Ana", TipoUsuario.Estudiante);
        Libro libro = crearLibro(10, "Don Quijote", EstadoLibro.DISPONIBLE);
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate vencimiento = LocalDate.of(2026, 1, 15);

        when(usuarioRepository.listar()).thenReturn(List.of(usuario));
        when(libroRepository.listar()).thenReturn(List.of(libro));
        when(prestamoRepository.listar()).thenReturn(List.of());

        prestamoService.registrar(5, "Don Quijote", "Ana", inicio, vencimiento);

        verify(libroRepository).editar(eq(10), any(Libro.class));
        assertEquals(EstadoLibro.PRESTADO, libro.getEstado());

        ArgumentCaptor<Prestamo> captor = ArgumentCaptor.forClass(Prestamo.class);
        verify(prestamoRepository).registrar(captor.capture());

        Prestamo registrado = captor.getValue();
        assertEquals(5, registrado.getId());
        assertEquals(libro, registrado.getLibro());
        assertEquals(usuario, registrado.getUsuario());
        assertEquals(inicio, registrado.getFechaInicio());
        assertEquals(vencimiento, registrado.getFechaVencimiento());
        assertNull(registrado.getFechaDevolucion());
    }

    @Test
    void registrarCuandoElLibroNoEstaDisponible() {
        Usuario usuario = crearUsuario(1, "Ana", TipoUsuario.Estudiante);
        Libro libro = crearLibro(10, "Don Quijote", EstadoLibro.PRESTADO);

        when(usuarioRepository.listar()).thenReturn(List.of(usuario));
        when(libroRepository.listar()).thenReturn(List.of(libro));
        when(prestamoRepository.listar()).thenReturn(List.of());

        assertThrows(LibroNoDisponibleException.class, () ->
            prestamoService.registrar(5, "Don Quijote", "Ana",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 15)));

        verify(prestamoRepository, never()).registrar(any());
        verify(libroRepository, never()).editar(anyInt(), any());
    }

    @Test
    void registrarCuandoElEstudianteAlcanzaElLimite() {
        Usuario usuario = crearUsuario(1, "Ana", TipoUsuario.Estudiante);
        Libro libro = crearLibro(40, "Nuevo libro", EstadoLibro.DISPONIBLE);
        List<Prestamo> prestamosActivos = List.of(
            crearPrestamo(1, usuario, crearLibro(10, "Libro 1", EstadoLibro.PRESTADO),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), null),
            crearPrestamo(2, usuario, crearLibro(20, "Libro 2", EstadoLibro.PRESTADO),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), null),
            crearPrestamo(3, usuario, crearLibro(30, "Libro 3", EstadoLibro.PRESTADO),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), null)
        );

        when(usuarioRepository.listar()).thenReturn(List.of(usuario));
        when(libroRepository.listar()).thenReturn(List.of(libro));
        when(prestamoRepository.listar()).thenReturn(prestamosActivos);

        assertThrows(LimitePrestamosException.class, () ->
            prestamoService.registrar(4, "Nuevo libro", "Ana",
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 15)));

        verify(prestamoRepository, never()).registrar(any());
    }

    @Test
    void devolverCuandoEsATiempoNoGeneraMulta() {
        Usuario usuario = crearUsuario(1, "Ana", TipoUsuario.Estudiante);
        Libro libro = crearLibro(10, "Don Quijote", EstadoLibro.PRESTADO);
        Prestamo prestamo = crearPrestamo(5, usuario, libro,
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), null);
        LocalDate fechaDevolucion = LocalDate.of(2026, 1, 10);

        when(usuarioRepository.listar()).thenReturn(List.of(usuario));
        when(libroRepository.listar()).thenReturn(List.of(libro));
        when(prestamoRepository.listar()).thenReturn(List.of(prestamo));
        doAnswer(invocation -> {
            Prestamo actual = invocation.getArgument(0);
            actual.setFechaDevolucion(invocation.getArgument(1));
            return null;
        }).when(prestamoRepository).devolver(any(), any());

        prestamoService.devolver("Ana", "Don Quijote", fechaDevolucion);

        assertEquals(EstadoLibro.DISPONIBLE, libro.getEstado());
        verify(libroRepository).editar(10, libro);
        verify(prestamoRepository).devolver(prestamo, fechaDevolucion);
        verify(multaRepository, never()).registrar(any());
    }

    @Test
    void devolverCuandoHayRetrasoRegistraMulta() {
        Usuario usuario = crearUsuario(1, "Ana", TipoUsuario.Estudiante);
        Libro libro = crearLibro(10, "Don Quijote", EstadoLibro.PRESTADO);
        Prestamo prestamo = crearPrestamo(5, usuario, libro,
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), null);
        LocalDate fechaDevolucion = LocalDate.of(2026, 1, 13);

        when(usuarioRepository.listar()).thenReturn(List.of(usuario));
        when(libroRepository.listar()).thenReturn(List.of(libro));
        when(prestamoRepository.listar()).thenReturn(List.of(prestamo));
        doAnswer(invocation -> {
            Prestamo actual = invocation.getArgument(0);
            actual.setFechaDevolucion(invocation.getArgument(1));
            return null;
        }).when(prestamoRepository).devolver(any(), any());

        prestamoService.devolver("Ana", "Don Quijote", fechaDevolucion);

        ArgumentCaptor<Multa> captor = ArgumentCaptor.forClass(Multa.class);
        verify(multaRepository).registrar(captor.capture());

        Multa multa = captor.getValue();
        assertEquals(3, multa.diasRetraso());
        assertEquals(15000.0, multa.valor());
        assertEquals("Ana", multa.nombreUsuario());
    }

    @Test
    void devolverCuandoNoExistePrestamo() {
        Usuario usuario = crearUsuario(1, "Ana", TipoUsuario.Estudiante);
        Libro libro = crearLibro(10, "Don Quijote", EstadoLibro.PRESTADO);

        when(usuarioRepository.listar()).thenReturn(List.of(usuario));
        when(libroRepository.listar()).thenReturn(List.of(libro));
        when(prestamoRepository.listar()).thenReturn(List.of());

        assertThrows(PrestamoNoEncontrado.class,
            () -> prestamoService.devolver("Ana", "Don Quijote", LocalDate.of(2026, 1, 10)));
    }

    @Test
    void renovarCuandoExistePrestamo() {
        Usuario usuario = crearUsuario(1, "Ana", TipoUsuario.Estudiante);
        Libro libro = crearLibro(10, "Don Quijote", EstadoLibro.PRESTADO);
        Prestamo prestamo = crearPrestamo(5, usuario, libro,
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), null);
        LocalDate nuevaFecha = LocalDate.of(2026, 1, 25);

        when(usuarioRepository.listar()).thenReturn(List.of(usuario));
        when(libroRepository.listar()).thenReturn(List.of(libro));
        when(prestamoRepository.listar()).thenReturn(List.of(prestamo));

        prestamoService.renovar("Ana", "Don Quijote", nuevaFecha);

        verify(prestamoRepository).renovar(prestamo, nuevaFecha);
    }

    @Test
    void consultarActivosCuandoHayDevueltosYPendientes() {
        Usuario usuario = crearUsuario(1, "Ana", TipoUsuario.Estudiante);
        Prestamo activo = crearPrestamo(1, usuario, crearLibro(10, "Libro A", EstadoLibro.PRESTADO),
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), null);
        Prestamo devuelto = crearPrestamo(2, usuario, crearLibro(20, "Libro B", EstadoLibro.DISPONIBLE),
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 8));

        when(prestamoRepository.listar()).thenReturn(List.of(activo, devuelto));

        List<Prestamo> activos = prestamoService.consultarActivos();

        assertEquals(List.of(activo), activos);
    }

    @Test
    void consultarHistorialDelegarEnElRepositorio() {
        Prestamo prestamo = crearPrestamo(1, crearUsuario(1, "Ana", TipoUsuario.Estudiante),
            crearLibro(10, "Libro A", EstadoLibro.PRESTADO),
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), null);
        when(prestamoRepository.listar()).thenReturn(List.of(prestamo));

        List<Prestamo> historial = prestamoService.consultarHistorial();

        assertEquals(List.of(prestamo), historial);
    }

    @Test
    void obtenerMultasDelegarEnElRepositorio() {
        List<Multa> multas = List.of(new Multa(2, 10000.0, "Ana"));
        when(multaRepository.listar()).thenReturn(multas);

        assertEquals(multas, prestamoService.obtenerMultas());
    }

    @Test
    void obtenerVencidosCuandoLaDevolucionEsPosteriorAlVencimiento() {
        Usuario usuario = crearUsuario(1, "Ana", TipoUsuario.Estudiante);
        Prestamo vencido = crearPrestamo(1, usuario, crearLibro(10, "Libro A", EstadoLibro.DISPONIBLE),
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 12));
        Prestamo aTiempo = crearPrestamo(2, usuario, crearLibro(20, "Libro B", EstadoLibro.DISPONIBLE),
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 10));
        Prestamo activo = crearPrestamo(3, usuario, crearLibro(30, "Libro C", EstadoLibro.PRESTADO),
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), null);

        when(prestamoRepository.listar()).thenReturn(List.of(vencido, aTiempo, activo));

        assertEquals(List.of(vencido), prestamoService.obtenerVencidos());
    }

    @Test
    void usuarioConMasPrestamosCuandoHayUnMaximo() {
        Usuario ana = crearUsuario(1, "Ana", TipoUsuario.Estudiante);
        Usuario luis = crearUsuario(2, "Luis", TipoUsuario.Profesor);

        when(prestamoRepository.listar()).thenReturn(List.of(
            crearPrestamo(1, ana, crearLibro(10, "Libro A", EstadoLibro.PRESTADO),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), null),
            crearPrestamo(2, ana, crearLibro(20, "Libro B", EstadoLibro.PRESTADO),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), null),
            crearPrestamo(3, luis, crearLibro(30, "Libro C", EstadoLibro.PRESTADO),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), null)
        ));

        List<Usuario> resultado = prestamoService.usuarioConMasPrestamos();

        assertEquals(List.of(ana), resultado);
    }

    @Test
    void usuarioConMasPrestamosCuandoNoHayPrestamos() {
        when(prestamoRepository.listar()).thenReturn(List.of());

        assertTrue(prestamoService.usuarioConMasPrestamos().isEmpty());
    }

    @Test
    void libroMasSolicitadoCuandoHayUnMaximo() {
        Libro quijote = crearLibro(10, "Don Quijote", EstadoLibro.PRESTADO);
        Libro otro = crearLibro(20, "Otro", EstadoLibro.PRESTADO);
        Usuario usuario = crearUsuario(1, "Ana", TipoUsuario.Estudiante);

        when(prestamoRepository.listar()).thenReturn(List.of(
            crearPrestamo(1, usuario, quijote, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), null),
            crearPrestamo(2, usuario, quijote, LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 10), null),
            crearPrestamo(3, usuario, otro, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 10), null)
        ));

        List<Libro> resultado = prestamoService.libroMasSolicitado();

        assertEquals(List.of(quijote), resultado);
    }

    private static Usuario crearUsuario(int id, String nombre, TipoUsuario tipoUsuario) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre(nombre);
        usuario.setTipoUsuario(tipoUsuario);
        return usuario;
    }

    private static Libro crearLibro(int isbn, String titulo, EstadoLibro estado) {
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAutor("Autor");
        libro.setAnio(2020);
        libro.setCategoria("Ensayo");
        libro.setEstado(estado);
        return libro;
    }

    private static Prestamo crearPrestamo(int id, Usuario usuario, Libro libro,
                                          LocalDate inicio, LocalDate vencimiento, LocalDate devolucion) {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(id);
        prestamo.setUsuario(usuario);
        prestamo.setLibros(libro);
        prestamo.setFechaInicio(inicio);
        prestamo.setFechaVencimiento(vencimiento);
        prestamo.setFechaDevolucion(devolucion);
        return prestamo;
    }
}
