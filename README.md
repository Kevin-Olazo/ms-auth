# ms-auth-facturador-electronico

Emite los JWT del sistema (Spring Security).

- Puerto `8101`, se registra en Eureka como `ms-auth-facturador-electronico`.
- BD MySQL `seguridadfacturador` (tablas `usuario`, `rol`, `usuario_rol`).
- **Antes del primer arranque ejecutar `script-seguridad.sql`**

| Endpoint | Acceso |
|---|---|
| `POST /api/v1/auth/login` `{ "username", "password" }` | publico, devuelve `response.token` y `response.roles` |
| `POST /api/v1/usuarios` | ADMIN (`rolesIds`: ver `GET /api/v1/usuarios/roles`) |
| `GET /api/v1/usuarios/roles` | ADMIN |

Los clientes deben llamar siempre por el gateway (`http://localhost:8090`).
