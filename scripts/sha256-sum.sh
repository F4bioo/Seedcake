#!/bin/bash

full_apk_path=$1
output_file="app/build/outputs/apk/release/SHA256SUM-v${VERSION_NAME}.asc"

# Generate the SHA256 checksum
sha256sum $full_apk_path > temp_checksum

# Read the checksum and full path
read -r checksum full_path < temp_checksum

# Extract file name
filename=$(basename "$full_path")

# Create a new temporary checksum file
echo "$checksum    $filename" > temp_checksum_modified

# Generate the PGP signature
echo "$PGP_PASSPHRASE" | gpg --batch --yes --pinentry-mode loopback --passphrase-fd 0 --armor --clearsign --digest-algo SHA256 temp_checksum_modified

# Move and clean up temporary files
mv temp_checksum_modified.asc $output_file
rm temp_checksum temp_checksum_modified
