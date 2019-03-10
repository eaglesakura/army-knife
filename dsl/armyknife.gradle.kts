import java.nio.charset.Charset

extra["artifact_group"] = "com.eaglesakura"
extra["artifact_version"] = System.getenv("CIRCLE_TAG").let { CIRCLE_TAG ->
    val majorMinor = if (CIRCLE_TAG.isNullOrEmpty()) {
        rootProject.extra["army_knife_version"] as String
    } else {
        CIRCLE_TAG.substring(CIRCLE_TAG.indexOf('v') + 1)
    }

    val buildNumberFile = rootProject.file(".configs/secrets/build-number.env")
    if (buildNumberFile.isFile) {
        return@let "$majorMinor.${buildNumberFile.readText(Charset.forName("UTF-8"))}"
    }

    return@let when {
        hasProperty("install_snapshot") -> "$majorMinor.99999"
        System.getenv("CIRCLE_BUILD_NUM") != null -> "$majorMinor.${System.getenv("CIRCLE_BUILD_NUM")}"
        else -> "$majorMinor.snapshot"
    }
}.trim()

extra["bintray_package_name"] = file(".").absoluteFile.name!!
extra["bintray_labels"] = arrayOf("android", "kotlin")
extra["bintray_vcs_url"] = "https://github.com/eaglesakura/army-knife"