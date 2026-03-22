#!/bin/bash
set -e

# deploy.sh - Deploy skill-repeater-service to k3s (service only).
# Cluster must already be prepared via skill-repeater/deployment/scripts/k3s/deploy-to-k3s.sh
# Usage: ./deploy.sh <version>

SERVICE_NAME="skill-repeater-service"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# deployment/scripts -> deployment/
SERVICE_DEPLOYMENT="$(dirname "$SCRIPT_DIR")"
NAMESPACE="${K8S_NAMESPACE:-skill-repeater}"

if [ $# -lt 1 ]; then
  echo "Usage: $0 <version>" >&2
  exit 1
fi
VERSION=$1

echo "🚀 Deploying $SERVICE_NAME (service only)"
echo "📦 Version: $VERSION"
echo "📁 Namespace: $NAMESPACE"
echo ""

if ! kubectl cluster-info &>/dev/null; then
  echo "❌ Error: Cannot connect to cluster" >&2
  exit 1
fi
echo "✅ Connected to cluster"

if ! kubectl get namespace "$NAMESPACE" &>/dev/null; then
  echo "❌ Error: Namespace $NAMESPACE does not exist. Run deploy-to-k3s.sh first." >&2
  exit 1
fi
echo "✅ Namespace $NAMESPACE exists"
echo ""

if [ -z "$DOCKERHUB_USERNAME" ]; then
  echo "❌ Error: DOCKERHUB_USERNAME is required" >&2
  exit 1
fi
export DOCKERHUB_USERNAME

echo "🚀 Applying $SERVICE_NAME manifest (version: $VERSION)..."
export IMAGE_VERSION=$VERSION
envsubst < "$SERVICE_DEPLOYMENT/$SERVICE_NAME.yaml" | kubectl apply -f -

echo ""
echo "⏳ Waiting for deployment..."
kubectl wait --for=condition=available --timeout=300s "deployment/$SERVICE_NAME" -n "$NAMESPACE" || true

echo ""
echo "✅ $SERVICE_NAME deployment completed!"
kubectl get pods -n "$NAMESPACE" -l app=$SERVICE_NAME
echo ""
echo "💡 View logs: kubectl logs -f deployment/$SERVICE_NAME -n $NAMESPACE"
