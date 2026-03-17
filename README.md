# Mini Web Framework en Java

Framework HTTP simple en Java para:

- Definir servicios REST `GET` con lambdas.
- Construir aplicaciones web a partir de POJOs anotados con `@RestController`.
- Extraer query parameters con `HttpRequest`.
- Servir archivos estáticos desde `target/classes`.

## Arquitectura

- `com.adojos.app.http`: servidor HTTP, request/response, archivos estáticos y handlers.
- `com.adojos.app.context`: contenedor ligero por anotaciones y descubrimiento en classpath.
- `com.adojos.app.annotations`: anotaciones del microframework.
- `com.adojos.app.controllers`: controladores POJO descubiertos automáticamente.
- `com.adojos.app`: puntos de entrada del framework (`App`, `ManualRoutesApp`, `CommandLineApp`).
- `com.adojos.app.examples`: utilidades de red/socket de apoyo.

## Características implementadas

1. `HttpServer.get(path, (req, res) -> ...)` para endpoints manuales
2. Descubrimiento automático de clases anotadas con `@RestController`
3. Ejecucion por linea de comandos de un controlador y una ruta especifica
4. Soporte para `@GetMapping("/ruta")` con retorno `String`
5. Soporte para `@RequestParam(value = "name", defaultValue = "World")`
6. Servidor concurrente con pool fijo de workers para atender multiples clientes al mismo tiempo
7. `HttpServer.staticfiles("webroot/public")` (también soporta `"/webroot/public"`)
8. Servidor en `http://localhost:8080`
9. Entrega de archivos estáticos cuando no hay endpoint dinámico

## Modelo de concurrencia

El servidor usa un modelo `acceptor + worker pool`:

1. El hilo principal queda bloqueado en `ServerSocket.accept()`.
2. Cada conexion entrante se delega a un worker del pool (`ExecutorService`).
3. Cada worker procesa la peticion completa y responde de forma independiente.

Implementacion actual en `HttpServer`:

- Pool fijo: `Math.max(4, Runtime.getRuntime().availableProcessors() * 2)`.
- Mapa de endpoints thread-safe con `ConcurrentHashMap`.
- Carga de rutas anotadas protegida con `ROUTE_LOADING_LOCK` para evitar carreras.
- Workers con nombres `http-worker-N` para facilitar depuracion.

Esto permite atender multiples clientes en paralelo sin bloquear toda la aplicacion por una sola conexion lenta.

### Consideraciones de seguridad en concurrencia

- El registro de rutas usa `putIfAbsent` para evitar sobreescrituras por carreras.
- `StaticFileHandler` mantiene la carpeta estatica en un campo `volatile` para visibilidad entre hilos.
- El contexto de cada request (`HttpRequest` y `HttpResponse`) se crea por conexion, sin compartirse entre threads.

### Como verificar la concurrencia

- Prueba unitaria: `HttpServerConcurrencyTest` valida carga concurrente de rutas y tamano del pool.
- Prueba manual: ejecuta varias solicitudes en paralelo contra `http://localhost:8080` y valida respuestas simultaneas.

## Aplicaciones incluidas

Clase principal del framework:

- `src/main/java/com/adojos/app/App.java`

Aplicación con rutas manuales usando lambdas:

- `src/main/java/com/adojos/app/ManualRoutesApp.java`

Aplicación CLI para ejecutar un controlador y una ruta sin levantar el servidor:

- `src/main/java/com/adojos/app/CommandLineApp.java`

## Ejecución

### Requisitos

- Java 21
- Maven 3+

### Compilar

```bash
mvn clean compile
```

### Ejecutar servidor normal con controladores anotados

```bash
java -cp target/classes com.adojos.app.App
```

Al iniciar, el servidor crea un pool fijo de workers para procesar conexiones en paralelo.

Endpoints disponibles por descubrimiento automático en `App`:

- `http://localhost:8080/`
- `http://localhost:8080/hello`
- `http://localhost:8080/pi`
- `http://localhost:8080/greeting`
- `http://localhost:8080/greeting?name=Ana`
- `http://localhost:8080/index.html`

### Ejecutar ejemplo manual con lambdas

```bash
java -cp target/classes com.adojos.app.ManualRoutesApp
```

Endpoints disponibles en `ManualRoutesApp`:

- `http://localhost:8080/App/hello?name=Daniel`
- `http://localhost:8080/App/pi`
- `http://localhost:8080/App/euler`
- `http://localhost:8080/index.html`

### Ejecutar un controlador desde consola sin levantar servidor

```bash
java -cp target/classes com.adojos.app.CommandLineApp com.adojos.app.controllers.HelloController /pi
```

Tambien soporta query params:

```bash
java -cp target/classes com.adojos.app.CommandLineApp com.adojos.app.controllers.GreetingController "/greeting?name=Daniel"
```

Ese modo:

- no abre sockets
- no inicia el servidor HTTP
- carga solo la clase indicada
- ejecuta el metodo asociado al `@GetMapping` de la ruta enviada

## Pruebas

Pruebas unitarias incluidas:

- `HttpRequestTest`
  - Extracción correcta de query params.
  - Parámetros inexistentes retornan `null`.
- `AnnotationApplicationContextTest`
  - Descubrimiento automático de controladores anotados.
  - Carga explícita de un controlador concreto.
  - Resolución de `@RequestParam` con y sin valor en la URL.
- `AppTest`
  - Verifica el modo servidor normal.
  - Verifica el modo CLI con ruta fija y con query params.
- `HttpServerConcurrencyTest`
  - Verifica que la carga de rutas anotadas sea segura bajo llamadas concurrentes.
  - Verifica que el servidor use mas de un worker para atender clientes en paralelo.
- `StaticFileHandlerTest`
  - Resolución de archivos con y sin `/` inicial en `staticfiles`.
  - Retorno `null` cuando el archivo no existe.

Ejecutar pruebas:

```bash
mvn test
```

## Estructura relevante

- `src/main/java/com/adojos/app/App.java`
- `src/main/java/com/adojos/app/ManualRoutesApp.java`
- `src/main/java/com/adojos/app/CommandLineApp.java`
- `src/main/java/com/adojos/app/http/HttpServer.java`
- `src/main/java/com/adojos/app/http/HttpRequest.java`
- `src/main/java/com/adojos/app/http/StaticFileHandler.java`
- `src/main/java/com/adojos/app/http/WebMethod.java`
- `src/main/java/com/adojos/app/context/AnnotationApplicationContext.java`
- `src/main/java/com/adojos/app/controllers/GreetingController.java`
- `src/main/java/com/adojos/app/controllers/HelloController.java`
- `src/main/resources/webroot/public/index.html`
- `src/test/java/com/adojos/app/AppTest.java`
- `src/test/java/com/adojos/app/context/AnnotationApplicationContextTest.java`
- `src/test/java/com/adojos/app/http/HttpRequestTest.java`
- `src/test/java/com/adojos/app/http/HttpServerConcurrencyTest.java`
- `src/test/java/com/adojos/app/http/StaticFileHandlerTest.java`
