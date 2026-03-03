#!/bin/bash

GRADLE_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"
SOURCE_FILE="mite/mite.jar"
TARGET_DIR="$GRADLE_HOME/caches/fml-loom/1.6.4-MITE"
# In loom 0.1, it's "1.6.4-MITE.jar"
TARGET_FILENAME="1.6.4-MITE.jar"

mkdir -p "$TARGET_DIR"

if [ ! -f "$SOURCE_FILE" ]; then
  echo "Cannot find source file: $SOURCE_FILE" >&2
  exit 1
fi
  cp "$SOURCE_FILE" "$TARGET_DIR/$TARGET_FILENAME"