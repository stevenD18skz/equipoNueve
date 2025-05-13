# DogApp - Agenda de Citas para Mascotas 🐾

**Miniproyecto 1**

- **Curso:** Desarrollo de Aplicaciones para Dispositivos Móviles
- **Institución:** Universidad del Valle - Sede Cali
- **Profesor:** Ing. Walter Medina

---

## 📝 Descripción General

DogApp es una aplicación Android diseñada para facilitar la gestión de citas en una veterinaria. Permite a los administradores de la veterinaria registrar, visualizar, modificar y eliminar citas de las mascotas, optimizando así el flujo de atención y la organización interna.

Este proyecto se desarrolla como parte del Miniproyecto 1, aplicando los conocimientos adquiridos en el curso de Desarrollo de Aplicaciones para Dispositivos Móviles, utilizando Kotlin como lenguaje de programación principal y siguiendo el patrón de arquitectura MVVM.

---

## 👥 Equipo de Desarrollo: `Equipo 9`

| Código     | Nombre                         | Correo                                     |
|------------|--------------------------------|--------------------------------------------|
| 202226285  | Cristian David Cabrera Pantoja | cristian.pantoja@correounivalle.edu.co     |
| 202228507  | Kevin Jordan Alzate            | kevin.jordan@correounivalle.edu.co         |
| 202224949  | Junior Cantor Arevalo          | junior.cantor@correounivalle.edu.co        |
| 202226675  | Brayan Steven Narvaez Valdes   | Brayan.steven.narvaez@correounivalle.edu.co|
| xxxxxxxxx  | xxxxxx xxxxxx xxxxxxx xxxxxx   | emanuel.rivas@correounivalle.edu.co        |

---

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
---

## 📱 Características principales

- **Autenticación biométrica** con huella digital.
- **Visualización y gestión de citas** en una interfaz limpia.
- **Creación de nuevas citas** con validaciones y autocompletado de razas (API externa).
- **Visualización detallada** de cada cita, incluyendo imagen aleatoria de la raza.
- **Edición y eliminación** de citas con persistencia local (Room).
- Diseño moderno con **animaciones Lottie** y enfoque en usabilidad.
---

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

---

## 📂 Estructura del Proyecto

El proyecto sigue el patrón de arquitectura MVVM y está organizado de la siguiente manera:

      -
      -
      -
      -

---