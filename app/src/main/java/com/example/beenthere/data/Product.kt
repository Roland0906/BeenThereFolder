//package app.appworks.school.stylish.data
//
//import android.os.Parcelable
//import androidx.room.ColumnInfo
//import androidx.room.Embedded
//import androidx.room.Entity
//import androidx.room.TypeConverters
//import com.example.beenthere.data.source.BeenThereConverters
//
//import com.squareup.moshi.Json
//import kotlinx.parcelize.Parcelize
//
///**
// * Created by Wayne Chen in Jul. 2019.
// */
//@Entity(tableName = "products_in_cart_table", primaryKeys = ["product_id", "product_selected_color_code", "product_selected_size"])
//@TypeConverters(BeenThereConverters::class)
//@Parcelize
//data class Product(
//    @ColumnInfo(name = "product_id")
//    val id: Long,
//    @ColumnInfo(name = "product_title")
//    val title: String,
//    @ColumnInfo(name = "product_description")
//    val description: String,
//    @ColumnInfo(name = "product_price")
//    val price: Int,
//    @ColumnInfo(name = "product_texture")
//    val texture: String,
//    @ColumnInfo(name = "product_wash")
//    val wash: String,
//    @ColumnInfo(name = "product_place")
//    val place: String,
//    @ColumnInfo(name = "product_note")
//    val note: String,
//    @ColumnInfo(name = "product_story")
//    val story: String,
////    @ColumnInfo(name = "product_colors")
////    val colors: List<Color>,
//    @ColumnInfo(name = "product_sizes")
//    val sizes: List<String>,
////    @ColumnInfo(name = "product_variants")
////    val variants: List<Variant>,
//    @ColumnInfo(name = "product_main_image")
//    @Json(name = "main_image") val mainImage: String,
//    @ColumnInfo(name = "product_images")
//    val images: List<String>
//) : Parcelable {
////    val stocks: Int
////        get() {
////            var value = 0
////            for (variant in variants) {
////                value += variant.stock
////            }
////            return value
////        }
////    @Embedded
////    var selectedVariant: Variant = Variant("", "", -1)
////    var amount: Long? = null
//}
