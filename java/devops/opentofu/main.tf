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
  }
}

# Configure the Kubernetes provider
provider "kubernetes" {
  config_path    = var.kubeconfig_path
  config_context = "default"
}

# Configure the Helm provider
provider "helm" {
  kubernetes {
    config_path    = var.kubeconfig_path
    config_context = "default"
  }
}

variable "kubeconfig_path" {
  description = "Path to the kubeconfig file"
  type        = string
  default     = "../../k3s/kubeconfig.yaml"
}

# Create infrastructure namespace if it doesn't exist
resource "kubernetes_namespace" "infrastructure" {
  metadata {
    name = "infrastructure"
  }
}

# Create applications namespace if it doesn't exist
resource "kubernetes_namespace" "applications" {
  metadata {
    name = "applications"
  }
}

# Include other infrastructure components
module "jenkins" {
  source = "./modules/jenkins"
  namespace = kubernetes_namespace.infrastructure.metadata[0].name
}

module "prometheus" {
  source = "./modules/prometheus"
  namespace = kubernetes_namespace.infrastructure.metadata[0].name
}

module "grafana" {
  source = "./modules/grafana"
  namespace = kubernetes_namespace.infrastructure.metadata[0].name
  depends_on = [module.prometheus]
}
