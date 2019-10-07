#!/usr/bin/env bash

set -o errexit
set -o pipefail
set -o nounset
set -o xtrace

cd src
cp installer-script installer.sh
tar -cvf archive.tar ext ; gzip -9 archive.tar
cat archive.tar.gz >> installer.sh
rm archive.tar.gz
chmod +x installer.sh
mv installer.sh ../dst/installer.sh
