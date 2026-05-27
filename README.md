# Accounting Manager API

Microservicio orquestador contable. Consume **sales-api** e **invoice-api** vía `WebClient` y expone la lógica unificada del dominio Accounting.

## Requisitos

- Java 25
- Maven 3.9+
- **sales-api** y **invoice-api** en ejecución (puertos según tu `.env` local)

## Ejecutar

```bash
./mvnw spring-boot:run
```

## Estructura

```
src/main/java/cl/duoc/accounting_manager/
├── client/          # SalesClient, InvoiceClient (WebClient)
├── config/          # WebClientConfig
└── dto/             # Request/response alineados con los microservicios remotos
```

## Vincular con GitHub

1. Crea un repositorio **vacío** en GitHub (sin README ni `.gitignore`), por ejemplo: `accounting_manager_api`.
2. En esta carpeta (`accounting-manager`):

```bash
git remote add origin https://github.com/TU_USUARIO/accounting_manager_api.git
git push -u origin dev
```

Si ya existe el remoto `origin`, actualízalo:

```bash
git remote set-url origin https://github.com/TU_USUARIO/accounting_manager_api.git
git push -u origin dev
```

## Nota

Este repositorio es **solo** el microservicio `accounting-manager`. No incluye el monorepo `springboot-layout` ni otros APIs del workspace.
