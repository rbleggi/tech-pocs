# Automated Deployment System

This directory implements a deployment system with:
- Jenkins (CI/CD)
- Helm (application deployment)
- OpenTofu (infrastructure as code)
- KIND (local Kubernetes cluster)
- Prometheus & Grafana (monitoring)
- Java application in the app/ directory

## Structure
- **Jenkinsfile**: CI/CD pipeline for building, testing, and deploying applications.
- **Jenkinsfile.infra**: Jenkins pipeline for provisioning infrastructure (cluster, namespaces, Prometheus, Grafana) via OpenTofu.
- **helm/app**: Helm chart for deploying applications with built-in monitoring integration.
- **infra/**: OpenTofu scripts to provision the cluster, namespaces, and install Prometheus/Grafana.
- **docker-compose.yml**: Runs Helm and Docker Registry for local development. KIND is provisioned via OpenTofu, not via Compose.
- **app/**: Java application source code and Dockerfile.

## How it works
1. **Provisioning**
   - Use the Jenkins job defined in `Jenkinsfile.infra` to provision the Kubernetes cluster (KIND), namespaces (`infra` and `apps`), and install Prometheus/Grafana using OpenTofu.
   - For local development, use Docker Compose to run Helm and the Docker Registry. KIND is created via OpenTofu.

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
- Make sure you have kubectl and kind installed for cluster management.

### 1. Start Helm and Docker Registry
In the root of this directory, run:
``` shell
docker compose up -d
```
This will start Helm and a local Docker Registry. The KIND cluster is provisioned via OpenTofu.

### 2. Provision the Cluster, Namespaces, and Monitoring (Automated)
**Importante:** Certifique-se de que o Docker está rodando antes de executar o job de infraestrutura no Jenkins. O cluster KIND será criado automaticamente pelo OpenTofu.

#### Using Jenkins (Recommended)
1. Instale e configure o Jenkins na sua máquina.
2. Crie um novo pipeline job no Jenkins.
3. Aponte o job para o `Jenkinsfile.infra` deste diretório.
4. Execute o job para provisionar a infraestrutura.

Esse job irá:
   - Criar o cluster KIND
   - Criar os namespaces `infra` e `apps`
   - Instalar Prometheus e Grafana no namespace `infra`

### 3. Acesse o Grafana
No host, rode:
``` shell
kubectl --kubeconfig ./kind/kubeconfig.yaml port-forward -n infra svc/grafana 3000:80
```
Depois acesse http://localhost:3000 no navegador.

### 4. Jenkins Setup
- Instale o Jenkins no host (fora do Kubernetes).
- No Jenkins, crie um novo pipeline job e aponte para o Jenkinsfile deste diretório.
- Configure o Jenkins para usar Docker e kubectl se necessário (para build de imagens e deploy no cluster).
- O pipeline irá:
  - Fazer checkout do código
  - Buildar e testar a aplicação Java em app/
  - Buildar e enviar imagens Docker para o registry local (localhost:5000)
  - Deploy da aplicação via Helm para o namespace `apps`
  - Falhar se os testes não passarem

### 5. Deploy de Aplicações
- Use Helm para deploy das aplicações no namespace `apps`.
- Todas as aplicações são integradas automaticamente com Prometheus e Grafana para monitoramento.

## Notas
- O Jenkinsfile pode ser adaptado para outros projetos.
- O chart Helm pode ser customizado para diferentes aplicações.
- OpenTofu pode ser usado para provisionar clusters em nuvem.
- Todas as aplicações são monitoradas por padrão.

## Recomendações
- Use Jenkins para orquestrar todo o fluxo de build, teste e deploy.
- Use OpenTofu para infraestrutura reprodutível.
- Use Helm para deploy e integração de monitoramento.
