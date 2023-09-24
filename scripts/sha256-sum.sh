#!/bin/bash

output_file="app/build/outputs/apk/release/SHA256SUM-v${VERSION_NAME}.asc"
temp_checksum="temp_checksum"

# Clear temporary and output files
rm -f $temp_checksum $output_file

# Loop to generate SHA256 for each file path provided as an argument
for full_path in "$@"
do
    # Generate the SHA256 checksum and append it to the temporary file
    sha256sum $full_path >> $temp_checksum
done

# Generate the PGP signature for the checksum file
echo "$PGP_PASSPHRASE" | gpg --batch --yes --pinentry-mode loopback --passphrase-fd 0 --armor --clearsign --digest-algo SHA256 $temp_checksum

# Move and clean up temporary files
mv "$temp_checksum.asc" $output_file
rm -f $temp_checksum
