#!/usr/bin/env bash

# run from project root folder './scripts/lockfiles.sh'
./gradlew \
            --write-locks --refresh-dependencies dependencies \
            :app:dependencies \
            :persistence:dependencies \
            :buildSrc:dependencies