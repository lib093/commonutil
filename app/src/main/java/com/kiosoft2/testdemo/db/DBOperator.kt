package com.ubix.kiosoft2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import com.kiosoft2.api.Entity
import com.kiosoft2.api.EntityManager
import com.kiosoft2.api.RoomOperator
import com.kiosoft2.testdemo.db.model.Book
import com.kiosoft2.testdemo.db.model.User

@Database(entities = [User::class, Book::class], exportSchema = true, version = 1)
abstract class DBOperator : RoomOperator() {
    override fun <T> getEntity(entityClass: Class<T>): Entity<T> {
        return EntityManager.getEntity(entityClass)
    }

    abstract fun getBookDao(): BookDao?

    companion object {
        private var dbOperator: DBOperator? = null

        /**
         * 初始化数据库
         */
        @JvmStatic
        fun init(context: Context) {
            if (dbOperator == null) {
                dbOperator = databaseBuilder<DBOperator>(
                    context,
                    DBOperator::class.java,
                    "demo_cus.db"
                ) //                    .allowMainThreadQueries()//能避免主线程操作DB，一定要在子线程操作
                    .build()
            }
        }

        fun get(): DBOperator? {
            return dbOperator
        }
    }
}