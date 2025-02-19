#!/bin/bash
set -e

echo -e "🐳 Dockerizing API \n"
cd latte-api
docker build -t api . > /dev/null

cd ..

echo -e "\n🐳 Dockerizing CLIENT \n"
cd latte-client
docker build -t client . > /dev/null
rm -rf dist

echo -e "\n🎉 All Dockerization processes completed! \n"