package au.com.trgtd.migration

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

@Suppress("unused", "UnstableApiUsage")
/**
 * Plugin configuring gradle to expect main/test/resource source sets
 * as they are found in the ant configured project. Thus, we can build
 * up a gradle project in parallel to the still used ant-setup, profiting
 * from gradle for the CI build.
 *
 * The target will eventually be to move the files to a more gradle-style
 * directory layout.
 */
class AntPathSourceSetPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        apply<JavaPlugin>()

        val sourceSets = extensions.getByType<SourceSetContainer>()

        sourceSets.named(SourceSet.MAIN_SOURCE_SET_NAME) {
            java.setSrcDirs(listOf(MAIN_SRC_SET))
            resources.setSrcDirs(listOf(MAIN_SRC_SET))
        }
        sourceSets.named(SourceSet.TEST_SOURCE_SET_NAME) {
            java.setSrcDirs(listOf(TEST_SRC_SET))
            resources.setSrcDirs(listOf(TEST_SRC_SET))
        }
    }

    companion object {
        private const val MAIN_SRC_SET = "src"
        private const val TEST_SRC_SET = "test/unit/src"
    }
}
