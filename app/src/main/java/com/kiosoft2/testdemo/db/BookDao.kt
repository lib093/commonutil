package com.ubix.kiosoft2.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kiosoft2.testdemo.db.model.Book

@Dao
abstract class BookDao : com.kiosoft2.api.dao.Dao<Book>() {
    @Query("select * from Book where id = :id")
    abstract fun findById(id: Long): List<Book>?
    @Query("select * from Book where name = :name")
    abstract fun findByName(name: String?): List<Book>?
    @Query("select * from Book")
    abstract fun selectAll(): List<Book>?
    @Insert
    abstract fun insertBook(book: Book)
    @Insert
    abstract fun insertBooks(book: List<Book>)
}