## 🐘 Local Database Setup

This project uses PostgreSQL as its database.
To simplify the development environment, a `docker-compose.yml` file is included at the root of the project to spin up a local instance using Docker.

---

## 📋 Requirements

* Docker installed and running

---

## 🚀 Getting Started

1. Copy the example environment file:

cp .env.example .env

2. Update the `.env` file with your desired values

3. Start the container:

docker compose up -d

4. Stop the container:

docker compose down

---

## 🔍 Verify Connection

You can check if the container is running correctly with:

docker exec -it finance-api pg_isready -U <username>

If successful, you should see:

/var/run/postgresql:5432 - accepting connections

---

## 🧪 Running Database Migrations

The application automatically applies database scripts when it starts.

However, you can also run migrations manually using Maven and Flyway:

mvn flyway:migrate 
-Dflyway.url=<spring.datasource.url> 
-Dflyway.user=<spring.datasource.username> 
-Dflyway.password=<spring.datasource.password>

This allows you to apply changes without restarting the application.

---