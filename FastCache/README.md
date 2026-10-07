Anotacion Big O: sirve para indicar a que orden de complejidad pertenece tu funcion
tambien llamada cota superior asintótica, existen varios tipos de anotaciones que serian 
las siguientes O(1), O(log(n)), O(n), O(n*log(n)), O(n^2) entre otras

Hashing
- hash:
- hashCode():
- bucket: son los cajones se podria decir que tiene una tabla hash si hay capacidad para guardar
por ejemplo 12 datos entonces son 12 buckets lo que existe
- colisión: La colisión es cuando se trata de meter mas de 2 datos en un solo bucket, esto se
puede solucionar con una lista enlazada

PROYECTO 2
FASTCACHE
MOTOR DE CACHÉ

Nivel:
Colecciones y estructuras de datos.

Tipo:
Aplicación Java en memoria.

CONTEXTO

Una empresa necesita desarrollar su propio motor de caché para acelerar aplicaciones internas.

No se permite utilizar librerías externas de caché.

OBJETIVO

Crear una caché genérica capaz de almacenar:

Clave -> Valor

FUNCIONES BÁSICAS

Implementar:

Insertar.
Obtener.
Actualizar.
Eliminar.
Comprobar existencia.
Obtener tamaño.
Vaciar caché.

LRU

Implementar una política:

Least Recently Used.

Cuando la caché alcance su capacidad máxima debe eliminar el elemento utilizado menos recientemente.

LFU

Implementar:

Least Frequently Used.

Debe eliminar el elemento utilizado con menor frecuencia.

FIFO

Implementar:

First In First Out.

Debe eliminar primero el elemento que entró primero.

TTL

Cada elemento puede tener:

Time To Live.

Cuando expire:

El elemento deja de ser válido.
No debe devolverse al usuario.

ESTADÍSTICAS

Registrar:

Hits.
Misses.
Hit ratio.
Número de elementos.
Tiempo promedio de acceso.
Elementos expulsados.
Elementos expirados.