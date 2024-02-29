plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencyLocking {
    lockAllConfigurations()
    lockMode = LockMode.STRICT
}
