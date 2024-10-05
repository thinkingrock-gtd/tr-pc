description = "TR-View-Overview"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    api(project(":au.com.trgtd.tr.appl"))
    api(project(":au.com.trgtd.tr.util"))
    api(project(":tr.model"))

    implementation(project(":au.com.trgtd.tr.resource"))
    implementation(project(":au.com.trgtd.tr.swing"))
    implementation(project(":au.com.trgtd.tr.view"))
    implementation(project(":au.com.trgtd.tr.view.actns"))
    implementation(project(":au.com.trgtd.tr.view.collect"))
    implementation(project(":au.com.trgtd.tr.view.contexts"))
    implementation(project(":au.com.trgtd.tr.view.criteria"))
    implementation(project(":au.com.trgtd.tr.view.process"))
    implementation(project(":au.com.trgtd.tr.view.projects"))
    implementation(project(":au.com.trgtd.tr.view.reference"))
    implementation(project(":au.com.trgtd.tr.view.someday"))
    implementation(project(":au.com.trgtd.tr.view.topics"))

    implementation(libs.batik.swing)
    implementation(libs.swingx)
}
