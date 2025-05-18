# Docker Compose Implementation

This document provides information about the Docker Compose implementation of the DevOps Deployment System.

## Overview

For local development and testing, this system can be easily deployed using Docker Compose.

## Prerequisites

- Docker
- Docker Compose

## Running the System

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/devops-deployment-system.git
    cd devops-deployment-system
    ```

2. Start all services (rebuild images if necessary):
    ```sh
    docker-compose up -d --build
    ```

3. Check the status of the services:
    ```sh
    docker-compose ps
    ```

4. View logs for a specific service:
    ```sh
    docker-compose logs [service_name]
    ```
    For example:
    ```sh
    docker-compose logs api
    docker-compose logs jenkins
    ```

5. Stop all services:
    ```sh
    docker-compose down -v --remove-orphans
    ```

## Accessing the Services

After starting the Docker Compose environment, you can access the services at the following URLs:

- Spring Boot API: [http://localhost:8080](http://localhost:8080)
- Jenkins: [http://localhost:8081](http://localhost:8081)
- Prometheus: [http://localhost:9090](http://localhost:9090)
- Grafana: [http://localhost:3000](http://localhost:3000)
- Kubernetes API: [https://localhost:6443](https://localhost:6443)
- Helm Kubernetes API: [https://localhost:6444](https://localhost:6444)
- OpenTofu Kubernetes API: [https://localhost:6445](https://localhost:6445)

## Docker Compose Configuration

The Docker Compose configuration includes the following services:

### Kubernetes - K3s

```yaml
k3s:
  image: rancher/k3s:v1.25.8-k3s1
  container_name: k3s
  command: server --disable traefik
  privileged: true
  ports:
    - "6443:6443"  # Kubernetes API
  environment:
    - K3S_TOKEN=k3s-token
    - K3S_KUBECONFIG_OUTPUT=/output/kubeconfig.yaml
    - K3S_KUBECONFIG_MODE=666
  volumes:
    - k3s-data:/var/lib/rancher/k3s
    - ./k3s:/output
  networks:
    - app-network
  restart: unless-stopped
```

### Helm

```yaml
helm:
  image: alpine/helm:3.11.1
  container_name: helm
  depends_on:
    - k3s
  ports:
    - "6444:6443"  # Expose Kubernetes API port for Helm
  volumes:
    - ./k3s:/config
    - ./helm:/apps
  environment:
    - KUBECONFIG=/config/kubeconfig.yaml
  entrypoint: ["/bin/sh", "-c", "sleep infinity"]
  networks:
    - app-network
  restart: unless-stopped
```

### OpenTofu

```yaml
opentofu:
  image: ghcr.io/opentofu/opentofu:1.6.0
  container_name: opentofu
  depends_on:
    - k3s
  ports:
    - "6445:6443"  # Expose Kubernetes API port for OpenTofu
  volumes:
    - ./k3s:/config
    - ./opentofu:/workspace
  environment:
    - KUBECONFIG=/config/kubeconfig.yaml
  working_dir: /workspace
  entrypoint: ["/bin/sh", "-c", "sleep infinity"]
  networks:
    - app-network
  restart: unless-stopped
```

### Spring Boot API

```yaml
api:
  build:
    context: .
    dockerfile: Dockerfile
  container_name: sample-api
  ports:
    - "8080:8080"
  environment:
    - SPRING_PROFILES_ACTIVE=docker
    - JAVA_OPTS=-Xmx512m -Xms256m
  networks:
    - app-network
  healthcheck:
    test: ["CMD", "wget", "-q", "--spider", "http://localhost:8080/actuator/health"]
    interval: 30s
    timeout: 10s
    retries: 3
    start_period: 40s
  restart: unless-stopped
```

### Jenkins

```yaml
jenkins:
  image: jenkins/jenkins:lts-jdk17
  container_name: jenkins
  ports:
    - "8081:8080"
    - "50000:50000"
  environment:
    - JAVA_OPTS=-Xmx1g -Xms512m -Djenkins.install.runSetupWizard=false
  volumes:
    - jenkins_home:/var/jenkins_home
    - /var/run/docker.sock:/var/run/docker.sock
  networks:
    - app-network
  restart: unless-stopped
```

### Prometheus

```yaml
prometheus:
  image: prom/prometheus:v2.45.0
  container_name: prometheus
  ports:
    - "9090:9090"
  volumes:
    - ./prometheus:/etc/prometheus
    - prometheus_data:/prometheus
  command:
    - '--config.file=/etc/prometheus/prometheus.yml'
    - '--storage.tsdb.path=/prometheus'
    - '--web.console.libraries=/etc/prometheus/console_libraries'
    - '--web.console.templates=/etc/prometheus/consoles'
    - '--web.enable-lifecycle'
  networks:
    - app-network
  restart: unless-stopped
  depends_on:
    - api
    - jenkins
```

### Grafana

```yaml
grafana:
  image: grafana/grafana:9.3.6
  container_name: grafana
  ports:
    - "3000:3000"
  environment:
    - GF_SECURITY_ADMIN_USER=admin
    - GF_SECURITY_ADMIN_PASSWORD=admin
    - GF_USERS_ALLOW_SIGN_UP=false
    - GF_SERVER_ROOT_URL=%(protocol)s://%(domain)s:%(http_port)s
    - GF_AUTH_ANONYMOUS_ENABLED=true
    - GF_AUTH_ANONYMOUS_ORG_ROLE=Viewer
  volumes:
    - grafana_data:/var/lib/grafana
    - ./grafana/provisioning:/etc/grafana/provisioning
  networks:
    - app-network
  restart: unless-stopped
  depends_on:
    - prometheus
```

## Networks and Volumes

```yaml
networks:
  app-network:
    driver: bridge

volumes:
  jenkins_home:
  prometheus_data:
  grafana_data:
  k3s-data:
```