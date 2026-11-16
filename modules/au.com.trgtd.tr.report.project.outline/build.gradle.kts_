description = "TR-Report-Project-Outline"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    api(project(":au.com.trgtd.tr.extract"))
    api(project(":tr.extract.reports"))
    api(project(":tr.model"))

    implementation(project(":au.com.trgtd.tr.appl"))
    implementation(project(":au.com.trgtd.tr.prefs.projects"))
    implementation(project(":au.com.trgtd.tr.resource"))

    implementation(libs.itext)
    implementation(libs.jasperreports)
}
