object Configuration {
    const val compileSdk = 33
    const val targetSdk = 33
    const val minSdk = 24
    private const val majorVersion = 1
    private const val minorVersion = 0
    private const val patchVersion = 0
    const val versionName = "$majorVersion.$minorVersion.$patchVersion"
    const val versionCode = 1
    const val snapshotVersionName = "$majorVersion.$minorVersion.${patchVersion + 1}-SNAPSHOT"
    const val artifactGroup = "com.swipe.catalog"
}