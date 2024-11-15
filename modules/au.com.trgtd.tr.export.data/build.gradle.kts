description = "TR-Export-Data"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    implementation(project(":au.com.trgtd.tr.appl"))
    implementation(project(":au.com.trgtd.tr.extract"))
    implementation(project(":tr.model"))

    implementation(libs.supercsv)
}
