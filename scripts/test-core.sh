#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p core/build/smoke
find core/src/main/java core/src/test/java -name '*.java' -print0 |
  xargs -0 javac --release 17 -encoding UTF-8 -d core/build/smoke
java -ea -cp core/build/smoke de.cebrero.drwho.core.CoreTest
