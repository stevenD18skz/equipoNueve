package com.example.dogapp.database

// Importa las clases necesarias para la configuración de Room y el contexto de Android
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dogapp.database.dao.AppointmentDao
import com.example.dogapp.database.entity.Appointment

/**
 * Clase que representa la base de datos principal de la app usando Room.
 * Aquí se definen las entidades y el acceso a los DAOs.
 */
@Database(entities = [Appointment::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Método abstracto para obtener el DAO de citas (AppointmentDao)
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        // Instancia única de la base de datos para evitar múltiples conexiones
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia de la base de datos, creándola si no existe (Singleton).
         * Usa el contexto de la aplicación para evitar fugas de memoria.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // Crea la base de datos usando Room.databaseBuilder
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Contexto global de la app
                    AppDatabase::class.java,    // Clase de la base de datos
                    "dogapp_database"          // Nombre del archivo de la base de datos
                )
                    .fallbackToDestructiveMigration(false) // No elimina datos si cambia el esquema
                    .build()
                INSTANCE = instance // Guarda la instancia creada
                instance
            }
        }
    }
}