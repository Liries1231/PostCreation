# ms-post

## Описание

Microservice Post отвечает за управление постами. Сервис предоставляет CRUD-операции для работы с постами, обеспечивая их привязку к аутентифицированному пользователю. Каждый запрос на создание или изменение поста ожидает наличие заголовка `UserData`, который содержит идентификатор пользователя.

## Основные функции

- **Создание постов**: добавление новых постов, привязанных к пользователю.
- **Получение постов**: просмотр постов по идентификатору.
- **Обновление постов**: изменение данных существующего поста.
- **Удаление постов**: удаление постов, принадлежащих пользователю.

## Особенности

- **Привязка постов к пользователям**: каждый пост принадлежит конкретному аутентифицированному пользователю.
- **Проверка заголовка UserData**: сервис ожидает заголовок `UserData` с идентификатором пользователя для всех операций, связанных с изменением данных.

## Технологии

- **Java 21**
- **Spring Boot**
- **PostgreSQL**
- **JSON Web Tokens (JWT)**

## Установка и запуск

1. Склонируйте репозиторий:
   ```bash
   git clone <repository_url>
   cd ms-post
   ```
2. Настройте `application.yml` (если требуется). Обратите внимание на настройки базы данных и секрета JWT.
3. Соберите и запустите приложение:
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

## Структура проекта

- **PostController**:

  - `GET /api/v1/post/{id}`: получение информации о посте по ID.
  - `POST /api/v1/post`: создание нового поста (ожидает заголовок `UserData`).
  - `PUT /api/v1/post/{id}`: обновление поста (ожидает заголовок `UserData`).
  - `DELETE /api/v1/post/{id}`: удаление поста (ожидает заголовок `UserData`).

- **PostService**:

  - Обрабатывает бизнес-логику для управления постами.
  - Проверяет права пользователя на создание, изменение и удаление постов.

- **PostRepository**:

  - Слой доступа к данным, использующий PostgreSQL.

- **Post**:

  - Основная сущность, представляющая пост, с полями:
    - `id`: идентификатор поста.
    - `userId`: идентификатор пользователя, которому принадлежит пост.
    - `content`: содержимое поста.
    - `createdAt`: дата создания поста.
    - `updatedAt`: дата последнего изменения поста.

## Тестирование

1. Используйте Postman для тестирования эндпоинтов создания, изменения и удаления постов.
2. Проверьте, что операции выполняются только при наличии корректного заголовка `UserData`.
3. Убедитесь, что пользователь может изменять только свои посты.

## Основные зависимости

- **Spring Boot Starter Web**: для разработки REST API.
- **Spring Boot Starter Data JPA**: для работы с базой данных.
- **PostgreSQL Driver**: для подключения к базе данных PostgreSQL.
- **Lombok**: для сокращения шаблонного кода.
- **java-jwt**: для обработки JWT токенов.
- **Spring Boot Starter Test**: для написания тестов.

---


