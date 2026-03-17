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
- `com.adojos.app.examples`: utilidades de red/socket de apoyo.

## Características implementadas

1. `HttpServer.get(path, (req, res) -> ...)` para endpoints manuales
2. Descubrimiento automático de clases anotadas con `@RestController`
3. Ejecucion por linea de comandos de un controlador y una ruta especifica
4. Soporte para `@GetMapping("/ruta")` con retorno `String`
5. Soporte para `@RequestParam(value = "name", defaultValue = "World")`
6. `HttpServer.staticfiles("webroot/public")` (también soporta `"/webroot/public"`)V
7. Servidor en `http://localhost:8080`
8. Entrega de archivos estáticos cuando no hay endpoint dinámico

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
- `http://localhost:8080/index2.html`

### Ejecutar un controlador desde consola sin levantar servidor

```bash
java -cp target/classes com.adojos.app.CommandLineApp com.adojos.app.controllers.HelloController /pi
```

Tambien soporta query params:

```bash
java -cp target/classes com.adojos.app.CommandLineApp com.adojos.app.controllers.GreetingController /greeting?name=Daniel
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
- `StaticFileHandlerTest`
  - Resolución de archivos con y sin `/` inicial en `staticfiles`.
  - Retorno `null` cuando el archivo no existe.

Ejecutar pruebas:

```bash
mvn test
```
