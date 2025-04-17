# 🧪 API Test Automation Framework

A robust and extensible **API Testing Framework** built with **Java 17**, **Cucumber**, **TestNG**, and **RestAssured**.
Designed for readable, maintainable, and scalable API automation parallel execution support.

---

## 📦 Tech Stack

- **Java 17**
- **Maven**
- **TestNG**
- **Cucumber JVM**
- **RestAssured**
- **Lombok**
- **Logback / SLF4J for logging**

---

## ✅ Getting Started

### Prerequisites

- Java 17
- Maven 3.x

### Install Dependencies

To install the required dependencies and set up the project, run the following Maven command:

```bash
mvn clean install

mvn clean verify

```

### Parallel Execution

```bash
mvn clean verify -Dparallel=methods -DthreadCount=4
```

### Configuration

Configuration settings can be managed in the config.properties file located under src/test/resources/config/.
Update it with your API base URI and other settings:

### Maintainers

GitHub:[@girishmanyam]


