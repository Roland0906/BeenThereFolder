package app.appworks.school.stylish.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.Variant

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Defines methods for using the [Product] class with Room.
 */
@Dao
interface StylishDatabaseDao {

    @Insert
    fun insert(product: Product)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param product: [Product]
     */
    @Update
    fun update(product: Product)

    /**
     * Deletes the [Product] with given id, colorCode and size
     * @param id: [Product.id]
     * @param colorCode: [Product.selectedVariant] [Variant.colorCode]
     * @param size: [Product.selectedVariant] [Variant.size]
     */
    @Query("DELETE from products_in_cart_table WHERE product_id = :id AND product_selected_color_code = :colorCode AND product_selected_size = :size")
    fun delete(id: Long, colorCode: String, size: String)

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM products_in_cart_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by product_id in ascending order.
     */
    @Query("SELECT * FROM products_in_cart_table ORDER BY product_id ASC")
    fun getAllProducts():
        LiveData<List<Product>>

    /**
     * Selects and return the [Product] with given id, colorCode and size
     * @param id: [Product.id]
     * @param colorCode: [Product.selectedVariant] [Variant.colorCode]
     * @param size: [Product.selectedVariant] [Variant.size]
     */
    @Query("SELECT * from products_in_cart_table WHERE product_id = :id AND product_selected_color_code = :colorCode AND product_selected_size = :size")
    fun get(id: Long, colorCode: String, size: String): Product?
}
