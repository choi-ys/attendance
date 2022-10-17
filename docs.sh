#!/bin/bash

STATIC_PATH='src/main/resources/static/docs'

function installRedocCli() {
  npm install -git redoc-cli
}

function moveYamlPath() {
  cd ${STATIC_PATH}
}

function generateDocs() {
  removeYamlExpression
  npx redoc-cli build generated.yaml
}

function removeYamlExpression() {
  sed -e 's/\|-//g' <openapi3.yaml >generated.yaml
}

function tearDown() {
  rm -rf openapi3.yaml
  rm -rf generated.yaml
}

function process() {
  installRedocCli
  moveYamlPath
  generateDocs
  tearDown
}

process
