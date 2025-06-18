set windows-shell := ["powershell.exe", "-NoLogo", "-Command"]

@_list:
    just --list --unsorted

repodir := `pwd`

netbeans-plat-version := "24"
java-version := "17"

alias verify-ci := verify-headless

# Builds the entire project - also downloading the harness if needed
build:
    ant build -Dnbplatform.default.netbeans.dest.dir={{repodir}}/netbeans-plat/{{ netbeans-plat-version }}/ide

# Creates the zipped application code in dist/trgtd.zip
build-zip:
    ant build-zip -Dnbplatform.default.netbeans.dest.dir={{repodir}}/netbeans-plat/{{ netbeans-plat-version }}/ide

# Verifies modules with headless tests
verify-headless *args:
    ./gradlew check {{args}}

# Verifies all modules - including UI tests
verify *args:
    ./gradlew check -Dtest.profile=allTests {{args}}

# Shows some diagnostics used by ant
diagnostics:
    ant -diagnostics

# Clears the cache of the local development version of ThinkingRock
[linux]
[macos]
clear-cache-dev:
	rm -rI ./build/testuserdir/var/cache/

# Clears the cache of the local development version of ThinkingRock
[windows]
clear-cache-dev:
	rm -r -fo ./build/testuserdir/var/cache/

# Clears the cache of production versions of ThinkingRock
[linux]
clear-cache-prod:
	rm -rI ~/.cache/trgtd/

# Clears the cache of production versions of ThinkingRock
[macos]
clear-cache-prod:
	rm -rI ~/Library/Caches/trgtd/

# Clears the cache of production versions of ThinkingRock
[windows]
clear-cache-prod:
	rm -r -fo env_var('APPDATA')\..\Local\trgtd\Cache\

# Starts the ThinkingRock application from sources
run:
	ant -f ./modules/au.com.trgtd.tr.calendar \
	    -Dant.build.javac.target={{ java-version }} \
	    -Dant.build.javac.source={{ java-version }} \
	    -Dcontinue.after.failing.tests=true \
	    run

## Starts the ThinkingRock application from sources - not ready yet
#run:
#	./gradlew :au.com.trgtd.tr.appl:runNetBeans
