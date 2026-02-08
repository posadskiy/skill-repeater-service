#!/bin/bash
set -e

# build-and-push.sh - Build and push skill-repeater-service to Docker Hub
# Usage: ./build-and-push.sh <version>
# Run from skill-repeater-service root.

SERVICE_NAME="skill-repeater-service"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SERVICE_DEPLOYMENT="$(dirname "$(dirname "$SCRIPT_DIR")")"
SERVICE_ROOT="$(dirname "$SERVICE_DEPLOYMENT")"

if [ -z "$DOCKERHUB_USERNAME" ] || [ -z "$DOCKERHUB_TOKEN" ]; then
  echo "Error: DOCKERHUB_USERNAME and DOCKERHUB_TOKEN are required" >&2
  exit 1
fi
if [ -z "$GITHUB_USERNAME" ] || [ -z "$GITHUB_TOKEN" ]; then
  echo "Error: GITHUB_USERNAME and GITHUB_TOKEN are required for build" >&2
  exit 1
fi
if [ $# -lt 1 ]; then
  echo "Usage: $0 <version>" >&2
  exit 1
fi
VERSION=$1
TAG_DATE=$(date +%Y%m%d%H%M%S)

echo "🚀 Building and pushing $SERVICE_NAME (version: $VERSION)..."
docker buildx build --platform linux/arm64 \
  --build-arg GITHUB_USERNAME="$GITHUB_USERNAME" \
  --build-arg GITHUB_TOKEN="$GITHUB_TOKEN" \
  -f "$SERVICE_ROOT/Dockerfile.prod" \
  -t "$DOCKERHUB_USERNAME/$SERVICE_NAME:$VERSION" \
  -t "$DOCKERHUB_USERNAME/$SERVICE_NAME:$TAG_DATE" \
  -t "$DOCKERHUB_USERNAME/$SERVICE_NAME:latest" \
  "$SERVICE_ROOT/" --push

echo "✅ $SERVICE_NAME image built and pushed successfully!"
