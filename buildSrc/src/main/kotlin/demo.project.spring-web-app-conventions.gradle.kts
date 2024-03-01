plugins {
    id("demo.project.java-common-conventions")
    idea
}

val usernameProvider = providers.gradleProperty("github.packages.read.username")
val passwordProvider = providers.gradleProperty("github.packages.read.token")

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/martynovskyi/telegram-bot-api-reactive")
        credentials {
            username = usernameProvider.getOrNull()
            password = passwordProvider.getOrNull()
        }
    }
}