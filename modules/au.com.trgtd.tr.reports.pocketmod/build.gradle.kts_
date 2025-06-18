description = "TR-Reports-Pocketmod"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    api(project(":au.com.trgtd.tr.extract"))

    implementation(project(":au.com.trgtd.tr.appl"))
    implementation(project(":au.com.trgtd.tr.resource"))
    implementation(project(":tr.extract.reports"))
    implementation(project(":tr.model"))

    implementation(libs.itext)
    implementation(libs.jasperreports)
}
