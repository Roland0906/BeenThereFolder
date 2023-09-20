pluginManagement {
    repositories {
        google()

        gradlePluginPortal()
        mavenCentral()
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
    }
}

rootProject.name = "BeenThere"
include(":app")
 