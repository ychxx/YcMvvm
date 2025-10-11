pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven (url = "https://jitpack.io")
        gradlePluginPortal()
        maven (url = "https://maven.aliyun.com/repository/jcenter")
    }

}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven (url = "https://jitpack.io")
        maven (url = "https://maven.aliyun.com/repository/jcenter")
    }
}

rootProject.name = "YcMvvm"
include(":app")
include(":ycMvvm")
