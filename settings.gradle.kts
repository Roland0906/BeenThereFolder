pluginManagement {
    repositories {
        google()

        gradlePluginPortal()
        mavenCentral()
        maven ("https://www.jitpack.io")
//        google()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
//        google()
        mavenLocal()
        maven ("https://www.jitpack.io")
    }
}

rootProject.name = "BeenThere"
include(":app")
 