package com.service.market.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.getdriver.data.local.dao.OrderDao
import com.service.market.data.local.dao.MyOrderDao
import com.service.market.data.local.dao.RemoteKeysDao
import com.service.market.data.local.dao.ServiceDao
import com.service.market.data.local.model.orders.MyOrders
import com.service.market.data.local.model.orders.Orders
import com.service.market.data.local.model.orders.RemoteKeys
import com.service.market.data.local.model.service.Services
import com.service.market.utils.Converters


@Database(entities = [Orders::class, RemoteKeys::class, Services::class, MyOrders::class], version = 11, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun orderDao(): OrderDao
    abstract fun serviceDao(): ServiceDao
    abstract fun myOrderDao(): MyOrderDao
    abstract fun pageKeyDao(): RemoteKeysDao

    //abstract fun pageKeyDao(): PageKeyDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "orders")
                .fallbackToDestructiveMigration()
                .build()
    }

}