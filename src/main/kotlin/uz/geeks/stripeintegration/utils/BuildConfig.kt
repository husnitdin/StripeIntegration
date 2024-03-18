package uz.geeks.stripeintegration.utils

object BuildConfig {
    var buildType = BuildType.LOCAL

    val BASE_URL: String
        get() = when (buildType) {
            BuildType.LOCAL -> "http://localhost:8080"
            BuildType.TEST -> "https://testapiinterhub.brio.uz"
        }

    enum class BuildType {
        LOCAL,
        TEST
    }
}