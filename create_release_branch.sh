#!/bin/bash

# Constants
CONFIG_FILE="buildSrc/src/main/java/Config.kt"

# Function to bump the version in Config.kt
bump_version() {
  echo "Bumping version in $CONFIG_FILE..."

  current_year=$(date +"%Y")
  current_month=$(date +"%-m")
  current_day=$(date +"%-d")
  current_hour=$(date +"%-H")
  current_minute=$(date +"%-M")
  current_date="${current_year}.${current_month}.${current_day}"

  year_offset=$(( (current_year - 2020) * 100000000 ))
  month_offset=$(( current_month * 1000000 ))
  day_offset=$(( current_day * 10000 ))
  hour_offset=$(( current_hour * 100 ))
  versionCode=$(( year_offset + month_offset + day_offset + hour_offset + current_minute ))

  # Extract current version prefix from Config.kt
  current_prefix=$(grep 'const val versionName:' $CONFIG_FILE | awk -F\" '{print $2}' | awk -F. '{print $1}')

  # Update version prefix if necessary
  new_prefix=$current_prefix
  if [ $current_year -gt 2023 ]; then
    new_prefix=$((current_prefix + 1))
  fi

  # Update Config.kt file
  sed -i "s/const val versionCode: Int = [0-9]\+/const val versionCode: Int = $versionCode/" $CONFIG_FILE
  sed -i "s/const val versionName: String = \"[0-9]*\.[0-9]*\.[0-9]*\"/const val versionName: String = \"$new_prefix.$current_date\"/" $CONFIG_FILE

  echo "Version bumped successfully."
}

# Function to commit and push changes
commit_and_push() {
  git add $CONFIG_FILE
  commit_message="Added: Create release branch v$new_prefix.$current_date build $versionCode"

  # Ask if user wants to add all files to the commit
  read -p "Do you want to add all uncommitted files to this commit? (y/n): " add_all
  if [ "$add_all" == "y" ]; then
    git add .
  fi

  git commit -m "$commit_message"

  # Create new release branch
  new_branch="release/$new_prefix.$current_date_$versionCode"
  git checkout -b $new_branch

  # Ask if user wants to push immediately
  read -p "Do you want to push these changes now? (y/n): " do_push
  if [ "$do_push" == "y" ]; then
    git push origin $new_branch
    echo "Branch $new_branch created and push done successfully."
  else
    echo "Branch $new_branch created. Remember to push the changes."
  fi
}

# Main execution starts here
echo "Starting the script..."

# Ask if user wants to bump the version
read -p "Do you want to bump the version in Config.kt? (y/n): " do_bump
if [ "$do_bump" == "y" ]; then
  bump_version
  commit_and_push
else
  echo "Exiting without making any changes."
fi
