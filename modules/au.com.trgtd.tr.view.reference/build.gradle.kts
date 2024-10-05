description = "TR-View-Reference"

plugins {
    id("java-library")
    id("au.com.trgtd.migration.antpathsourceset")
}

dependencies {
    api(project(":tr.model"))
    
    api(libs.swingx)

    implementation(project(":au.com.trgtd.tr.services"))
    implementation(project(":au.com.trgtd.tr.appl"))
    implementation(project(":au.com.trgtd.tr.view"))
    implementation(project(":au.com.trgtd.tr.view.filters"))
    implementation(project(":au.com.trgtd.tr.view.process"))
    implementation(project(":au.com.trgtd.tr.view.topics"))
 
    implementation(libs.glazedlists)
}
