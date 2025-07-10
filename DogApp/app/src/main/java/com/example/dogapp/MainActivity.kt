package com.example.dogapp

// Importa las clases necesarias para el funcionamiento de la actividad principal
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dogapp.databinding.ActivityMainBinding // Permite el acceso seguro a las vistas del layout principal usando ViewBinding
import androidx.appcompat.app.AppCompatDelegate // Permite controlar el modo de tema (oscuro/claro)

/**
 * MainActivity es la actividad principal de la aplicación DogApp.
 * Aquí se inicializa el binding del layout y se fuerza el modo claro para toda la app.
 */
class MainActivity : AppCompatActivity() {
    // Variable para el binding de la vista principal. Se inicializa más adelante.
    private lateinit var binding: ActivityMainBinding

    /**
     * onCreate es el método que se ejecuta cuando la actividad es creada.
     * Aquí se configura el modo de tema y se inicializa el binding para acceder a las vistas.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Llama a la implementación base para inicializar la actividad
        // Fuerza el modo claro en toda la aplicación (desactiva modo oscuro)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // Inicializa el binding usando el layoutInflater para acceder a las vistas del layout XML
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Establece el layout de la actividad usando el root del binding
        setContentView(binding.root)
    }
}