description = "TR-Extract"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    api(project(":tr.model"))
    api(project(":au.com.trgtd.tr.appl"))

    implementation(project(":au.com.trgtd.tr.datastore"))
    implementation(project(":au.com.trgtd.tr.runtime"))
    implementation(project(":au.com.trgtd.tr.swing"))

    implementation(libs.apache.commons.text)
    implementation(libs.fop)
}
