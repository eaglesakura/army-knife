import org.gradle.api.Project
import java.util.*

class Extras(
    private val project: Project
) {
    /**
     * Get property.
     */
    fun get(key: String): Any {
        return try {
            project.extensions.extraProperties[key]!!
        } catch (e: Throwable) {
            project.rootProject.extensions.extraProperties[key]!!
        }
    }

    /**
     * Current build by android studio.
     */
    val buildByAndroidStudio
        get() = get("android_studio") as Boolean

    /**
     * Kotlin configure.
     */
    val kotlin = Kotlin()

    /**
     * Android application configure.
     */
    val androidApp = AndroidApp()

    /**
     * Dependencies version.
     */
    val dependencies = Dependencies()

    /**
     * Build configs.
     */
    val build = Build()

    inner class Kotlin {
        /**
         * Kotlin lang version.
         * e.g.)
         *  "1.1.21"
         */
        val version
            get() = get("kotlin_version") as String

        /**
         * Kotlin coroutines-extension vesion.
         * e.g.)
         *  "1.1.1"
         */
        val coroutinesVersion
            get() = get("kotlin_coroutines_version") as String
    }

    inner class Dependencies {
    }

    inner class Build {
        /**
         *  Build number.
         *  if not run on CI, then returns 1.
         */
        val number
            get() = get("build_number") as Int

        /**
         * Build start date.
         */
        val date
            get() = get("build_date") as Date

        /**
         * Build on CI flag.
         */
        val ci
            get() = get("build_on_ci") as Boolean
    }

    inner class AndroidApp {
        /**
         * Assemble DPI.
         */
        val assembleDpi
            get() = get("android_assemble_dpi") as String

        /**
         * size of using 'dex' command heap size.
         * default  is "2g"
         */
        val dexMaxHeapSize
            get() = get("dex_max_heap_on_ci") as String
    }
}