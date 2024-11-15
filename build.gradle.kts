plugins {
    java
    alias(libs.plugins.download)
}

private val netbeansVersion = libs.versions.netbeans.ide.get()

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
    }
}

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()

        maven {
            url = uri("https://netbeans.apidesign.org/maven2")
        }
    }

    dependencies {
        //implementation(platform(rootProject.libs.netbeans.cluster.platform))
        //implementation(platform(rootProject.libs.netbeans.cluster.harness))
        //
        //runtimeOnly(libs.netbeans.cluster.platform)
        //implementation(libs.netbeans.api.core.multitabs)

        runtimeOnly(rootProject.libs.netbeans.modules.projectapi.nb)
        runtimeOnly(rootProject.libs.netbeans.modules.core.multitabs.project)
        runtimeOnly(rootProject.libs.netbeans.modules.core.windows)

        testImplementation(rootProject.libs.junit.jupiter)
        testImplementation(rootProject.libs.junit4)
        testRuntimeOnly(rootProject.libs.junit.vintage.engine)
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }

    tasks.withType<Jar>().configureEach {
        manifest {
            from("${project.projectDir}/manifest.mf")
        }
    }
}


tasks {
    val downloadNetBeans = register("downloadNetBeans") {
        description = "Ensures netbeans-$netbeansVersion-bin.zip has been downloaded to the build directory"
        group = "prepare"

        doLast {
            download.run {
                src("https://archive.apache.org/dist/netbeans/netbeans/$netbeansVersion/netbeans-$netbeansVersion-bin.zip")
                dest(layout.buildDirectory)
                overwrite(true)
                onlyIfModified(true)
            }
        }
    }
    register<Copy>("unpackNetBeans") {
        description =
            "Ensures the required netbeans-$netbeansVersion modules have been extracted to the netbeans-plat directory"
        group = "prepare"

        val netbeansZip = zipTree(file(layout.buildDirectory).resolve("netbeans-$netbeansVersion-bin.zip"))
        val netbeansDir = file("./netbeans-plat/$netbeansVersion/").also {
            it.mkdirs()
        }

        from(netbeansZip)
        into(netbeansDir)

        eachFile {
            relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
        }

        include("netbeans/platform/**", "netbeans/harness/**", "netbeans/ide/**")
        exclude("netbeans/platform/docs/**")

        inputs.files(downloadNetBeans)
        outputs.dir(netbeansDir)

        includeEmptyDirs = false

        doFirst {
            file(layout.buildDirectory).mkdirs()
        }
    }
}
