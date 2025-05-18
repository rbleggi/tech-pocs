# Jenkins module for OpenTofu

variable "namespace" {
  description = "The namespace where Jenkins will be deployed"
  type        = string
  default     = "infrastructure"
}

variable "jenkins_helm_chart_version" {
  description = "The version of the Jenkins Helm chart to deploy"
  type        = string
  default     = "4.3.0"
}

# Deploy Jenkins using Helm
resource "helm_release" "jenkins" {
  name       = "jenkins"
  repository = "https://charts.jenkins.io"
  chart      = "jenkins"
  version    = var.jenkins_helm_chart_version
  namespace  = var.namespace

  values = [
    file("${path.module}/values.yaml")
  ]

  set {
    name  = "controller.serviceType"
    value = "NodePort"
  }

  set {
    name  = "controller.resources.requests.cpu"
    value = "500m"
  }

  set {
    name  = "controller.resources.requests.memory"
    value = "512Mi"
  }

  set {
    name  = "controller.resources.limits.cpu"
    value = "1000m"
  }

  set {
    name  = "controller.resources.limits.memory"
    value = "1Gi"
  }

  set {
    name  = "persistence.enabled"
    value = "true"
  }

  set {
    name  = "persistence.size"
    value = "8Gi"
  }
}

# Output the Jenkins URL
output "jenkins_url" {
  description = "URL to access Jenkins"
  value       = "http://jenkins.${var.namespace}.svc.cluster.local:8080"
}

# Output the Jenkins admin password command
output "jenkins_admin_password" {
  description = "Command to get the Jenkins admin password"
  value       = "kubectl exec --namespace ${var.namespace} -it svc/jenkins -c jenkins -- /bin/cat /run/secrets/additional/chart-admin-password"
}