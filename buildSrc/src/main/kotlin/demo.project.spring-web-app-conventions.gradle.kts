plugins {
    id("demo.project.java-common-conventions")
    idea
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/martynovskyi/telegram-bot-api-reactive")
        credentials {
            username = extra["github.packages.read.username"].toString()
            password = extra["github.packages.read.token"].toString()
        }
    }
}