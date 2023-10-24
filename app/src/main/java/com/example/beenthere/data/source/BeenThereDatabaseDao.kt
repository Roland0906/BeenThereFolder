package com.example.beenthere.data.source

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.beenthere.data.Book
import com.example.beenthere.data.Experience
import com.example.beenthere.data.Situation
import kotlinx.coroutines.flow.Flow

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Defines methods for using the [Book] class with Room.
 */
@Dao
interface BeenThereDatabaseDao {

//    @Query("SELECT * FROM experience_table")
//    fun getAllExps(): LiveData<List<Experience>>


    @Query("SELECT * FROM experience_table")
    fun getAllExp(): Flow<List<Experience>>

    @Query("SELECT * FROM experience_table")
    fun observeALlExp(): LiveData<List<Experience>>

    @Query("SELECT * FROM situation_table")
    fun getSituations(): Flow<List<Situation>>

    @Insert
    fun insert(experience: Experience)

    @Delete
    fun delete(experience: Experience)

    @Update
    fun update(experience: Experience)

    @Upsert
    fun upsert(situation: Situation)


    @Insert
    fun insert(experiences: List<Experience>)



    @Query("DELETE from experience_table")
    fun clearExp() // removed suspend 'cause it's quick


    @Query("DELETE from situation_table")
    fun clearSituation()
//
//    /**
//     * When updating a row with a value already set in a column,
//     * replaces the old value with the new one.
//     *
//     * @param product: [Book]
//     */
//    @Update
//    fun update(product: Book)
//
//    /**
//     * Deletes the [Book] with given id, colorCode and size
//     * @param id: [Book.id]
//     * @param colorCode: [Book.selectedVariant] [Variant.colorCode]
//     * @param size: [Book.selectedVariant] [Variant.size]
//     */
//    @Query("DELETE from products_in_cart_table WHERE product_id = :id AND product_selected_color_code = :colorCode AND product_selected_size = :size")
//    fun delete(id: Long, colorCode: String, size: String)
//
//    /**
//     * Deletes all values from the table.
//     *
//     * This does not delete the table, only its contents.
//     */
//    @Query("DELETE FROM products_in_cart_table")
//    fun clear()
//
//    /**
//     * Selects and returns all rows in the table,
//     *
//     * sorted by product_id in ascending order.
//     */
//    @Query("SELECT * FROM products_in_cart_table ORDER BY product_id ASC")
//    fun getAllProducts():
//        LiveData<List<Book>>
//
//    /**
//     * Selects and return the [Book] with given id, colorCode and size
//     * @param id: [Book.id]
//     * @param colorCode: [Book.selectedVariant] [Variant.colorCode]
//     * @param size: [Book.selectedVariant] [Variant.size]
//     */
//    @Query("SELECT * from products_in_cart_table WHERE product_id = :id AND product_selected_color_code = :colorCode AND product_selected_size = :size")
//    fun get(id: Long, colorCode: String, size: String): Book?
}
