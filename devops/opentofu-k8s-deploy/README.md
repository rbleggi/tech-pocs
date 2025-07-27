# Automated Deployment System

This directory implements a deployment system with:
- Jenkins (CI/CD)
- Helm (application deployment)
- OpenTofu (infrastructure as code)
- Minikube/kind/k3s (local Kubernetes cluster)
- Prometheus & Grafana (monitoring)
- Java application in the app/ directory

## Structure
- **Jenkinsfile**: CI/CD pipeline for building, testing, and deploying applications.
- **Jenkinsfile.infra**: Jenkins pipeline for provisioning infrastructure (cluster, namespaces, Prometheus, Grafana) via OpenTofu.
- **helm/app**: Helm chart for deploying applications with built-in monitoring integration.
- **infra/**: OpenTofu scripts to provision the cluster, namespaces, and install Prometheus/Grafana.
- **docker-compose.yml**: Runs k3s, Helm, Docker Registry, and your Java app via Docker Compose for local development.
- **app/**: Java application source code and Dockerfile.

## How it works
1. **Provisioning**
   - Use the Jenkins job defined in `Jenkinsfile.infra` to provision the Kubernetes cluster, namespaces (`infra` and `apps`), and install Prometheus/Grafana using OpenTofu.
   - For local development, you can use `docker-compose.yml` to spin up k3s, Helm, and the Docker Registry.

2. **Jenkins Pipeline**
   - The main Jenkinsfile checks out code, builds, runs automated tests (pipeline fails if tests fail), builds/pushes the Docker image for the Java app in `app/`.
   - Deployment is done via Helm using the chart in `helm/app`.

3. **Application Deployment**
   - Use Helm to install applications in the `apps` namespace.

4. **Monitoring Integration**
   - All deployed applications are integrated with Prometheus and Grafana by default.

## Step-by-step Setup

### Prerequisites
- Install Docker on your machine.
- Install Jenkins on your host (outside Kubernetes). You can use the official Jenkins Docker image or install it natively. [Jenkins installation guide](https://www.jenkins.io/doc/book/installing/)
- Make sure you have kubectl installed for cluster management.

### 1. Start the Kubernetes Cluster, Helm, and Java App
In the root of this directory, run:
``` shell
docker compose up -d
```
This will start a local k3s Kubernetes cluster, a Helm container, and build/run your Java app from the app/ directory.

### 2. Provision the Cluster, Namespaces, and Monitoring (Automated)
You can automate the creation of namespaces and the installation of Prometheus and Grafana using the provided OpenTofu scripts in the `infra/` directory and Helm charts in `helm/`.

#### Using Jenkins (Recommended)
1. Install and set up Jenkins on your host machine.
2. Create a new pipeline job in Jenkins.
3. Point the job to the `Jenkinsfile.infra` in this directory.
4. Run the Jenkins job to provision the infrastructure.

This Jenkins job will:
   - Create the Kubernetes cluster (if not already created)
   - Set up the `infra` and `apps` namespaces
   - Install Prometheus and Grafana in the `infra` namespace

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
  - Build and test your Java application in app/
  - Build and push Docker images to your local registry (localhost:5000)
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
