import subprocess
import time
import requests
import pytest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.chrome.options import Options

SERVICES = [
    {"name": "jenkins", "url": "http://localhost:8080"},
    {"name": "prometheus", "url": "http://localhost:9090"},
    {"name": "grafana", "url": "http://localhost:3000"},
]

def open_browser_for_services():
    chrome_options = Options()
    # Para ver o navegador, mantenha a linha abaixo comentada
    # chrome_options.add_argument("--headless")
    service = ChromeService()  # Assumes chromedriver is in PATH
    driver = webdriver.Chrome(service=service, options=chrome_options)
    try:
        for svc in SERVICES:
            print(f"[BROWSER] Opening {svc['name']} at {svc['url']}")
            driver.get(svc["url"])
            time.sleep(7)  # Wait for user to see the page
        print("[BROWSER] All service pages have been opened for verification.")
        time.sleep(5)  # Final pause before closing browser
    finally:
        driver.quit()

def setup_module(module):
    print("[SETUP] Starting docker-compose services...")
    subprocess.run(["docker-compose", "up", "-d", "--build"], check=True)
    print("[SETUP] Waiting for services to be ready...")
    time.sleep(30)
    print("[SETUP] Services should be up.")
    open_browser_for_services()

def teardown_module(module):
    print("[TEARDOWN] Stopping docker-compose services...")
    subprocess.run(["docker-compose", "down", "-v", "--remove-orphans"], check=True)
    print("[TEARDOWN] Services stopped.")

def is_service_up(url):
    try:
        r = requests.get(url, timeout=3)
        return r.ok
    except Exception:
        return False

@pytest.mark.parametrize("service", SERVICES)
def test_service_running(service):
    try:
        response = requests.get(service["url"], timeout=10)
        assert response.status_code < 500
    except Exception as e:
        pytest.fail(f"{service['name']} not responding: {e}")

@pytest.mark.skipif(
    not is_service_up("http://localhost:8080"),
    reason="Jenkins is not running for pipeline test"
)
def test_jenkins_pipeline():
    jenkins_url = "http://localhost:8080"
    job_name = "test-pipeline"
    # Create a simple pipeline job
    job_config = '''<flow-definition plugin="workflow-job">
  <description>Test pipeline</description>
  <keepDependencies>false</keepDependencies>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition" plugin="workflow-cps">
    <script>node { echo 'Hello Jenkins' }</script>
    <sandbox>true</sandbox>
  </definition>
</flow-definition>'''
    auth = ("admin", "admin")
    create_url = f"{jenkins_url}/createItem?name={job_name}"
    headers = {"Content-Type": "application/xml"}
    r = requests.post(create_url, data=job_config, headers=headers, auth=auth)
    assert r.status_code in [200, 201, 302], f"Failed to create job: {r.text}"
    build_url = f"{jenkins_url}/job/{job_name}/build"
    r = requests.post(build_url, auth=auth)
    assert r.status_code in [201, 302], f"Failed to trigger build: {r.text}"
    time.sleep(10)
    last_build_url = f"{jenkins_url}/job/{job_name}/lastBuild/api/json"
    r = requests.get(last_build_url, auth=auth)
    assert r.status_code == 200, f"Failed to get build status: {r.text}"
    build_info = r.json()
    assert build_info["result"] == "SUCCESS", f"Build failed: {build_info}"

@pytest.mark.skipif(
    not is_service_up("http://localhost:3000"),
    reason="Grafana is not running for dashboard test"
)
def test_grafana_dashboard():
    """
    Tests creating a simple dashboard in Grafana via API.
    """
    grafana_url = "http://localhost:3000"
    api_key = "admin:admin"  # Adjust if needed
    dashboard = {
        "dashboard": {
            "id": None,
            "uid": None,
            "title": "Test Dashboard",
            "panels": [
                {
                    "type": "graph",
                    "title": "Test Panel",
                    "gridPos": {"x": 0, "y": 0, "w": 24, "h": 8},
                    "targets": [],
                }
            ],
        },
        "overwrite": True
    }
    headers = {"Content-Type": "application/json"}
    r = requests.post(f"{grafana_url}/api/dashboards/db", json=dashboard, headers=headers, auth=("admin", "admin"))
    assert r.status_code in [200, 201], f"Failed to create dashboard: {r.text}"
    # Check if dashboard was created
    dash_url = f"{grafana_url}/api/search?query=Test%20Dashboard"
    r = requests.get(dash_url, auth=("admin", "admin"))
    assert r.status_code == 200 and any(d['title'] == 'Test Dashboard' for d in r.json()), "Dashboard not found"

@pytest.mark.skipif(
    not is_service_up("http://localhost:3000"),
    reason="Grafana is not running for browser test"
)
def test_grafana_browser():
    """
    Opens the browser and navigates to Grafana login page.
    You will see the browser window during the test.
    """
    chrome_options = Options()
    # Comment out the next line to see the browser window
    # chrome_options.add_argument("--headless")
    service = ChromeService()  # Assumes chromedriver is in PATH
    driver = webdriver.Chrome(service=service, options=chrome_options)
    try:
        print("[SELENIUM] Opening Grafana in browser...")
        driver.get("http://localhost:3000/login")
        time.sleep(5)  # Wait so you can see the page
        assert "Grafana" in driver.title
    finally:
        driver.quit()
