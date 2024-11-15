plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "ThinkingRock"

val auTrProjects = listOf(
    "appl",
    "archive",
    "cal",
    "calendar",
    "calendar.ical4j",
    "calendar.ical4j.impl",
    "data",
    "data.recent",
    "datastore",
    "datastore.xstream",
    //"datastore.xstream2", // broken?
    "email",
    "export",
    "export.actions",
    "export.data",
    "export.references",
    "export.someday",
    "extract",
    //"extract.clean", // broken?
    "find",
    "i18n",
    "imports",
    "imports.thoughts",
    "l10n.de_DE",
    "l10n.en_US",
    "l10n.es_ES",
    "l10n.fr_FR",
    "l10n.nl_NL",
    "prefs.actions",
    "prefs.data",
    "prefs.dates",
    "prefs.projects",
    "prefs.recurrence",
    "prefs.ui",
    "report.project.detail",
    "report.project.outline",
    "report.projects.future",
    "report.sa",
    "reports.actions.delegated",
    "reports.actions.doasap",
    "reports.actions.scheduled",
    "reports.done",
    "reports.pocketmod",
    "reports.reference",
    "reports.someday",
    "reports.weekly",
    "resource",
    "runtime",
    "services",
    "swing",
    "sync.device",
    "sync.iphone",
    "task.activation",
    "task.messages",
    "task.recurrence",
    "updates",
    "util",
    "view",
    "view.actn",
    "view.actns",
    "view.calendar",
    "view.collect",
    "view.contexts",
    "view.criteria",
    "view.delegates",
    "view.filters",
    "view.goals",
    "view.overview",
    "view.process",
    "view.project",
    "view.projects",
    "view.reference",
    "view.someday",
    "view.topics",
).map { "au.com.trgtd.tr.$it" }

val trProjects = listOf(
    "extract.reports",
    "extract.reports.projectdetails",
    "extract.reports.projectoutline",
    "model",
).map { "tr.$it" }

//val 3pDeps = listOf(
//)

include(auTrProjects)
include(trProjects)
//include(3pDeps)

defineProjectPaths()

fun defineProjectPaths() {
    setOf(
        auTrProjects,
        trProjects,
//	3pDeps,
    ).forEach { projects ->
        projects.forEach { projectName ->
             project(":$projectName").projectDir = file("modules/$projectName")
        }
    }
}

includeBuild("gradle-plugins")
