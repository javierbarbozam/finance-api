## Local Database Configuration

Este proyecto utiliza PostgreSQL como base de datos. Para facilitar el entorno de 
desarrollo, en la raíz del proyecto se incluye un archivo `docker-compose.yml` que 
levanta una instancia local usando Docker.

### Requisitos
- Docker instalado y corriendo

### Pasos
1. Copia el archivo de ejemplo: `cp .env.example .env`
2. Edita `.env` con tus valores
3. Levanta el contenedor: `docker compose up -d`
4. Para detenerlo: `docker compose down`

### Comprobar conexión
Puede utilizar el siguiente comando para corroborar que el contenedor responde correctamente
`docker exec -it finance-api  pg_isready -U <username>`

Si la respuesta es '/var/run/postgresql:5432 - accepting connections' la creación del contenedor fue exitosa.