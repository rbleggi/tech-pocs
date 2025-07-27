terraform {
  required_providers {
    k3s = {
      source  = "xunleii/k3s"
      version = "~> 0.7.4"
    }
  }
}

provider "k3s" {}

provider "kubernetes" {
    config_path = "${path.module}/../k3s/kubeconfig.yaml"
}

provider "helm" {
  kubernetes = {
    config_path = "${path.module}/../k3s/kubeconfig.yaml"
  }
}
