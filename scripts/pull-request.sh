#!/bin/sh

# Save the current directory
current_dir=$(pwd)

# Source the menuselect.sh script
source "${current_dir}/menuselect.sh"

# Jump to repository root
cd "$(git rev-parse --show-toplevel)"

# Get the description given by parameter, if any
description="$@"

# Deletes the temporary file, if it exists here by an error
template="pull_request_template.md"

echo "✨  ✨  Pull Request ✨  ✨ "
echo "\n"

# Which App?
echo "✨ Aplicativo: 📱"
options=("ANY" "PF" "PJ")
select_option "${options[@]}"
app="${options[$?]}" 

# # Pull request type
echo "✨ Tipo do PR: "
options=("Added" "Changed" "Deprecated" "Removed" "Refactored" "Moved" "Fixed" "Security")
select_option "${options[@]}"
type="${options[$?]}" 

# Jira Ticket
branch=$(git branch --show-current)
ticket=$(echo $branch | sed -r 's/^[[:alpha:]]+\/([[:upper:]]+)\/([[:digit:]]+)\/.+$/\1-\2/')
if [[ $branch = $ticket ]]; then # ask user to input jira ticket
    read -p "Jira ticket: " 
    ticket=$REPLY
fi

# Description
if [[ -z $description ]]; then
    description=$(git show -s --format=%s)
fi


# Join title
title="$app - $type: [$ticket] $description"
echo "\n$title\n"

# Open pull request with gh CLI
gh pr create --title "$title" --base dev --body-file $template --web
