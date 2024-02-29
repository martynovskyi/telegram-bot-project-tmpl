import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "demo.project.app"
version = "0.0.1"

plugins {
    id("demo.project.spring-web-app-conventions")
    id("demo.project.java-subproject-conventions")
    alias(libs.plugins.springBoot)
}

dependencies {
    implementation(project(":persistence"))
    implementation("com.motokyi.tg:telegram-bot-api-reactive:2.1.+")
    implementation(libs.bundles.spring.boot.service.reactive)

    runtimeOnly(libs.postgresql)
    runtimeOnly(libs.postgresql.r2dbc)
    implementation(libs.bundles.flyway)

    implementation(libs.commons.collections4)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

tasks.named<BootJar>("bootJar") {
    mainClass.set("demo.project.app.Application")
    archiveFileName.set("demo-project-app.boot.jar")
}

testing {
    repositories {
        mavenCentral()
    }
    dependencies {
        testImplementation(libs.assertj)
        testImplementation(libs.mockito.core)
        testImplementation(libs.mockito.junit.jupiter)
        testImplementation(libs.bundles.junit.jupiter)
        testImplementation(libs.reactor.test)
    }
}