description = "TR-Calendar-ical4j"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    implementation(project(":au.com.trgtd.tr.swing"))
    implementation(project(":au.com.trgtd.tr.util"))

    api(libs.ical4j)
}
