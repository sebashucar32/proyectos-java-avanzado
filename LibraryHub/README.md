#Contexto

Una universidad desea reemplazar su antiguo sistema de préstamos de libros por una aplicación moderna escrita en Java.
El sistema será utilizado por los bibliotecarios para administrar libros, usuarios y préstamos.
No se utilizará ninguna base de datos; toda la información permanecerá en memoria.

#Requisitos Funcionales
Gestion de libros: El sistema debe permitir
  - Registrar libros.
  - Editar información.
  - Eliminar libros.
  - Buscar por ISBN.
  - Buscar por autor.
  - Buscar por categoría.
  - Buscar por título.
  - Listar disponibles.
  - Listar prestados.

Cada libro debe tener
  - ISBN
  - Título
  - Autor
  - Año
  - Categoría
  - Estado

USUARIOS

El sistema debe permitir:

Registrar usuarios.
Modificar usuarios.
Eliminar usuarios.
Buscar usuarios.

Debe existir como mínimo:

Estudiante.
Profesor.

REGLAS DE PRÉSTAMO

Un estudiante puede tener máximo:

3 libros.

Un profesor puede tener máximo:

10 libros.

No se puede prestar un libro que ya está prestado.

PRÉSTAMOS

Implementar:

Crear préstamo.
Devolver libro.
Renovar préstamo.

Cada préstamo debe manejar:

Libro.
Usuario.
Fecha de inicio.
Fecha de vencimiento.
Fecha de devolución.
Estado.

MULTAS

Si un libro es devuelto después de la fecha límite:

Debe calcularse una multa.
Debe registrarse.
Debe poder consultarse.

REPORTES

El sistema debe poder mostrar:

Libro más solicitado.
Usuario con más préstamos.
Libros disponibles.
Libros prestados.
Libros vencidos.
Usuarios con multas.
Cantidad total de préstamos.
Cantidad de libros disponibles.

CONCEPTOS OBLIGATORIOS

Debes utilizar y practicar:

Clases.
Interfaces.
Encapsulación.
Composición.
Herencia cuando tenga sentido.
Polimorfismo.
Enum.
Record.
Optional.
equals().
hashCode().
Collections.
Excepciones personalizadas.
Inmutabilidad.
SOLID.

RETO DE DISEÑO

Debes evitar crear una única clase gigante que haga todo.

Separar responsabilidades.

Debes poder explicar:

¿Por qué una clase tiene determinada responsabilidad?

¿Por qué utilizaste una interfaz?

¿Por qué utilizaste composición o herencia?

¿Por qué utilizaste determinada Collection?

BONUS

Agregar persistencia en JSON.


Uso de principios Solid
- S: Se maeja la responsabilidad unica cuando son repositorios, servicios y controladores.
- O: un servicio nuevo o un MemoriaLibroRepository se engancha por la interfaz, sin reescribir LibroService.
- L: Json*Repository puede usarse donde se espera ComunRepository / la interfaz de repositorio.
- I: cada repositorio tiene operaciones de su agregado, no un IRepository gigante con 40 métodos.
- D: PrestamoService recibe abstracciones; PrestamoConfig crea las implementaciones JSON. 
- Eso es inyección de dependencias manual (sin Spring).

Se maneja la inmutabilidad usando el record para las multas, se cumple con cada uno de los conceptos que se deben utilizar en este proyecto
tambien se trato de hacer una prueba con una api grafica llamada javafx de momento solo se creo para algunas secciones de
reportes, y por ultimo se logro implementar algunas pruebas unitarias necesarias para el funcionamiento del codigo.








