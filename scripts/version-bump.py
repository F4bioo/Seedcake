#!/usr/bin/env python3

import re
from datetime import datetime

# Constants
FIRST_RELEASE_YEAR = 2023
FIRST_RELEASE_VERSION = "1"
VERSION_CODE = "const val versionCode"
VERSION_NAME = "const val versionName"
file_path = "buildSrc/src/main/java/Config.kt"

# Read the configuration file
try:
    with open(file_path, 'r') as file:
        lines = file.readlines()
except FileNotFoundError:
    print(f"Error: The file {file_path} was not found.")
    exit(1)

# Get the current date and year
current_date = datetime.now().strftime("%Y.%m.%d")
current_year = int(datetime.now().strftime("%Y"))

# Generate versionCode based on the current year, month, day, hour, and minute
# Take the current year, subtract 2020, then multiply by 100,000,000 (shifts 8 digits)
# Add month * 1,000,000 (shifts 6 digits), day * 10,000 (shifts 4 digits),
# hour * 100 (shifts 2 digits), and minute
year_offset = (current_year - 2020) * 100_000_000
month = int(datetime.now().strftime("%m"))
day = int(datetime.now().strftime("%d"))
hour = int(datetime.now().strftime("%H"))
minute = int(datetime.now().strftime("%M"))

versionCode_new = year_offset + month * 1000000 + day * 10000 + hour * 100 + minute

# Find the current version prefix
current_prefix = FIRST_RELEASE_VERSION
for line in lines:
    if VERSION_NAME in line:
        match = re.search(r'"(.*?)"', line)
        if match:
            current_prefix = match.group(1).split('.')[0].strip()

# Update version prefix if necessary
new_prefix = str(int(current_prefix) + 1) if current_year > FIRST_RELEASE_YEAR else current_prefix

# Update lines in the configuration file
updated_lines = []
for line in lines:
    if VERSION_CODE in line:
        updated_lines.append(f"    {VERSION_CODE}: Int = {versionCode_new}")
    elif VERSION_NAME in line:
        updated_lines.append(f"    {VERSION_NAME}: String = \"{new_prefix}.{current_date}\"")
    else:
        updated_lines.append(line.rstrip('\n'))

# Write updated lines back to the file
try:
    with open(file_path, 'w') as file:
        file.write('\n'.join(updated_lines))
        file.write('\n')
except IOError:
    print(f"Error: Could not write to the file {file_path}.")
    exit(1)
