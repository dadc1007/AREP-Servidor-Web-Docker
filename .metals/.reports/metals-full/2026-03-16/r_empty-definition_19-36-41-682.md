error id: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/controllers/GreetingController.java:com/adojos/app/annotations/GetMapping#
file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/controllers/GreetingController.java
empty definition using pc, found symbol in pc: com/adojos/app/annotations/GetMapping#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 83
uri: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/controllers/GreetingController.java
text:
```scala
package com.adojos.app.controllers;


import com.adojos.app.annotations.GetMappi@@ng;
import com.adojos.app.annotations.RequestParam;
import com.adojos.app.annotations.RestController;

@RestController
public class GreetingController {
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hola " + name;
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: com/adojos/app/annotations/GetMapping#