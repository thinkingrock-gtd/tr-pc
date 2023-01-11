@_list:
    just --list --unsorted

repodir := `pwd`

alias verify-ci := verify-tr

# Builds the entire project - also downloading the harness if needed
build:
    ant build -Dnbplatform.default.netbeans.dest.dir={{repodir}}/netbeans-plat/16/ide

# Creates the zipped application code in dist/trgtd.zip
build-zip:
    ant build-zip -Dnbplatform.default.netbeans.dest.dir={{repodir}}/netbeans-plat/16/ide

# Verifies a single module
verify-module module: build
    cd modules/{{module}} && ant test -Dnbplatform.default.netbeans.dest.dir={{repodir}}/netbeans-plat/16/ide

# Verifies modules with headless tests
verify-headless: verify-tr verify-deps

# Verifies thinking rock modules (only headless)
verify-tr:
    just verify-module au.com.trgtd.tr.archive
    just verify-module au.com.trgtd.tr.cal
    just verify-module au.com.trgtd.tr.calendar
    just verify-module au.com.trgtd.tr.calendar.ical4j
    just verify-module au.com.trgtd.tr.calendar.ical4j.impl
    just verify-module au.com.trgtd.tr.data
    just verify-module au.com.trgtd.tr.data.recent
    just verify-module au.com.trgtd.tr.datastore
    just verify-module au.com.trgtd.tr.datastore.xstream
    just verify-module au.com.trgtd.tr.datastore.xstream2
    just verify-module au.com.trgtd.tr.email
    just verify-module au.com.trgtd.tr.export
    just verify-module au.com.trgtd.tr.export.actions
    just verify-module au.com.trgtd.tr.export.data
    just verify-module au.com.trgtd.tr.export.references
    just verify-module au.com.trgtd.tr.export.someday
    just verify-module au.com.trgtd.tr.extract
    just verify-module au.com.trgtd.tr.find
    just verify-module au.com.trgtd.tr.imports
    just verify-module au.com.trgtd.tr.imports.thoughts
    just verify-module au.com.trgtd.tr.prefs.actions
    just verify-module au.com.trgtd.tr.prefs.data
    just verify-module au.com.trgtd.tr.prefs.dates
    just verify-module au.com.trgtd.tr.prefs.projects
    just verify-module au.com.trgtd.tr.prefs.recurrence
    just verify-module au.com.trgtd.tr.report.project.detail
    just verify-module au.com.trgtd.tr.report.project.outline
    just verify-module au.com.trgtd.tr.report.projects.future
    just verify-module au.com.trgtd.tr.report.sa
    just verify-module au.com.trgtd.tr.reports.actions.delegated
    just verify-module au.com.trgtd.tr.reports.actions.doasap
    just verify-module au.com.trgtd.tr.reports.actions.scheduled
    just verify-module au.com.trgtd.tr.reports.done
    just verify-module au.com.trgtd.tr.reports.pocketmod
    just verify-module au.com.trgtd.tr.reports.reference
    just verify-module au.com.trgtd.tr.reports.someday
    just verify-module au.com.trgtd.tr.reports.weekly
    just verify-module au.com.trgtd.tr.resource
    just verify-module au.com.trgtd.tr.runtime
    just verify-module au.com.trgtd.tr.services
    just verify-module au.com.trgtd.tr.sync.device
    just verify-module au.com.trgtd.tr.sync.iphone
    just verify-module au.com.trgtd.tr.task.activation
    just verify-module au.com.trgtd.tr.task.messages
    just verify-module au.com.trgtd.tr.util
    just verify-module au.com.trgtd.tr.view
    just verify-module au.com.trgtd.tr.view.actn
    just verify-module au.com.trgtd.tr.view.actns
    just verify-module au.com.trgtd.tr.view.calendar
    just verify-module au.com.trgtd.tr.view.collect
    just verify-module au.com.trgtd.tr.view.contexts
    just verify-module au.com.trgtd.tr.view.criteria
    just verify-module au.com.trgtd.tr.view.delegates
    just verify-module au.com.trgtd.tr.view.filters
    just verify-module au.com.trgtd.tr.view.goals
    just verify-module au.com.trgtd.tr.view.process
    just verify-module au.com.trgtd.tr.view.project
    just verify-module au.com.trgtd.tr.view.projects
    just verify-module au.com.trgtd.tr.view.reference
    just verify-module au.com.trgtd.tr.view.someday
    just verify-module au.com.trgtd.tr.view.topics
    just verify-module tr.extract.reports
    just verify-module tr.extract.reports.projectdetails
    just verify-module tr.extract.reports.projectoutline
    just verify-module tr.model

# The following modules don't have test. Need to be included if they get any.
# just verify-module au.com.trgtd.tr.appl
# just verify-module au.com.trgtd.tr.extract.clean
# just verify-module au.com.trgtd.tr.i18n
# just verify-module au.com.trgtd.tr.l10n.nl_NL
# just verify-module au.com.trgtd.tr.prefs.ui
# just verify-module au.com.trgtd.tr.task.recurrence
# just verify-module au.com.trgtd.tr.updates
# just verify-module au.com.trgtd.tr.view.overview
# just verify-module au.com.trgtd.tr.l10n.de_DE
# just verify-module au.com.trgtd.tr.l10n.en_US
# just verify-module au.com.trgtd.tr.l10n.es_ES
# just verify-module au.com.trgtd.tr.l10n.fr_FR


# Verifies the modules of external dependencies
verify-deps:
    just verify-module activation
    just verify-module commons.email
    just verify-module commons-lang
    just verify-module commons-lang3
    just verify-module commons-logging
    just verify-module commons-text
    just verify-module fop
    just verify-module glazedlists-1.7.0_java
    just verify-module ical4j
    just verify-module jasperreports
    just verify-module javamail
    just verify-module jaxb
    just verify-module miglayout-4.0-swing
    just verify-module minimal-json
    just verify-module swingx
    just verify-module xalan
    just verify-module xstream-1.4.19

# Verifies a module that requires a graphical user interface (not in CI)
verify-ui:
    just verify-module au.com.trgtd.tr.swing


# Shows some diagnostics used by ant
diagnostics:
    ant -diagnostics
