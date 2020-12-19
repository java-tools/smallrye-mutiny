#!/usr/bin/env bash
set -e

init_gpg() {
    gpg2 --fast-import --no-tty --batch --yes smallrye-sign.asc
}

init_git() {
    git config --global user.name "${GITHUB_ACTOR}"
    git config --global user.email "smallrye@googlegroups.com"

    git update-index --assume-unchanged .build/deploy.sh
    git update-index --assume-unchanged .build/decrypt-secrets.sh
}

# -------- SCRIPT START HERE -----------------

init_git
init_gpg

git fetch origin --tags
git update-index --assume-unchanged .build/deploy.sh
mkdir -p /tmp/repository
git checkout "${REF}"
mvn -B clean deploy -DskipTests -Prelease -s maven-settings.xml \
  -DaltReleaseDeploymentRepository=local::file://tmp/repository

ls -l /tmp/repository/io/smallrye/reactive

