description = "TR-View-Process"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    api(project(":tr.model"))

    api(libs.swingx)

    implementation(project(":au.com.trgtd.tr.swing"))
    implementation(project(":au.com.trgtd.tr.appl"))
    implementation(project(":au.com.trgtd.tr.email"))
    implementation(project(":au.com.trgtd.tr.prefs.projects"))
    implementation(project(":au.com.trgtd.tr.runtime"))
    implementation(project(":au.com.trgtd.tr.view"))
    implementation(project(":au.com.trgtd.tr.view.actn"))
    implementation(project(":au.com.trgtd.tr.view.contexts"))
    implementation(project(":au.com.trgtd.tr.view.criteria"))
    implementation(project(":au.com.trgtd.tr.view.goals"))
    implementation(project(":au.com.trgtd.tr.view.topics"))
}
