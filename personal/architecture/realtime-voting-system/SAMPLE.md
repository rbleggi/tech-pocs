# POC Tracker app

### 1. 🎯 Problem Statement and Context

MR Bill, wants a system to keep track of his favorite pocs, so you need to build a mobile app where mr Bill can register all his pocs, and also he needs to be able to search pocs, by name, by language,m by tags. This system should be multi-tenant because mr bill will sell such system to. Bunch of people in brazil, such system must have also ability to generate repots and generate a video with the all pocs the users did in 1 year. Such system must be secure and have proper login and be able to support Realtime dojos using mr bill platform you will build for him.

Restrictions:
- Lambda
- Monoliths
- Single AZ Solutions
- Mobile: Ionic
- Single language for mobile - it needs to be “native”
- MongoDB
- Single Relational DB
- Other Clouds that are not AWS


#### 1.1 Capabilities

* Save POCs.
* Search POCs by name, language, and tags.
* Report generation: Downloadable reports (PDF/CSV) via API.
* Video generation: Endpoint to generate a video summary of user's POCs from the last year.
* Realtime Dojos: Collaborative coding sessions (shared code editor).


### 2. 🎯 Goals

* Leverage React Native to implement the app with native components and good user experience
* System must be Multi-tenant: Each customer has isolated data and authentication.
* Secure authentication and authorization (OAuth2, JWT, password policies).
* Scalable, highly available (multi-AZ, AWS managed services).
* Observability: Logging, metrics, and alerting for all key operations.


### 3. 🎯 Non-Goals

* No support for non-Github POCs like Gitlab or Bitbucket.
* No multi-language mobile codebase to avoid orchestrating different teams for iOS and Android
* No video/text chat on Realtime Dojo. Other tools can help on this and we can focus only on sharing text editor
* No customization of date for the video generation. Our solution will always look for 1 year of content.


### 📐 3. Principles

* Low coupling, high cohesion between services.
* Multi-tenancy: strict data isolation per tenant.
* Observability: all actions logged, metrics for usage and errors.
* Testability: automated tests for all components.
* Scalability: stateless services, auto-scaling groups.
* High availability: multi-AZ deployments, managed AWS services.
* Extensibility: easy to add new features (e.g., new report types).
* Cost efficiency: use managed AWS services, avoid over-provisioning.
* User experience: fast, responsive native app.

### 🏗️ 4. Overall Diagrams

Here there will be a bunch of diagrams, to understand the solution.

🗂️ 4.1 Overall architecture: Show the big picture, relationship between macro components.
<img src="./diagrams/overall.png" />

🗂️ 4.2 Deployment: Show the infra in a big picture.

<img src="./diagrams/deployment.png" />

🗂️ 4.3 Use Cases: Make 1 macro use case diagram that list the main capability that needs to be covered.

#### Dojo Flow

<img src="./diagrams/dojo.png" />


### 🧭 5. Trade-offs

#### 5.1. ECS vs EKS

Technology Choosed: EKS

ECS (Elastic container service) is a fully managed container orchestration service from Amazon.
Worflow:
- Task Definitions: define how containers run: image, CPU, memory, ports, etc.
- Cluster: host tasks, runs on EC2 instances (or Fargate)
- Services: ensure a desired number of tasks copies are running, enabling auto-scaling, health recovery, IAM and CloudWatch
- Has integration points with ALB, ECR, IAM and CloudWatch

EKS in the other hand delivers Kubernetes control plane management on your behalf.
Workflow:
- Kubernetes clusters are managed by AWS (control-plane) and host pods
- Workers (nodes) from the cluster backbone, running on EC2 instance (or Fargate)
- Deployments, Services, ConfigMaps, CRDs, Helms and more kubernetes resources

ECS is best suitable for teams wanting productivity without complexity due to its tight integration with AWS, lean orchestration and ease of use. Famous companies using it includes Amazon, Samsung and Goldman Sachs.

EKS in the other hand is more complex, but offers powerful orchestration, flexibility and portability. It is favored by teams who need Kubernetes features across environments. Famous companies using it are Riot Games, JPMorgan Chase, Capital One.


| | ECS (EC2) | ECS (Fargate) | EKS (EC2) | EKS (Fargate) |
|-|-|-|-|-|
| **Pros (+)**| - No control-plane cost. Only pay for EC2.<br>- Simple to setup and operate.<br>- Ready integration with ALB, Terraform, IAM.<br>- Quick deployment iterations due to lightweight orchestration.<br>- Full control of instance types, AMI, OS tuning | - It has same benefits from ECS, but Fargate is serverless container. AWS manages the underlying hosts. | - Highly portable and flexible.<br>- Advanced ochestration features such namespaces, custom scheduling and autoscaling. | - Same benefits of EKS and AWS manages the underlying hosts |
| **Cons (-)** |  - Feature limitations compared to kubernetes.<br>- Manual Scheduling: tasks stays on EC2 unless moved manually.  | - Costs can increase exponentially, you pay for task vCPU/RAM | - Expensive control plane: $0.10/hour (~ $72/month) per cluster.<br>- More moving parts, IAM integration, node management | - It can increase the cost even more since you pay for each task vCPU/RAM |

#### 5.2. React Native vs. Kotlin/Swift

Technology Chosen: React Native

React Native allows building mobile apps using JavaScript/TypeScript and React, sharing most code across iOS and Android. Native development uses Kotlin for Android and Swift for iOS, with separate codebases for each platform.

Workflow:
- React Native: single codebase (JS/TS), uses React components, bridges to native modules for device features, runs on both iOS and Android.
- Kotlin/Swift: separate codebases, full access to platform APIs, best performance and integration, tailored UX for each OS.

| | React Native | Kotlin/Swift (Native) |
|-|-|-|
| **Pros (+)** | - Single codebase for iOS and Android<br>- Faster initial development<br>- Large community and ecosystem<br>- Hot reload speeds up UI iteration<br>- Good for MVPs and rapid prototyping | - Best performance and responsiveness<br>- Full access to all device APIs and features<br>- Native look and feel<br>- Better long-term maintainability for complex apps<br>- Easier integration with platform-specific SDKs |
| **Cons (-)** | - Lower performance for complex/animation-heavy apps<br>- Some native modules require custom bridging<br>- Occasional lag behind latest OS features<br>- Debugging can be more complex<br>- Not always pixel-perfect native UX | - Two codebases to maintain<br>- Higher initial development cost<br>- Requires expertise in both Kotlin and Swift<br>- Slower to deliver cross-platform features<br>- Requires higher orchestration overhead for release features accross multi platforms |


#### 5.3. Cognito vs. Custom Auth

Technology Chosen: Custom Auth

Custom Auth means building all authentication logic from scratch, including user management, password storage, and token generation. Cognito is a managed authentication and user management service from AWS.

Workflow:
- Custom Auth: implement login, password hash, tokens, policies, manual multi-tenant.
- Cognito: user pools, login/social, OAuth2/JWT, AWS integration, multi-tenant.

| | Custom Auth | Cognito |
|-|-|-|
| **Pros (+)** | - Fully customizable<br>- No vendor lock-in<br>- Can meet unique business/security requirements<br>- Full control over user experience and data | - Managed and secure<br>- Multi-tenant support<br>- OAuth2/JWT/SAML<br>- AWS integration<br>- Scalable |
| **Cons (-)** | - High dev/ops effort<br>- Security risks if not implemented correctly<br>- Manual scalability<br>- Must handle compliance and best practices | - Less customizable<br>- Learning curve<br>- Some AWS lock-in |

### 🌏 6. For each key major component

#### 6.1 Auth

<img src="./diagrams/auth.png" />

#### 6.2 Video Report

<img src="./diagrams/video-generation.png" />

### 🖹 8. Testing strategy

* Unit tests for all backend services and mobile(JUnit/Jest)
* Integration tests for API endpoints
* UI tests for mobile app using testing-library
* Load tests for report/video endpoints using k6
* Security tests: auth, multi-tenant isolation
* Chaos testing for EKS/Aurora failover

### 🖹 9. Observability strategy

* CloudWatch logs for all services
* X-Ray(IA) tracing for API calls
* Alerts for errors, high latency
* Dashboards for usage and error rates

### 🖹 10. Data Store Designs

<img src="./diagrams/erd.png" />


### 🖹 11. Technology Stack

* Mobile: React Native v0.81
* Backend: Java 25
* DB: Amazon Aurora PostgreSQL (multi-AZ)
* Object Storage: AWS S3
* Container Orchestration: AWS EKS Fargate
* Realtime: WebSocket service on EKS
* Video/Report: Python using Langchain to connect with AI (GPT5 nano), running in EKS
* Monitoring: CloudWatch, X-Ray
* CI/CD: Jenkins


### 🖹 12. References

* Architecture Anti-Patterns: https://architecture-antipatterns.tech/
* EIP https://www.enterpriseintegrationpatterns.com/
* SOA Patterns https://patterns.arcitura.com/soa-patterns
* API Patterns https://microservice-api-patterns.org/
* Anti-Patterns https://sourcemaking.com/antipatterns/software-development-antipatterns
* Refactoring Patterns https://sourcemaking.com/refactoring/refactorings
* Database Refactoring Patterns https://databaserefactoring.com/
* Data Modelling Redis https://redis.com/blog/nosql-data-modeling/
* Cloud Patterns https://docs.aws.amazon.com/prescriptive-guidance/latest/cloud-design-patterns/introduction.html
* 12 Factors App https://12factor.net/
* Relational DB Patterns https://www.geeksforgeeks.org/design-patterns-for-relational-databases/
* Rendering Patterns https://www.patterns.dev/vanilla/rendering-patterns/
* REST API Design https://blog.stoplight.io/api-design-patterns-for-rest-web-services