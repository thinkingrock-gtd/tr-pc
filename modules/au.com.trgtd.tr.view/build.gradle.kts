description = "TR-View"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    api(project(":tr.model"))
    api(project(":au.com.trgtd.tr.appl"))
    api(project(":au.com.trgtd.tr.prefs.actions"))
    api(project(":au.com.trgtd.tr.prefs.dates"))
    api(project(":au.com.trgtd.tr.prefs.ui"))
    api(project(":au.com.trgtd.tr.resource"))
    api(project(":au.com.trgtd.tr.services"))
    api(project(":au.com.trgtd.tr.swing"))

    api(libs.swingx)
    api(libs.apache.commons.text)

    implementation(project(":au.com.trgtd.tr.runtime"))
}
