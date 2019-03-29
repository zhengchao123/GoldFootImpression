package decoration.scsowing.com.decoration.ui.event

import android.content.Context
import com.gold.footimpression.net.utils.LogUtils

class OwnCrashHandler
private constructor() : Thread.UncaughtExceptionHandler {
    private var context: Context? = null
    val TAG = this.javaClass.simpleName
    override fun uncaughtException(t: Thread, e: Throwable) {

        e.printStackTrace()
        e.stackTrace.forEach {
            LogUtils.e(TAG, "\tat stackTrace " + it.toString())
        }
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    fun init(context: Context) {
        this.context = context
    }

    companion object {
        private var INSTANCE: OwnCrashHandler? = null
        val instance: OwnCrashHandler
            @Synchronized get() {
                if (INSTANCE == null)
                    INSTANCE = OwnCrashHandler()
                return INSTANCE!!
            }
    }


}
