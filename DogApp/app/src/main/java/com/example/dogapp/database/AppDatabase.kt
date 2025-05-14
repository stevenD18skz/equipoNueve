package com.example.dogapp.database // Ajusta tu package name

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dogapp.database.dao.AppointmentDao
import com.example.dogapp.database.entity.Appointment

@Database(entities = [Appointment::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appointmentDao(): AppointmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dogapp_database"
                )
                    .fallbackToDestructiveMigration() // ¡Cuidado en producción!
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}