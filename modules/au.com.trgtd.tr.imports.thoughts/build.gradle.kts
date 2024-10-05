description = "TR-Imports-Thoughts"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    implementation(project(":au.com.trgtd.tr.appl"))
    implementation(project(":au.com.trgtd.tr.data"))
    implementation(project(":au.com.trgtd.tr.swing"))
    implementation(project(":au.com.trgtd.tr.view.collect"))
    implementation(project(":tr.model"))
}
