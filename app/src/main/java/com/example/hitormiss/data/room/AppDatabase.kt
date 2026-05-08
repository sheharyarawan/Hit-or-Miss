package com.example.hitormiss.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hitormiss.data.entity.Player
import com.example.hitormiss.data.entity.PlayerStatsSummary

@Database(
    entities = [
        Player::class,
        PlayerStatsSummary::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun playerStatsDao(): PlayerStatsDao

    companion object{
        @Volatile

        private var instance: AppDatabase?=null
        var LOCK=Any()

        operator fun invoke(context: Context)=instance?:
        synchronized(LOCK){
            instance?: createDB(context).also{
                instance=it
            }
        }
        private fun createDB(context: Context)= Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            name = "Cricket_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}