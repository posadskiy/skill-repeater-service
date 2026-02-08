## skill-repeater-service

Backend for Skill Repeater (Micronaut).

### Deployment (Kubernetes / k3s)

This service has a **`deployment/`** folder: manifest `deployment/skill-repeater-service.yaml` and scripts **`deployment/scripts/deploy.sh`**, **`deployment/scripts/build-and-push.sh`**. Shared cluster config lives in the parent repo’s **`deployment/`**. Prepare the cluster once from the parent repo, then deploy this service:

```bash
./deployment/scripts/deploy.sh <version>
./deployment/scripts/build-and-push.sh <version>
```
