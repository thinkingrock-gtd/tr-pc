description = "TR-Task-Messages"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    implementation(project(":au.com.trgtd.tr.appl"))
    implementation(project(":au.com.trgtd.tr.runtime"))
    implementation(project(":au.com.trgtd.tr.util"))
}
