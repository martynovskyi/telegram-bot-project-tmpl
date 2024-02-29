plugins {
    id("demo.project.java-subproject-conventions")
    id("demo.project.java-library-conventions")
    alias(libs.plugins.springBoot)
}

group = "demo.project.persistence"
version = "unspecified"

dependencies {
    implementation(libs.commons.collections4)
    implementation(libs.spring.boot.starter.data.r2dbc)
    implementation(libs.reactor.core)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

testing {
    repositories {
        mavenCentral()
    }
    dependencies {
        testRuntimeOnly(libs.postgresql)
        testRuntimeOnly(libs.postgresql.r2dbc)
        testRuntimeOnly(libs.bundles.flyway)

        testImplementation(libs.spring.boot.starter.test)
        testImplementation(libs.bundles.junit.jupiter)
        testImplementation(libs.testcontainers.junit)
        testImplementation(libs.testcontainers.r2dbc)
        testImplementation(libs.testcontainers.postgresql)
        testImplementation(libs.reactor.test)
    }
}

tasks.withType<JavaCompile>() {
    options.compilerArgs.addAll(listOf("-parameters"))
}