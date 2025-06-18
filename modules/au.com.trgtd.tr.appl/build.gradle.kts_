description = "TR-Application"

plugins {
    id("java-library")
    application
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    api(libs.netbeans.api.modules.options.api)

    implementation(libs.miglayout.swing)

    runtimeOnly(libs.netbeans.api.modules.netbinox)
    runtimeOnly(libs.netbeans.api.openide.filesystems.compat8)
    runtimeOnly(libs.netbeans.modules.ide.kit)
}

val netbeansVersion = libs.versions.netbeans.ide.get()

tasks.register<JavaExec>("runNetBeans") {
    val userdir = file("${rootProject.layout.buildDirectory.get()}/testuserdir")
    dependsOn(rootProject.tasks.named("downloadNetBeans"))
    classpath = sourceSets["main"].runtimeClasspath + files("${layout.buildDirectory}/netbeans-platform/platform/lib/boot.jar")
    mainClass.set("org.netbeans.core.startup.Main")
    args("--branding", project.name)
    jvmArgs = listOf(
        "-ea",
        "-Xms512m", "-Xmx2048m",
        "-Dorg.netbeans.ProxyClassLoader.level=1000",
        "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        "--add-opens=java.base/java.net=ALL-UNNAMED",
        "--add-opens=java.base/java.security=ALL-UNNAMED",
        "--add-opens=java.base/java.text=ALL-UNNAMED",
        "--add-opens=java.base/java.util=ALL-UNNAMED",
        "--add-opens=java.desktop/java.awt.font=ALL-UNNAMED",
        "--add-opens=java.desktop/javax.swing=ALL-UNNAMED",
        "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
	"-XX:+HeapDumpOnOutOfMemoryError",
	"-XX:HeapDumpPath=$userdir/var/log/heapdumup.hprof",
    )
    val clusterDir = file("${rootProject.layout.buildDirectory.get()}/cluster")
    val netbeansDir = file("${rootProject.layout.projectDirectory}/netbeans-plat/$netbeansVersion/")
    systemProperty("netbeans.user", userdir)
    systemProperty("netbeans.dirs", listOf(clusterDir, netbeansDir.resolve("harness"), netbeansDir.resolve("ide")).joinToString(":"))
    systemProperty("netbeans.home", netbeansDir.resolve("platform"))
    systemProperty("netbeans.logger.console", "true")
    systemProperty("java.security.manager", "allow")
}

