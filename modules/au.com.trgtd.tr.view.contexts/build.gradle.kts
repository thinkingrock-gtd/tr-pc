description = "TR-View-Contexts"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    implementation(project(":au.com.trgtd.tr.view"))
    implementation(libs.glazedlists)
}
