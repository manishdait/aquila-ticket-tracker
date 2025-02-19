#!/bin/bash
set -e

echo -e "🐳 Dockerizing API \n"
cd latte-api
docker build -t latte-api . > /dev/null

cd ..

echo -e "\n🐳 Dockerizing CLIENT \n"
cd latte-client
docker build -t latte-client . > /dev/null

echo -e "\n🎉 All Dockerization processes completed! \n"
