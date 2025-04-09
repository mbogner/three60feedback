#!/usr/bin/env bash
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
cd "${DIR}" || exit 1

# osv-scanner --help
#
#NAME:
#   osv-scanner scan source - scans a source project's dependencies for known vulnerabilities using the OSV database.
#
#USAGE:
#   osv-scanner scan source [command options] [directory1 directory2...]
#
#DESCRIPTION:
#   scans a source project's dependencies for known vulnerabilities using the OSV database.
#
#OPTIONS:
#   --lockfile value, -L value [ --lockfile value, -L value ]  scan package lockfile on this path
#   --sbom value, -S value [ --sbom value, -S value ]          scan sbom file on this path, the sbom file name must follow the relevant spec
#   --recursive, -r                                            check subdirectories (default: false)
#   --no-ignore                                                also scan files that would be ignored by .gitignore (default: false)
#   --call-analysis value [ --call-analysis value ]            attempt call analysis on code to detect only active vulnerabilities
#   --no-call-analysis value [ --no-call-analysis value ]      disables call graph analysis
#   --include-git-root                                         include scanning git root (non-submoduled) repositories (default: false)
#   --data-source value                                        source to fetch package information from; value can be: deps.dev, native (default: "deps.dev")
#   --maven-registry value                                     URL of the default registry to fetch Maven metadata
#   --config value                                             set/override config file
#   --format value, -f value                                   sets the output format; value can be: table, html, vertical, json, markdown, sarif, gh-annotations, cyclonedx-1-4, cyclonedx-1-5 (default: "table")
#   --serve                                                    output as HTML result and serve it locally (default: false)
#   --port value                                               port number to use when serving HTML report (default: 8000)
#   --output value                                             saves the result to the given file path
#   --verbosity value                                          specify the level of information that should be provided during runtime; value can be: error, warn, info (default: "info")
#   --offline                                                  run in offline mode, disabling any features requiring network access (default: false)
#   --offline-vulnerabilities                                  checks for vulnerabilities using local databases that are already cached (default: false)
#   --download-offline-databases                               downloads vulnerability databases for offline comparison (default: false)
#   --no-resolve                                               disable transitive dependency resolution of manifest files (default: false)
#   --all-packages                                             when json output is selected, prints all packages (default: false)
#   --licenses value                                           report on licenses based on an allowlist
#   --help, -h                                                 show help

./gradlew clean cyclonedxBom
docker run -it -v "${PWD}":/src ghcr.io/google/osv-scanner scan -r -S /src/build/reports/bom.json /src
