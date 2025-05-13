package com.example.dogapp // <<<--- Asegúrate que este sea TU package name

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dogapp.databinding.ActivityMainBinding // <<<--- Importa la clase ViewBinding correcta (generada desde activity_main.xml)

class MainActivity : AppCompatActivity() {

    // Declara la variable para ViewBinding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Infla el layout usando ViewBinding
        // Esto crea una instancia de la clase de enlace que contiene referencias directas a tus vistas
        binding = ActivityMainBinding.inflate(layoutInflater)

        // 2. Establece la vista de contenido de la actividad usando la raíz del layout inflado
        // binding.root se refiere al ConstraintLayout (o el layout raíz que tengas) en activity_main.xml
        setContentView(binding.root)

        // --- Configuración Adicional (si es necesaria) ---

        // En esta configuración simple donde el NavHostFragment se define completamente en XML
        // (activity_main.xml) con app:navGraph="@navigation/nav_graph" y app:defaultNavHost="true",
        // generalmente NO necesitas obtener explícitamente el NavController aquí en onCreate.
        // El sistema maneja la carga del fragmento inicial (LoginFragment) automáticamente.

        // Si más adelante necesitas interactuar con la navegación DESDE la MainActivity
        // (por ejemplo, para conectar un BottomNavigationView o un ActionBar/Toolbar global),
        // entonces sí necesitarías obtener el NavController. Ejemplo (NO necesario ahora para HU 1.0):
        /*
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        // setupActionBarWithNavController(navController) // Ejemplo para conectar ActionBar
        */
    }

    // Es importante NO tener más lógica compleja aquí si la idea es que MainActivity
    // sea solo un contenedor. La lógica de UI debe residir en los Fragments y ViewModels.
}