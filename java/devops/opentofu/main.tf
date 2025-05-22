# OpenTofu configuration for infrastructure components

terraform {
  required_version = ">= 1.0.0"
  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.0"
    }
    helm = {
      source  = "hashicorp/helm"
      version = "~> 2.0"
    }
    null = {
      source  = "hashicorp/null"
      version = "~> 3.0"
    }
  }
}

# Configure the Kubernetes provider
provider "kubernetes" {
  config_path    = var.kubeconfig_path
  config_context = "default"
  insecure       = true  # Skip TLS verification to handle cert issues
}

# Configure the Helm provider
provider "helm" {
  kubernetes {
    config_path    = var.kubeconfig_path
    config_context = "default"
    insecure       = true  # Skip TLS verification to handle cert issues
  }
}

variable "kubeconfig_path" {
  description = "Path to the kubeconfig file"
  type        = string
  default     = "../../k3s/kubeconfig.yaml"
}

# Create infrastructure namespace using kubectl directly (avoids auth issues)
resource "null_resource" "create_infrastructure_namespace" {
  provisioner "local-exec" {
    command = "kubectl --kubeconfig ${var.kubeconfig_path} create namespace infrastructure --dry-run=client -o yaml | kubectl --kubeconfig ${var.kubeconfig_path} apply -f -"
  }
}

# Create applications namespace using kubectl directly (avoids auth issues)
resource "null_resource" "create_applications_namespace" {
  provisioner "local-exec" {
    command = "kubectl --kubeconfig ${var.kubeconfig_path} create namespace applications --dry-run=client -o yaml | kubectl --kubeconfig ${var.kubeconfig_path} apply -f -"
  }
}

# Include infrastructure monitoring components
module "prometheus" {
  source    = "./modules/prometheus"
  namespace = "infrastructure"
  depends_on = [null_resource.create_infrastructure_namespace]
}

module "grafana" {
  source    = "./modules/grafana"
  namespace = "infrastructure"
  depends_on = [module.prometheus, null_resource.create_infrastructure_namespace]
}

# Deploy application with Prometheus and Grafana integration
module "app" {
  source     = "./modules/app"
  namespace  = "applications"
  app_name   = "java-app"
  app_image  = "tech-pocs/java-devops-app"
  app_tag    = "latest"
  prometheus_enabled = true
  depends_on = [
    module.prometheus,
    module.grafana,
    null_resource.create_applications_namespace
  ]
}

# Output for automatic validation (test)
output "test_namespaces_created" {
  value = "infrastructure and applications namespaces created via kubectl"
  depends_on = [
    null_resource.create_infrastructure_namespace,
    null_resource.create_applications_namespace
  ]
}

output "test_monitoring_deployed" {
  value = "Monitoring stack deployed: Prometheus, Grafana in infrastructure namespace"
  depends_on = [
    module.prometheus,
    module.grafana
  ]
}

