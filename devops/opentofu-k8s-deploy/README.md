# Automated Deployment System

This directory implements a deployment system with:
- Jenkins (CI/CD)
- Helm (application deployment)
- OpenTofu (infrastructure as code)
- Minikube/kind/k3s (local Kubernetes cluster)
- Prometheus & Grafana (monitoring)

## Structure
- **Jenkinsfile**: CI/CD pipeline for building, testing, and deploying applications.
- **helm/app**: Helm chart for deploying applications with built-in monitoring integration.
- **infra/**: OpenTofu scripts to provision the cluster, namespaces, and install Prometheus/Grafana.
- **docker-compose.yml**: Runs k3s and Helm via Docker Compose for local development.

## How it works
1. **Provisioning**
   - Use OpenTofu (or docker-compose.yml for local setup) to create the Kubernetes cluster and namespaces `infra` and `apps`.
   - Prometheus and Grafana are installed automatically in the `infra` namespace.

2. **Jenkins Pipeline**
   - The Jenkinsfile checks out code, builds, runs automated tests (pipeline fails if tests fail), builds/pushes the Docker image.
   - Deployment can be done via Helm using the chart in `helm/app`.

3. **Application Deployment**
   - Use Helm to install applications in the `apps` namespace.
   - All applications are integrated with Prometheus/Grafana by default (see Helm chart templates).

4. **Monitoring**
   - Prometheus collects metrics from applications.
   - Grafana provides visualization of metrics.

## Step-by-step Setup

### Prerequisites
- Install Docker on your machine.
- Install Jenkins on your host (outside Kubernetes). You can use the official Jenkins Docker image or install it natively. [Jenkins installation guide](https://www.jenkins.io/doc/book/installing/)
- Make sure you have kubectl installed for cluster management.

### 1. Start the Kubernetes Cluster and Helm
In the root of this directory, run:
``` shell
docker compose up -d
```
This will start a local k3s Kubernetes cluster and a Helm container.

### 2. Provision the Cluster, Namespaces, and Monitoring (Automated)
You can automate the creation of namespaces and the installation of Prometheus and Grafana using the provided OpenTofu scripts in the `infra/` directory and Helm charts in `helm/`.

#### Using OpenTofu (Recommended for reproducibility)
1. Go to the `infra` directory:
   ``` shell
   cd infra
   ```
2. Run OpenTofu to provision the cluster, namespaces, and monitoring:
   ``` shell
   tofu apply -auto-approve
   ```
   This will automatically create the Kubernetes cluster (if configured), namespaces (`infra`, `apps`), and install Prometheus and Grafana in the `infra` namespace.

### 3. Access Grafana
On your host, run:
``` shell
kubectl --kubeconfig ./k3s/kubeconfig.yaml port-forward -n infra svc/grafana 3000:80
```
Then open http://localhost:3000 in your browser.

### 4. Jenkins Setup
- Install Jenkins on your host (not inside Kubernetes).
- In Jenkins, create a new pipeline job and point it to the Jenkinsfile in this directory.
- Configure Jenkins to use Docker and kubectl if needed (for building images and deploying to the cluster).
- The pipeline will:
  - Checkout your code
  - Build and test your application
  - Build and push Docker images
  - Deploy your application using Helm to the `apps` namespace
  - Fail if tests do not pass

### 5. Deploy Applications
- Use Helm to deploy your applications to the `apps` namespace.
- All deployed apps are automatically integrated with Prometheus and Grafana for monitoring.

## Notes
- The Jenkinsfile can be adapted for other projects.
- The Helm chart can be customized for different applications.
- OpenTofu can be used to provision clusters in the cloud.
- All deployed apps are monitored by default.

## Recommendations
- Use Jenkins to orchestrate the entire build, test, and deployment flow.
- Use OpenTofu for reproducible infrastructure.
- Use Helm for easy deployment and monitoring integration.
