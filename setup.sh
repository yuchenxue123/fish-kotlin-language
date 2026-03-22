#!/bin/bash

# Configuration
GRADLE_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"
DOWNLOAD_URL="https://raw.githubusercontent.com/yuchenxue123/builder/main/mite.jar"
TARGET_DIR="$GRADLE_HOME/caches/fml-loom/1.6.4-MITE"
TARGET_FILENAME="1.6.4-MITE.jar"

mkdir -p "$TARGET_DIR"

if command -v curl >/dev/null 2>&1; then
    DOWNLOAD_CMD="curl -L -f -o"
elif command -v wget >/dev/null 2>&1; then
    DOWNLOAD_CMD="wget -O"
else
    echo "Error：Cannot find curl or wget!" >&2
    exit 1
fi

TARGET_PATH="$TARGET_DIR/$TARGET_FILENAME"

if $DOWNLOAD_CMD "$TARGET_PATH" "$DOWNLOAD_URL"; then
    echo "Download successful!"
else
    echo "Download failed!" >&2
    exit 1
fi

if [ -s "$TARGET_PATH" ]; then
    echo "File size: $(du -h "$TARGET_PATH" | cut -f1)"
else
    echo "Error: file is empty" >&2
    exit 1
fi