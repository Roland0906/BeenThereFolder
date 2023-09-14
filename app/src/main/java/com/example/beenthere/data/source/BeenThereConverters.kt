package app.appworks.school.stylish.data.source.local

import androidx.room.TypeConverter
import app.appworks.school.stylish.data.Color
import app.appworks.school.stylish.data.Variant
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class StylishConverters {

    /**
     * Convert [List] [String] to Json
     */
    @TypeConverter
    fun convertListToJson(list: List<String>?): String? {
        list?.let {
            return Moshi.Builder().build().adapter<List<String>>(List::class.java).toJson(list)
        }
        return null
    }

    /**
     * Convert Json to [List] [String]
     */
    @TypeConverter
    fun convertJsonToList(json: String?): List<String>? {
        json?.let {
            val type = Types.newParameterizedType(List::class.java, String::class.java)
            val adapter: JsonAdapter<List<String>> = Moshi.Builder().build().adapter(type)
            return adapter.fromJson(it)
        }
        return null
    }

    /**
     * Convert [List] [Color] to Json
     */
    @TypeConverter
    fun convertColorsToJson(colors: List<Color>?): String? {
        colors?.let {
            return Moshi.Builder().build().adapter<List<Color>>(List::class.java).toJson(colors)
        }
        return null
    }

    /**
     * Convert Json to [List] [Color]
     */
    @TypeConverter
    fun convertJsonToColors(json: String?): List<Color>? {
        json?.let {
            val type = Types.newParameterizedType(List::class.java, Color::class.java)
            val adapter: JsonAdapter<List<Color>> = Moshi.Builder().build().adapter(type)
            return adapter.fromJson(it)
        }
        return null
    }

    /**
     * Convert [List] [Variant] to Json
     */
    @TypeConverter
    fun convertVariantsToJson(variants: List<Variant>?): String? {
        variants?.let {
            return Moshi.Builder().build().adapter<List<Variant>>(List::class.java).toJson(variants)
        }
        return null
    }

    /**
     * Convert Json to [List] [Variant]
     */
    @TypeConverter
    fun convertJsonToVariants(json: String?): List<Variant>? {
        json?.let {
            val type = Types.newParameterizedType(List::class.java, Variant::class.java)
            val adapter: JsonAdapter<List<Variant>> = Moshi.Builder().build().adapter(type)
            return adapter.fromJson(it)
        }
        return null
    }
}
