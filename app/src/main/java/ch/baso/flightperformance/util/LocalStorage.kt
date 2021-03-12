package ch.baso.flightperformance.util

import android.app.Activity
import ch.baso.flightperformance.model.InputData
import ch.baso.flightperformance.model.StorageData
import com.google.gson.Gson

class LocalStorage private constructor(private val context: Activity, private val dbName: String) {

    private val gson = Gson()

    fun storeData(data: Any) {

        val sharedPref = context.getPreferences(Activity.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putString(dbName, gson.toJson(data))
        editor.commit()
    }

    fun <T> getData(clazz: Class<T>): T? {
        val sharedPref = context.getPreferences(Activity.MODE_PRIVATE)
        val inputDataStr = sharedPref.getString(dbName, null)
        if (inputDataStr != null) {
            return gson.fromJson(inputDataStr, clazz)
        }

        return null
    }

    companion object : Singleton2Holder<LocalStorage, Activity, String>(::LocalStorage)

}