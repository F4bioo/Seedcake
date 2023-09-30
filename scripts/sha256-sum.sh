#!/bin/bash

output_file="build-artifacts/SHA256SUM-v${VERSION_NAME}.asc"
temp_checksum="temp_checksum"

# Clear temporary and output files
rm -f "$temp_checksum" "$output_file"

# Loop to generate SHA256 for each file path provided as an argument
for full_path in "$@"; do
  # Generate the SHA256 checksum
  checksum_output=$(sha256sum "$full_path")

  # Extract the checksum and filename
  checksum=$(echo "$checksum_output" | awk '{print $1}')
  filename=$(basename "$full_path")

  # Append the formatted checksum and filename to the temporary file
  echo "$checksum    $filename" >>$temp_checksum
done

# Generate the PGP signature for the checksum file
echo "$PGP_PASSPHRASE" | gpg --batch --yes --pinentry-mode loopback --passphrase-fd 0 --armor --clearsign --digest-algo SHA256 $temp_checksum

# Move and clean up temporary files
mv "$temp_checksum.asc" "$output_file"
rm -f "$temp_checksum"
