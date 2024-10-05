description = "TR-Model"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    api(project(":au.com.trgtd.tr.util"))

    implementation(project(":au.com.trgtd.tr.appl"))
    implementation(project(":au.com.trgtd.tr.prefs.actions"))
    implementation(project(":au.com.trgtd.tr.prefs.dates"))
    implementation(project(":au.com.trgtd.tr.prefs.projects"))
    implementation(project(":au.com.trgtd.tr.prefs.recurrence"))
    implementation(project(":au.com.trgtd.tr.resource"))

    implementation(libs.ical4j)
    implementation(libs.apache.commons.email)
    implementation(libs.apache.commons.lang3)
    implementation(libs.minimaljson)
    implementation(libs.netbeans.api.progress)
    implementation(libs.netbeans.api.openide.dialogs)
    implementation(libs.netbeans.api.openide.nodes)
    implementation(libs.netbeans.api.openide.util)
}
