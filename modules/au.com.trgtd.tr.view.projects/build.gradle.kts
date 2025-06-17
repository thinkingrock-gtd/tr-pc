description = "TR-View-Projects"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    implementation(project(":au.com.trgtd.tr.data"))
    implementation(project(":au.com.trgtd.tr.extract"))
    implementation(project(":au.com.trgtd.tr.prefs.projects"))
    implementation(project(":au.com.trgtd.tr.report.project.detail"))
    implementation(project(":au.com.trgtd.tr.report.project.outline"))
    implementation(project(":au.com.trgtd.tr.swing"))
    implementation(project(":au.com.trgtd.tr.view"))
    implementation(project(":au.com.trgtd.tr.view.actn"))
    implementation(project(":au.com.trgtd.tr.view.contexts"))
    implementation(project(":au.com.trgtd.tr.view.goals"))
    implementation(project(":au.com.trgtd.tr.view.project"))
    implementation(project(":au.com.trgtd.tr.view.topics"))
    implementation(project(":tr.extract.reports"))

    implementation(libs.itext)
    implementation(libs.jasperreports)
}
