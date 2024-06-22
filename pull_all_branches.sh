#!/bin/sh
for branch in $(git branch | sed 's/..//'); do
    git checkout $branch
    git pull origin $branch
done
