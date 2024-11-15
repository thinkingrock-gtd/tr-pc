description = "TR-Swing"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    api(libs.miglayout.swing)

    implementation(project(":au.com.trgtd.tr.prefs.dates"))
    implementation(project(":au.com.trgtd.tr.util"))

    implementation(libs.netbeans.api.openide.util.ui)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    if (System.getProperty("test.profile") != "allTests") {
        exclude("**/SpinnerCycleNumberTest*")
    }
}
