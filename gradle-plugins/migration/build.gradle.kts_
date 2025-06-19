import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

dependencies {
    implementation(libs.plugin.kotlin)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    }
}

gradlePlugin {
    plugins {
        create("antPathSourcSetPlugin") {
            id = "au.com.trgtd.migration.antpathsourceset"
            implementationClass = "au.com.trgtd.migration.AntPathSourceSetPlugin"
        }
    }
}