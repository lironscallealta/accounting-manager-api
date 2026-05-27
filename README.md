# Accounting Manager API

Microservicio orquestador contable. Consume **users-api**, **sales-api** e **invoice-api** vía `WebClient` y expone casos de uso de **compras**.

Repositorio: [github.com/lironscallealta/accounting-manager-api](https://github.com/lironscallealta/accounting-manager-api)

## Requisitos

- Java 25
- Maven 3.9+
- Docker (MySQL del layout)
- **users-api**, **sales-api** e **invoice-api** en ejecución

## Configuración

```bash
cp .env.example .env
docker compose up -d
./mvnw spring-boot:run
```

Puerto por defecto: **8086** (ver `.env.example`).

## API (`/api/v1/compras`)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/health` | Health check |
| GET | `/api/v1/compras` | Listar compras |
| GET | `/api/v1/compras/{id}` | Ver compra |
| POST | `/api/v1/compras` | Registrar compra |
| PATCH | `/api/v1/compras/{id}/anular` | Anular compra |

Swagger: `http://localhost:8086/swagger-ui.html`

## Estructura

```
src/main/java/cl/duoc/accounting_manager/
├── client/       # SalesClient, InvoiceClient, UsersClient
├── config/       # WebClientConfig
├── controller/   # CompraController, StatusController
├── service/      # AccountingService
├── exception/    # GlobalExceptionHandler
└── dto/          # Request/response
```

## Git remoto

```bash
git remote set-url origin https://github.com/lironscallealta/accounting-manager-api.git
git push -u origin dev
```
