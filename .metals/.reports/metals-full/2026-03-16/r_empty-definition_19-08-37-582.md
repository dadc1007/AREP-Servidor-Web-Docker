error id: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/App.java:java/lang/reflect/InvocationTargetException#
file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/App.java
empty definition using pc, found symbol in pc: java/lang/reflect/InvocationTargetException#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 60
uri: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/App.java
text:
```scala
package com.adojos.app;

import java.lang.reflect.Invocati@@onTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class App {
    static Map<String, Method> controllerMethods = new HashMap<>();

    public static void main(String[] args)
            throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        System.out.println("Loading controller classes...");

        Class<?> c = Class.forName(args[0]);

        if (c.isAnnotationPresent(RestController.class)) {
            for (Method method : c.getDeclaredMethods()) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    GetMapping annotation = method.getAnnotation(GetMapping.class);
                    controllerMethods.put(annotation.value(), method);
                }
            }
        }

        String pathWithParams = args[1];
        String path = pathWithParams.split("\\?", 2)[0];
        Map<String, String> requestParams = parseRequestParams(pathWithParams);

        System.out.println("executting web method for path: " + path);

        Method method = controllerMethods.get(path);

        Object instance = null;
        if (!Modifier.isStatic(method.getModifiers())) {
            try {
                instance = c.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        Object[] methodArgs = buildMethodArgs(method, requestParams);

        System.out.println(method.invoke(instance, methodArgs));
    }

    private static Map<String, String> parseRequestParams(String pathWithParams) {
        Map<String, String> params = new HashMap<>();
        String[] pathAndQuery = pathWithParams.split("\\?", 2);

        if (pathAndQuery.length < 2 || pathAndQuery[1].isEmpty()) {
            return params;
        }

        String[] pairs = pathAndQuery[1].split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");

            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }

        return params;
    }

    private static Object[] buildMethodArgs(Method method, Map<String, String> requestParams) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            RequestParam rp = parameters[i].getAnnotation(RequestParam.class);
            if (rp != null) {
                args[i] = requestParams.getOrDefault(rp.value(), rp.defaultValue());
            }
        }

        return args;
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: java/lang/reflect/InvocationTargetException#