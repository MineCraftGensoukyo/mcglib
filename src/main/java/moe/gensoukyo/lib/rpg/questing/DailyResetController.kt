package moe.gensoukyo.lib.rpg.questing

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.commons.io.FileUtils
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Controls events that will reset at 4:00 am
 *
 * @see [Companion.get] to obtain an instance
 * @author ChloePrime
 */
class DailyResetController private constructor(
    key: String
) {
    companion object {
        private val instances = HashMap<String, DailyResetController>(32)

        /**
         * Obtaining an instance
         * DO NOT invoke this with dynamically generated string.
         */
        @JvmStatic
        fun get(key: String): DailyResetController {
            return instances.getOrPut(key) {
                DailyResetController(key)
            }
        }


        private val GSON_GENERIC_TYPE = object : TypeToken<HashMap<String, Long>>() {}.type
        private val GSON = GsonBuilder().create()
        private val LOGGER = LogManager.getLogger()

        private fun getFile(key: String): File {
            return File("./mcg_data/events/daily/$key.json")
        }

        private fun loadMap(key: String): MutableMap<String, Long> {
            val file = getFile(key)
            return if (file.exists()) {
                file.bufferedReader(Charsets.UTF_8).use {
                    return GSON.fromJson(it, GSON_GENERIC_TYPE)
                }
            } else {
                HashMap(100)
            }
        }
    }

    private val lastSuccessInvokeTimeOfUser: MutableMap<String, Long> = loadMap(key)
    private val file = getFile(key)
    init {
        try {
            FileUtils.forceMkdirParent(file)
            file.createNewFile()
        } catch (e: IOException) {
            logIOError(e)
        }
    }

    /**
     * Gets whether the player has passed one day
     *
     * After giving daily bonus, invoke [set] method to record it,
     * and prevents the same user from returning true until next day.
     *
     * @see [set]
     */
    operator fun get(user: String): Boolean {
        val t = lastSuccessInvokeTimeOfUser.getOrDefault(user, 0)
        return t <= TimeComputer.getLastResetTime()
    }

    /**
     * @see [get]
     */
    fun set(user: String) {
        lastSuccessInvokeTimeOfUser[user] = System.currentTimeMillis()
        GlobalScope.launch {
            try {
                file.bufferedWriter(Charsets.UTF_8).use {
                    GSON.toJson(lastSuccessInvokeTimeOfUser, GSON_GENERIC_TYPE, it)
                }
            } catch (e: IOException) {
                logIOError(e)
            }
        }
    }

    private fun logIOError(ex: Exception) {
        LOGGER.error("Failed to manipulate file ${file.absolutePath}", ex)
    }
}

private object TimeComputer {
    private const val ONE_DAY = 86400000
    private var lastTimeOfCompute = 0L
    private var cachedNextResetTime = 0L
    fun getLastResetTime(): Long {
        if (System.currentTimeMillis() - lastTimeOfCompute < ONE_DAY / 96) {
            return cachedNextResetTime
        }
        lastTimeOfCompute = System.currentTimeMillis()

        val result = computeLastResetTime()
        cachedNextResetTime = result
        return result
    }

    private fun computeLastResetTime(): Long{
        val calendar = Calendar.getInstance()
        if (calendar[Calendar.HOUR_OF_DAY] < 4) {
            calendar[Calendar.DAY_OF_MONTH] -= 1
        }
        calendar.set(Calendar.HOUR_OF_DAY, 4)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
