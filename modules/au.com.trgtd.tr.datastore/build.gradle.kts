description = "TR-Datastore"

plugins {
    id("java-library")      
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    api(libs.netbeans.api.openide.util)
    api(project(":au.com.trgtd.tr.util"))

    implementation(project(":tr.model"))
}
