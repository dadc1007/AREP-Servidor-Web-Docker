error id: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/HelloController.java:_empty_/RestController#
file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/HelloController.java
empty definition using pc, found symbol in pc: _empty_/RestController#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 28
uri: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/HelloController.java
text:
```scala
package com.adojos.app;

@@@RestController
public class HelloController {
	@GetMapping("/")
	public static String index() {
		return "Greetings from Spring Boot!";
	}

	@GetMapping("/pi")
	public static String wmPI() {
		return "PI = " + Math.PI;
	}

	@GetMapping("/hello")
	public static String wmHello() {
		return "Hello world";
	}
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/RestController#