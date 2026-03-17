error id: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/context/AnnotationApplicationContext.java:com/adojos/app/annotations/GetMapping#
file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/context/AnnotationApplicationContext.java
empty definition using pc, found symbol in pc: com/adojos/app/annotations/GetMapping#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 486
uri: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/context/AnnotationApplicationContext.java
text:
```scala
package com.adojos.app.context;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.adojos.app.annotations.@@GetMapping;
import com.adojos.app.annotations.RequestParam;
import com.adojos.app.annotations.RestController;
import com.adojos.app.http.HttpRequest;
import com.adojos.app.http.WebMethod;

public class AnnotationApplicationContext {
    private final Map<Class<?>, Object> beans = new HashMap<>();
    private final Map<String, WebMethod> routes = new HashMap<>();

    public void load() {
        for (Class<?> candidate : findCandidateClasses()) {
            load(candidate, false);
        }
    }

    public void load(Class<?> candidate) {
        load(candidate, true);
    }

    public Map<String, WebMethod> getRoutes() {
        return routes;
    }

    public Collection<Object> getBeans() {
        return beans.values();
    }

    private void load(Class<?> candidate, boolean failIfNotController) {
        if (!candidate.isAnnotationPresent(RestController.class)) {
            if (failIfNotController) {
                throw new IllegalStateException("Class is not annotated with @RestController: " + candidate.getName());
            }
            return;
        }

        Object bean = createBean(candidate);
        beans.put(candidate, bean);
        registerRoutes(candidate, bean);
    }

    private void registerRoutes(Class<?> controllerClass, Object bean) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(GetMapping.class)) {
                continue;
            }

            if (!String.class.equals(method.getReturnType())) {
                throw new IllegalStateException("@GetMapping only supports String return types: "
                        + controllerClass.getName() + "." + method.getName());
            }

            validateParameters(controllerClass, method);
            method.setAccessible(true);

            String path = method.getAnnotation(GetMapping.class).value();
            if (routes.containsKey(path)) {
                throw new IllegalStateException("Duplicate @GetMapping path detected: " + path);
            }

            routes.put(path, (request, response) -> invoke(bean, method, request));
        }
    }

    private void validateParameters(Class<?> controllerClass, Method method) {
        for (Parameter parameter : method.getParameters()) {
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            if (requestParam == null || !String.class.equals(parameter.getType())) {
                throw new IllegalStateException("Unsupported parameter in @GetMapping method: "
                        + controllerClass.getName() + "." + method.getName());
            }
        }
    }

    private String invoke(Object bean, Method method, HttpRequest request) {
        try {
            Object[] args = resolveArguments(method, request);
            return (String) method.invoke(bean, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to invoke controller method: " + method.getName(), e);
        }
    }

    private Object[] resolveArguments(Method method, HttpRequest request) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int index = 0; index < parameters.length; index++) {
            RequestParam requestParam = parameters[index].getAnnotation(RequestParam.class);
            String value = request.getValues(requestParam.value());
            args[index] = value == null || value.isEmpty() ? requestParam.defaultValue() : value;
        }

        return args;
    }

    private Object createBean(Class<?> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to create component: " + type.getName(), e);
        }
    }

    private List<Class<?>> findCandidateClasses() {
        String classPath = System.getProperty("java.class.path", "");
        String[] entries = classPath.split(File.pathSeparator);
        Set<String> classNames = new HashSet<>();

        for (String entry : entries) {
            if (entry == null || entry.isBlank()) {
                continue;
            }

            File file = new File(entry);
            if (!file.isDirectory()) {
                continue;
            }

            classNames.addAll(scanDirectory(file.toPath(), file.toPath()));
        }

        List<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (String className : classNames) {
            try {
                classes.add(Class.forName(className, false, classLoader));
            } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
            }
        }
        return classes;
    }

    private List<String> scanDirectory(Path root, Path current) {
        List<String> classNames = new ArrayList<>();
        try (var paths = Files.list(current)) {
            paths.forEach(path -> {
                if (Files.isDirectory(path)) {
                    classNames.addAll(scanDirectory(root, path));
                    return;
                }

                String fileName = path.getFileName().toString();
                if (!fileName.endsWith(".class") || fileName.contains("$")
 fileName.equals("module-info.class") || fileName.equals("package-info.class")) {
                    return;
                }

                Path relativePath = root.relativize(path);
                String className = relativePath.toString()
                        .replace(File.separatorChar, '.')
                        .replace('/', '.')
                        .replaceAll("\\.class$", "");
                classNames.add(className);
            });
        } catch (IOException e) {
            throw new IllegalStateException("Failed to scan classpath directory: " + current, e);
        }
        return classNames;
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: com/adojos/app/annotations/GetMapping#