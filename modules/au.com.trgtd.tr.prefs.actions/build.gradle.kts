description = "TR-Prefs-Actions"

plugins {
    id("java-library")         
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    implementation(project(":au.com.trgtd.tr.appl"))
    implementation(project(":au.com.trgtd.tr.swing"))
    implementation(project(":au.com.trgtd.tr.util"))
}
