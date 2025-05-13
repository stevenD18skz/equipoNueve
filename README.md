# DogApp - Agenda de Citas para Mascotas 🐾

![DogApp Logo](app/src/main/res/drawable/img_dog_head.png) **Miniproyecto 1 - Sprint 1**
**Curso:** Desarrollo de Aplicaciones para Dispositivos Móviles
**Institución:** Universidad del Valle - Sede Cali
**Profesor:** Ing. Walter Medina
**Fecha de Entrega Sprint 1:** 15 de mayo de 2025

## 📝 Descripción General

DogApp es una aplicación Android diseñada para facilitar la gestión de citas en una veterinaria. Permite a los administradores de la veterinaria registrar, visualizar, modificar y eliminar citas de las mascotas, optimizando así el flujo de atención y la organización interna.

Este proyecto se desarrolla como parte del Miniproyecto 1, aplicando los conocimientos adquiridos en el curso de Desarrollo de Aplicaciones para Dispositivos Móviles, utilizando Kotlin como lenguaje de programación principal y siguiendo el patrón de arquitectura MVVM.

## 👥 Equipo de Desarrollo: `[NOMBRE_DEL_EQUIPO]`

* **Integrante 1:** `Brayan Steven Narvaez Valdes` - `Brayan.steven.narvaez@correounivalle.edu.co`
* **Integrante 2:** `[Nombre Completo del Integrante 2]` - `[tu.correo2@example.com]` - `[@usuarioGitHub2 (Opcional)]`
* **Integrante 3:** `[Nombre Completo del Integrante 3 (si aplica)]` - `[tu.correo3@example.com]` - `[@usuarioGitHub3 (Opcional)]`
* **Integrante 4:** `[Nombre Completo del Integrante 3 (si aplica)]` - `[tu.correo3@example.com]` - `[@usuarioGitHub3 (Opcional)]`
* **Integrante 5:** `[Nombre Completo del Integrante 3 (si aplica)]` - `[tu.correo3@example.com]` - `[@usuarioGitHub3 (Opcional)]`

## 🛠️ Herramientas y Tecnologías Utilizadas

* **Lenguaje:** Kotlin
* **IDE:** Android Studio 
* **Arquitectura:** MVVM (Model-View-ViewModel) con Repositorio.
* **Componentes Principales de Android Jetpack:**
    * ViewBinding
    * LiveData
    * ViewModel
    * Navigation Component
    * Room Persistence Library (para base de datos local SQLite)
    * Biometric Prompt (para autenticación con huella digital)
* **Librerías Externas:**
    * Lottie (para animaciones)
    * Retrofit (para consumo de APIs REST)
    * [Menciona cualquier otra librería importante que uses, ej: Gson, Coil, Glide, etc.]
* **APIs Externas:**
    * Dog CEO API (`https://dog.ceo/api/`): Para obtener listado de razas e imágenes de perros.
* **Gestión de Proyecto:** Jira (`[ENLACE_A_JIRA]`)
* **Control de Versiones:** Git y GitHub

## ✨ Características Implementadas (Sprint 1)

El Sprint 1 se enfoca en las siguientes Historias de Usuario:

### HU 1.0: Ventana Login
* **Yo como (Actor):** Aplicación
* **Quiero (Acción):** Poder presentar al usuario un sistema de logueo con biometría dactilar
* **Para poder (Consecuencia):** Mejorar la experiencia de usuario al ingresar a la app
* **Criterios Clave:**
    * Interfaz con fondo gris oscuro, sin toolbar.
    * Imagen de perro en la parte superior derecha.
    * Título "DogApp" con fuente personalizada.
    * Animación Lottie de huella digital para iniciar autenticación.
    * Diálogo de autenticación biométrica nativo.
    * Navegación al Home (HU 2.0) si la autenticación es exitosa.
    * Creación de ícono personalizado para la aplicación.

### HU 2.0: Home Administrador de Citas
* *(Añadir descripción breve cuando se implemente)*

### HU 3.0: Ventana Nueva Cita
* *(Añadir descripción breve cuando se implemente)*

### HU 4.0: Ventana Detalle de la Cita
* *(Añadir descripción breve cuando se implemente)*

### HU 5.0: Ventana Editar Cita
* *(Añadir descripción breve cuando se implemente)*

## 🚀 Configuración del Proyecto

### Prerrequisitos
* Android Studio 2023.1.1  o superior.
* SDK de Android: API Nivel 34 
* Min SDK: API Nivel 23 (Android 6.0 Marshmallow) - Requerido para `BiometricPrompt`.
* Un dispositivo físico o emulador con capacidad de autenticación biométrica (para HU 1.0) y conexión a internet (para HU 3.0 en adelante).

### Pasos para Compilar y Ejecutar
1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/stevenD18skz/Moviles-DogApp.git
    ```
2.  **Abrir en Android Studio:**
    * Abrir Android Studio.
    * Seleccionar "Open an Existing Project".
    * Navegar hasta la carpeta donde clonaste el repositorio y seleccionarla.
3.  **Sincronizar Gradle:**
    * Android Studio debería sincronizar automáticamente los archivos Gradle. Si no, haz clic en "Sync Project with Gradle Files" (🐘 icono de elefante en la barra de herramientas).
4.  **Ejecutar la aplicación:**
    * Selecciona un dispositivo/emulador compatible.
    * Haz clic en el botón "Run 'app'" (▶️ icono de play).

## 📂 Estructura del Proyecto

El proyecto sigue el patrón de arquitectura MVVM y está organizado de la siguiente manera:
