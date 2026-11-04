# DevTrack – Complete Developer Growth Platform

**DevTrack** is a production-grade, multi-user developer productivity dashboard built with **Java 21**, **Spring Boot 4.1.2**, **Spring Security**, **Hibernate ORM / Spring Data JPA**, **MySQL 8.0**, **Thymeleaf**, **Bootstrap 5.3**, and **Chart.js**.

---

## 🚀 Key Highlights & Capabilities

- **🔐 Multi-User Authentication & Data Isolation:** Complete tenant isolation powered by Spring Security with `BCryptPasswordEncoder`, session management, CSRF protection, and user-scoped data repositories.
- **📊 Interactive Dashboard:** Live metrics, active goal completion rate, solved DSA problems, active/completed software projects, coding streak metrics, upcoming deadlines, and real-time activity audit feed.
- **🎯 Learning Goal Management:** Full CRUD with category filtering, status tracking (`PENDING`, `IN_PROGRESS`, `COMPLETED`), deadline tracking, and overdue alerts.
- **💻 DSA Problem Tracker:** Filter by LeetCode, GFG, CodeChef; topic-wise grouping (Trees, DP, Graphs, etc.); difficulty badges (`EASY`, `MEDIUM`, `HARD`); problem notes and direct URL shortcuts.
- **📁 Software Project & Milestone Tracker:** Track projects with GitHub repository links, live deployments, expected completion dates, and interactive checklist milestones that automatically calculate progress percentages.
- **📈 8 Interactive Chart.js Visualizations:** Goal completion rate doughnut, 14-day DSA solve timeline, topic distribution bar chart, difficulty pie chart, weekly productivity breakdown, 6-month monthly productivity, project status chart, and daily activity density.
- **🔥 GitHub-Style Coding Streak Engine:** Dynamic calculation of current streak, longest streak, and total active days using an activity audit log with a 16-week activity heatmap.
- **⏰ Reminder & Notification System:** Track due dates, mark as completed or dismissed, and link reminders directly to specific goals or projects.
- **📅 Monthly Calendar:** Unified view of goal deadlines, project target dates, milestone milestones, reminders, and historical activities.

---

## 👥 Default Demo Accounts

The database comes pre-seeded with accounts for immediate exploration:

| Username | Password | Role | Description |
| :--- | :--- | :--- | :--- |
| **`alex`** | `password123` | `ROLE_USER` | Full account with pre-seeded goals, DSA problems, projects, reminders, and a **10-day consecutive streak**. |
| **`sarah`** | `password123` | `ROLE_USER` | Fresh account showcasing instant real-time data isolation. |
| **`admin`** | `admin123` | `ROLE_ADMIN` | Platform administrator account. |

*You can also register a new account on the `/register` page at any time.*

---

## 🛠️ Technology Stack

- **Backend:** Java 21 / 22, Spring Boot 4.1.2, Spring Security 7, Spring MVC, Spring Data JPA, Hibernate ORM
- **Database:** MySQL 8.0
- **Frontend:** Thymeleaf, Bootstrap 5.3, Bootstrap Icons, Chart.js 4.4, Vanilla JS
- **Build & Packaging:** Maven 3.9+
- **Containerization:** Multi-stage Dockerfile (`eclipse-temurin:21`), Docker Compose

---

## 📦 How to Run Locally

### Option 1: Standalone Executable JAR (Fastest)

Ensure MySQL is running, then execute:

```bash
java -jar target/devtrack-0.0.1-SNAPSHOT.jar
```

Access the application in your browser at: **[http://localhost:8085](http://localhost:8085)**

---

### Option 2: Run with Maven

```bash
mvn spring-boot:run
```
*(Or double-click `run-app.bat` on Windows)*

---

### Option 3: Deploy with Docker Compose (Zero Configuration)

Run the entire stack (MySQL 8 + DevTrack Web App) in isolated containers:

```bash
docker-compose up -d --build
```
*(Or double-click `deploy-docker.bat` on Windows)*

Check running containers:
```bash
docker-compose ps
```

View application logs:
```bash
docker-compose logs -f devtrack-app
```

Stop the stack:
```bash
docker-compose down
```

---

## ⚙️ Environment Variables (Production)

| Variable | Default Value | Description |
| :--- | :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | `default` (or `prod`) | Active Spring configuration profile |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/devtrack_db?...` | JDBC connection string |
| `SPRING_DATASOURCE_USERNAME` | `root` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | *(empty)* | Database password |

---

## 📂 Project Directory Structure

```text
devtrack/
├── Dockerfile                      # Multi-stage production build
├── docker-compose.yml              # Complete MySQL + Web container stack
├── deploy-docker.bat               # Windows 1-click Docker deploy
├── deploy-docker.sh                # Linux/macOS Docker deploy script
├── run-app.bat                     # Local Spring Boot run script
├── pom.xml                         # Maven dependencies & plugins
├── src/
│   ├── main/
│   │   ├── java/com/devtrack/devtrack/
│   │   │   ├── config/             # SecurityConfig, DataInitializer
│   │   │   ├── controller/         # Auth, Dashboard, Goals, DSA, Projects, Analytics, etc.
│   │   │   ├── dto/                # Registration, Stats, Calendar, Analytics DTOs
│   │   │   ├── entity/             # User, Goal, DsaProblem, Project, Milestone, Activity, Reminder
│   │   │   ├── enums/              # Role, Difficulty, Platform, GoalCategory, Statuses
│   │   │   ├── exception/          # GlobalExceptionHandler, ResourceNotFoundException
│   │   │   ├── repository/         # User, Goal, DsaProblem, Project, Reminder Repositories
│   │   │   └── service/            # UserService, CustomUserDetailsService, Domain Services
│   │   └── resources/
│   │       ├── application.properties        # Local development profile (port 8085)
│   │       ├── application-prod.properties   # Production profile with environment overrides
│   │       ├── static/                       # CSS (dark SaaS theme), JS, Chart.js configs
│   │       └── templates/                    # Thymeleaf layouts, fragments, modules, auth
│   └── test/
└── target/
    └── devtrack-0.0.1-SNAPSHOT.jar # Pre-built standalone production JAR (~60 MB)
```
