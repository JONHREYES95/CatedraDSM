# LoginForo1 - Mini Proyecto de Autenticación con Jetpack Compose

Este repositorio contiene el código fuente de un mini proyecto de aplicación Android desarrollado utilizando Jetpack Compose. La aplicación implementa un flujo básico de autenticación de usuario con una pantalla de inicio de sesión y una pantalla de panel de administración posterior al inicio de sesión.

## Introducción

LoginForo1 es un proyecto de aprendizaje diseñado para explorar las funcionalidades y ventajas del framework de UI moderno de Android, Jetpack Compose. El objetivo principal es demostrar la implementación de un flujo de autenticación simple y la navegación entre diferentes pantallas utilizando las herramientas que proporciona Compose.

## Características Principales

* **Pantalla de Inicio de Sesión:**
    * Interfaz de usuario construida completamente con Jetpack Compose.
    * Campos para ingresar correo electrónico y contraseña.
    * Validación básica de credenciales (usuario fijo: "[admin@gmail.com]", contraseña: "1234").
    * Animaciones sutiles al aparecer los elementos de la pantalla.
    * Opción para mostrar/ocultar la contraseña.
    * Mensaje de error en caso de credenciales incorrectas.
    * Enlace "¿Olvidaste tu contraseña?" (funcionalidad no implementada).
* **Panel de Administración (HomeScreen):**
    * Interfaz de usuario construida con Jetpack Compose.
    * Muestra un saludo con el nombre de usuario autenticado (pasado a través del `UserViewModel`).
    * Presenta una cuadrícula de "funciones" administrativas representadas por tarjetas con iconos y títulos.
    * Las funciones son ejemplos (Crear Evento, Ver Eventos, etc.) con acciones `onClick` no implementadas.
    * Diseño adaptable que organiza las funciones en filas de dos elementos.
* **Navegación:**
    * Implementada utilizando el componente `NavHost` de Jetpack Compose Navigation.
    * Dos rutas principales: "login" y "home".
    * La navegación desde la pantalla de inicio de sesión a la pantalla de inicio se realiza tras un inicio de sesión exitoso.
* **Gestión de Estado:**
    * Se utiliza `ViewModel` (`UserViewModel`) para mantener y gestionar el estado del nombre de usuario autenticado, permitiendo que la información persista a través de la navegación.
    * Se utilizan `MutableStateFlow` y `StateFlow` para exponer el nombre de usuario de forma reactiva a la UI.
* **Estructura de la Aplicación:**
    * Organización del código en paquetes lógicos (`sv.edu.udb.login`, `gui.LoginScreen`, `gui.Panel.HomeScreen`, `gui`).
    * Uso de `Scaffold` para proporcionar una estructura básica de la interfaz de usuario con una barra superior y una barra de navegación inferior (esta última con elementos no funcionales).
    * Tematización básica de la aplicación utilizando `LoginTheme`.

## Flujo de la Aplicación

1.  Al iniciar la aplicación, el usuario es dirigido a la pantalla de inicio de sesión (`LoginScreen`).
2.  El usuario ingresa su correo electrónico y contraseña.
3.  Al hacer clic en "Iniciar Sesión", la aplicación verifica si las credenciales coinciden con el usuario predefinido.
4.  Si las credenciales son correctas:
    * El nombre de usuario se guarda en el `UserViewModel`.
    * La aplicación navega a la pantalla del panel de administración (`HomeScreen`).
5.  En la pantalla del panel de administración, se muestra un saludo con el nombre de usuario y una lista de funciones administrativas (actualmente no funcionales).
6.  La barra de navegación inferior proporciona elementos para "Perfil", "Configuración", "Modo Oscuro" y "Ayuda" (actualmente sin implementar).

## Decisiones de Diseño

* **Uso de Jetpack Compose:** Se eligió Jetpack Compose para construir la interfaz de usuario debido a sus ventajas en cuanto a desarrollo declarativo, menor cantidad de código y mejor integración con Kotlin.
* **Navegación con `NavHost`:** Se utilizó el componente de navegación de Jetpack Compose para gestionar el flujo entre las diferentes pantallas de la aplicación de manera estructurada.
* **`ViewModel` para Gestión de Estado:** Se implementó un `ViewModel` para separar la lógica de la UI de la presentación y para mantener el estado del usuario a través de los cambios de configuración y la navegación.
* **Diseño Modular:** La interfaz de usuario se construyó utilizando composables reutilizables para facilitar el mantenimiento y la extensibilidad del código.
* **Validación Simple:** Para este mini proyecto, se implementó una validación de credenciales muy básica con un usuario codificado. En una aplicación real, esto se reemplazaría con una autenticación contra un backend.

## Video Demostrativo

Podemos ver un video corto del inicio de la aplicación y sus animaciones en el siguiente enlace:  [Video demostrativo](https://drive.google.com/file/d/1t9LZ6tcdfOpT81eJy9JQgENGJknVLCPp/view?usp=sharing)

## Conclusiones

Este mini proyecto demuestra de manera efectiva los conceptos fundamentales de la construcción de interfaces de usuario en Android con Jetpack Compose, incluyendo la creación de layouts, la gestión del estado, la navegación y la aplicación de animaciones básicas. La naturaleza declarativa de Compose simplifica la definición de la UI y la integración con Kotlin mejora la legibilidad y mantenibilidad del código. Si bien este es un ejemplo simple, sienta las bases para construir aplicaciones Android más complejas y ricas en funcionalidades utilizando las herramientas modernas que ofrece Jetpack Compose. La transición al paradigma declarativo puede requerir un cambio de mentalidad para los desarrolladores acostumbrados al sistema de vistas tradicional, pero los beneficios en términos de eficiencia y claridad del código son evidentes.

## Próximos Pasos (Para proyecto final)

* Implementar la funcionalidad de las "funciones" en la pantalla del panel de administración.
* Implementar una autenticación más robusta contra un backend real.
* Agregar más pantallas y un flujo de navegación más complejo.
* Mejorar el diseño y la experiencia del usuario.
* Implementar pruebas unitarias e de interfaz de usuario.
* Explorar características más avanzadas de Jetpack Compose, como layouts personalizados y animaciones complejas.

## Licencia

[MIT License)]

