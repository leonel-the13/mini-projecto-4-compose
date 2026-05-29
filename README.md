# mini-projecto-4-compose

Autenticação REST API com Spring Boot, JWT e PostgreSQL, empacotada com Docker Compose.

**Resumo**
- **Stack:** Java 21, Spring Boot 3, Spring Security, Spring Data JPA, PostgreSQL, Docker Compose
- **Serviços:** `app` (Spring Boot) e `database` (Postgres)

**Requisitos**
- Docker & Docker Compose
- (Opcional) Maven/Java para rodar localmente sem Docker

**Como rodar (recomendado: Docker Compose)**
- Build e subir (recria imagens):

```bash
docker compose down
docker compose up --build
```

- O Postgres é exposto na porta `5433` (map para `5432` do container). Volume de dados: `postgres_data`.

**Build/run local (sem Docker)**
- Requisitos: JDK 21, Maven

```bash
./mvnw -DskipTests package
java -jar target/*.jar
```

**Configuração importante**
- Variáveis de BD estão em `docker-compose.yml` e `src/main/resources/application.yaml`:
  - `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`
  - A URL usada pela aplicação é: `jdbc:postgresql://database:5432/mydb`

**Endpoints principais**
- POST /auth/register — criar utilizador
  - Exemplo curl:

```bash
curl -i -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Victor Leonel","email":"teste@gmail.com","password":"senha123"}'
```

- POST /auth/login — autenticar e receber JWT
  - Formato do corpo:

```json
{
  "email": "teste@gmail.com",
  "password": "senha123"
}
```

  - Exemplo curl:

```bash
curl -i -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"teste@gmail.com","password":"senha123"}'
```

  - Resposta: `AuthResponse` com campo `token`.

- Rotas protegidas: qualquer outra rota além de `/auth/**` requer `Authorization: Bearer <token>`.

**Segurança / JWT**
- Implementação JWT em `src/main/java/com/example/mini_projecto_4_compose/auth/security/JwtService.java`.
- O filtro `JwtFilter` valida o header `Authorization` no formato `Bearer <token>` e povoará o `SecurityContext` quando o token for válido.

**Arquivos chave (leia se precisar depurar)**
- [AuthController.java](src/main/java/com/example/mini_projecto_4_compose/controller/AuthController.java)
- [AuthService.java](src/main/java/com/example/mini_projecto_4_compose/auth/service/AuthService.java)
- [SecurityConfig.java](src/main/java/com/example/mini_projecto_4_compose/auth/security/SecurityConfig.java)
- [JwtFilter.java](src/main/java/com/example/mini_projecto_4_compose/auth/security/JwtFilter.java)
- [JwtService.java](src/main/java/com/example/mini_projecto_4_compose/auth/security/JwtService.java)
- [CustomUserDetailsService.java](src/main/java/com/example/mini_projecto_4_compose/auth/security/CustomUserDetailsService.java)

**Problemas comuns e soluções**
- `401 Unauthorized` ao chamar `/auth/register`:
  - Certifique-se de que a versão em execução reflete as suas alterações (rebuild e `docker compose up --build`).
  - Verifique `SecurityConfig` permite `/auth/**`.

- `java.lang.NullPointerException` no `JwtFilter` quando não existe header `Authorization`:
  - O filtro já foi ajustado para ignorar requisições sem `Authorization` e seguir a cadeia.

- Erro de inicialização relacionado ao JJWT `WeakKeyException`:
  - Significa que a secret usada para HS256 era demasiado curta. O projeto foi atualizado para usar uma secret HMAC com comprimento adequado.

- `failed to copy: local error: tls: bad record MAC` ao baixar imagens Docker:
  - É um problema de rede/daemon Docker; tente reiniciar o daemon e executar `docker compose pull` separadamente.

- Erro `syntax error at or near "user"` do Postgres:
  - A entidade `User` estava a gerar tabela `user` (palavra reservada). O mapeamento foi trocado para `@Table(name = "users")`.

**Testes rápidos**
- Registrar -> Login -> Acessar rota protegida:
  1. Registrar (POST `/auth/register`)
  2. Login (POST `/auth/login`) e salvar `token`
  3. Fazer GET/POST para rota protegida com header `Authorization: Bearer <token>`

**Contribuição**
- Puxe um branch, faça alterações e abra PR com descrição clara.

**Licença**
- MIT (adapte conforme necessário)

---

Arquivo gerado automaticamente pelo assistente — se quiser que eu detalhe mais alguma secção (ex.: OpenAPI, exemplos Postman, scripts de teste), diga qual e eu acrescento.