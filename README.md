# Proyecto de Cátedra DSM

Aplicación móvil desarrollada en Kotlin para la gestión de eventos académicos, utilizando Jetpack Compose y Firebase. Permite registro y autenticación de usuarios, creación y visualización de eventos, comentarios, calificaciones, estadísticas y administración de usuarios.

## Estructura del Proyecto

- **app/src/main/java/sv/edu/udb/login/**
  - **HenryNajera/**: Comentarios, calificaciones y pantallas de inicio.
  - **JarlyVigil/**: RSVP, historial y estadísticas de eventos, ayuda y scaffolding de la app.
  - **JonathanReyes/**: Registro, login, perfil y administración de usuarios.
  - **NelsonLuna/**: Creación y visualización de eventos.
  - **MainActivity.kt**: Punto de entrada principal de la aplicación.
- **app/src/main/res/**: Recursos gráficos, layouts, temas y valores.
- **app/google-services.json**: Configuración de Firebase.
- **build.gradle.kts / app/build.gradle.kts**: Configuración y dependencias del proyecto.

## Tecnologías y Dependencias

- **Lenguaje:** Kotlin
- **Framework:** Android Jetpack Compose
- **Gestión de estados y navegación:** ViewModel, Navigation Compose
- **UI:** Material3, Material Icons
- **Backend:** Firebase (Authentication, Firestore, Analytics)
- **Testing:** JUnit, Espresso

## Instalación y Configuración

1. **Clonar el repositorio:**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   ```
2. **Abrir en Android Studio** (recomendado) o importar como proyecto Gradle.
3. **Configurar Firebase:**
   - El archivo `app/google-services.json` ya está incluido para la integración con Firebase.
   - Si necesitas usar tu propio proyecto de Firebase, reemplaza este archivo por el generado en la consola de Firebase.
4. **Sincronizar dependencias:**
   - Android Studio lo hará automáticamente al abrir el proyecto.
   - O ejecuta:
     ```bash
     ./gradlew build
     ```
5. **Ejecutar la aplicación:**
   - Selecciona un emulador o dispositivo físico y presiona "Run" en Android Studio.

## Uso Básico

- **Registro/Login:** Los usuarios pueden crear cuentas y autenticarse mediante Firebase Auth.
- **Gestión de eventos:** Crear, visualizar, calificar y comentar eventos.
- **Administración:** Usuarios con permisos pueden gestionar otros usuarios y eventos.
- **Estadísticas y reportes:** Visualización de métricas y participación en eventos.

## Autores y Colaboradores

- Jonathan Reyes
- Henry Najera
- Jarly Vigil
- Nelson Luna

## Licencia

Este proyecto está licenciado bajo **Creative Commons CC0 1.0 Universal** (Dominio Público). Consulta el archivo LICENSE para más detalles.

---

**Notas:**
- Asegúrate de tener configurado un entorno de desarrollo Android con JDK 11+ y Android Studio actualizado.
- Para funcionalidades avanzadas de Firebase, revisa la documentación oficial y ajusta las reglas de seguridad según tus necesidades.