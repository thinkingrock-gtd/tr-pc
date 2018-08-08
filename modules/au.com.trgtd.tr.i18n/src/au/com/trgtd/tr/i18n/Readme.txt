This project replaces all previous localization modules for TR.

The goal is to combine all translation modules into one module.

The build script will contain targets to create one jar for each language from 
the Omega target bundles and include them as wrapped jars.

The TR application installers or the TR startup will need a mechanism to set the
user locale and also TR will need a way of changing the locale (without the user
having to edit trgtd.conf).

