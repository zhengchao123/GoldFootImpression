package com.gold.footimpression.net.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.StringRes
import com.gold.footimpression.net.utils.LogUtils
import java.util.*


class SharedPreferencesUtils private constructor(context: Context, spName: String = DEFAULT_SP_NAME) {

    val TAG = this.javaClass.simpleName
    val sharedPreferences: SharedPreferences
    private val sEditor: SharedPreferences.Editor

    val all: Map<String, *>
        get() = sharedPreferences.all

    init {
        mContext = context.applicationContext
        sharedPreferences = mContext!!.getSharedPreferences(spName, Context.MODE_PRIVATE)
        sEditor = sharedPreferences.edit()
        mCurSPName = spName
        LogUtils.i(TAG, "SharedPreferencesUtils: $mCurSPName")
    }

    fun put(@StringRes key: Int, value: Any): SharedPreferencesUtils? {
        return put(mContext!!.getString(key), value)
    }

    fun put(key: String, value: Any): SharedPreferencesUtils? {

        LogUtils.i(TAG, "put key $key with value $value")
        if (value is String) {
            sEditor.putString(key, value)
        } else if (value is Int) {
            sEditor.putInt(key, value)
        } else if (value is Boolean) {
            sEditor.putBoolean(key, value)
        } else if (value is Float) {
            sEditor.putFloat(key, value)
        } else if (value is Long) {
            sEditor.putLong(key, value)
        } else {
            sEditor.putString(key, value.toString())
        }
        //        editorCompat.apply(sEditor);
        sEditor.apply()
        return sSharedPreferencesUtils
    }

    operator fun get(@StringRes key: Int, defaultObject: Any): Any? {
        return get(mContext!!.getString(key), defaultObject)
    }

    operator fun get(key: String, defaultObject: Any): Any? {
        if (defaultObject is String) {
            return sharedPreferences.getString(key, defaultObject)
        } else if (defaultObject is Int) {
            return sharedPreferences.getInt(key, defaultObject)
        } else if (defaultObject is Boolean) {
            return sharedPreferences.getBoolean(key, defaultObject)
        } else if (defaultObject is Float) {
            return sharedPreferences.getFloat(key, defaultObject)
        } else if (defaultObject is Long) {
            return sharedPreferences.getLong(key, defaultObject)
        }
        return null
    }

    fun putInt(key: String, value: Int): SharedPreferencesUtils {
        sEditor.putInt(key, value)
        sEditor.apply()
        return this
    }

    fun putInt(@StringRes key: Int, value: Int): SharedPreferencesUtils {
        return putInt(mContext!!.getString(key), value)
    }

    fun getInt(@StringRes key: Int): Int {
        return getInt(mContext!!.getString(key))
    }

    fun getInt(@StringRes key: Int, defValue: Int): Int {
        return getInt(mContext!!.getString(key), defValue)
    }


    @JvmOverloads
    fun getInt(key: String, defValue: Int = DEFAULT_INT): Int {
        return sharedPreferences.getInt(key, defValue)
    }

    fun putFloat(@StringRes key: Int, value: Float): SharedPreferencesUtils? {
        return putFloat(mContext!!.getString(key), value)
    }

    fun putFloat(key: String, value: Float): SharedPreferencesUtils? {
        sEditor.putFloat(key, value)
        sEditor.apply()
        return sSharedPreferencesUtils
    }

    @JvmOverloads
    fun getFloat(key: String, defValue: Float = DEFAULT_FLOAT): Float {
        return sharedPreferences.getFloat(key, defValue)
    }

    fun getFloat(@StringRes key: Int): Float {
        return getFloat(mContext!!.getString(key))
    }

    fun getFloat(@StringRes key: Int, defValue: Float): Float {
        return getFloat(mContext!!.getString(key), defValue)
    }

    fun putLong(@StringRes key: Int, value: Long): SharedPreferencesUtils? {
        return putLong(mContext!!.getString(key), value)
    }

    fun putLong(key: String, value: Long): SharedPreferencesUtils? {
        sEditor.putLong(key, value)
        sEditor.apply()
        return sSharedPreferencesUtils
    }

    @JvmOverloads
    fun getLong(key: String, defValue: Long = DEFAULT_INT.toLong()): Long {
        return sharedPreferences.getLong(key, defValue)
    }

    fun getLong(@StringRes key: Int): Long {
        return getLong(mContext!!.getString(key))
    }

    fun getLong(@StringRes key: Int, defValue: Long): Long {
        return getLong(mContext!!.getString(key), defValue)
    }

    fun putString(@StringRes key: Int, value: String): SharedPreferencesUtils? {
        return putString(mContext!!.getString(key), value)
    }

    fun putString(key: String, value: String): SharedPreferencesUtils? {
        sEditor.putString(key, value)
        sEditor.apply()
        return sSharedPreferencesUtils
    }

    @JvmOverloads
    fun getString(key: String, defValue: String = DEFAULT_STRING): String? {
        return sharedPreferences.getString(key, defValue)
    }

    fun getString(@StringRes key: Int): String? {
        return getString(mContext!!.getString(key), DEFAULT_STRING)
    }

    fun getString(@StringRes key: Int, defValue: String): String? {
        return getString(mContext!!.getString(key), defValue)
    }

    fun putBoolean(@StringRes key: Int, value: Boolean): SharedPreferencesUtils? {
        return putBoolean(mContext!!.getString(key), value)
    }

    fun putBoolean(key: String, value: Boolean): SharedPreferencesUtils? {
        sEditor.putBoolean(key, value)
        sEditor.apply()
        return sSharedPreferencesUtils
    }

    @JvmOverloads
    fun getBoolean(key: String, defValue: Boolean = DEFAULT_BOOLEAN): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }

    fun getBoolean(@StringRes key: Int): Boolean {
        return getBoolean(mContext!!.getString(key))
    }

    fun getBoolean(@StringRes key: Int, defValue: Boolean): Boolean {
        return getBoolean(mContext!!.getString(key), defValue)
    }

    fun putStringSet(@StringRes key: Int, value: Set<String>): SharedPreferencesUtils? {
        return putStringSet(mContext!!.getString(key), value)
    }

    fun putStringSet(key: String, value: Set<String>): SharedPreferencesUtils? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            sEditor.putStringSet(key, value)
            sEditor.apply()
        }
        return sSharedPreferencesUtils
    }


    @JvmOverloads
    fun getStringSet(key: String, defValue: Set<String> = DEFAULT_STRING_SET): Set<String>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            sharedPreferences.getStringSet(key, defValue)
        } else {
            DEFAULT_STRING_SET
        }
    }

    fun getStringSet(@StringRes key: Int): Set<String>? {
        return getStringSet(mContext!!.getString(key))
    }

    fun getStringSet(@StringRes key: Int, defValue: Set<String>): Set<String>? {
        return getStringSet(mContext!!.getString(key), defValue)
    }


    operator fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    operator fun contains(@StringRes key: Int): Boolean {
        return contains(mContext!!.getString(key))
    }

    fun remove(@StringRes key: Int): SharedPreferencesUtils? {
        return remove(mContext!!.getString(key))
    }

    fun remove(key: String): SharedPreferencesUtils? {
        sEditor.remove(key)
        sEditor.apply()
        return sSharedPreferencesUtils
    }

    fun clear(): SharedPreferencesUtils? {
        sEditor.clear()
        sEditor.apply()
        return sSharedPreferencesUtils
    }

    companion object {

        //


        private val TAG = "SharedPreferencesUtils"

        private var sSharedPreferencesUtils: SharedPreferencesUtils? = null
        //    private static SharedPreferencesCompat.EditorCompat editorCompat = SharedPreferencesCompat.EditorCompat.getInstance();

        private val DEFAULT_SP_NAME = "SharedData"
        private val DEFAULT_INT = 0
        private val DEFAULT_FLOAT = 0.0f
        private val DEFAULT_STRING = ""
        private val DEFAULT_BOOLEAN = false
        private val DEFAULT_STRING_SET = HashSet<String>(0)

        private var mCurSPName = DEFAULT_SP_NAME
        private var mContext: Context? = null
        val NEAE_DOT = "NEAE_DOT"

        fun init(context: Context): SharedPreferencesUtils {
            if (sSharedPreferencesUtils == null || mCurSPName != DEFAULT_SP_NAME) {
                sSharedPreferencesUtils = SharedPreferencesUtils(context)
            }
            return sSharedPreferencesUtils as SharedPreferencesUtils
        }

        fun destorySharedPreference() {
            sSharedPreferencesUtils = null
        }

        fun init(context: Context, spName: String): SharedPreferencesUtils {
            if (sSharedPreferencesUtils == null) {
                sSharedPreferencesUtils = SharedPreferencesUtils(context, spName)
            } else if (spName != mCurSPName) {
                sSharedPreferencesUtils = SharedPreferencesUtils(context, spName)
            }
            return sSharedPreferencesUtils as SharedPreferencesUtils
        }
    }
}
