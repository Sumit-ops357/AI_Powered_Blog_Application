# AI Powered Blog Application Backend

Spring Boot backend for an AI-assisted blogging platform. It provides JWT authentication, MongoDB-backed blog storage, comments, social actions, analytics, file uploads, and AI writing helpers.

## Features

- User signup and login with BCrypt password hashing.
- JWT-based stateless authentication.
- Blog CRUD with draft and published states.
- Public blog listing with search, category filtering, tag filtering, recent sorting, and popularity sorting.
- Blog view tracking.
- User-specific blog dashboard endpoint.
- Comments with authenticated creation and owner-only deletion.
- Like and bookmark toggles for blogs.
- Author follow/unfollow support.
- Public user profile lookup with password hash removed from responses.
- Author analytics including total blogs, views, likes, comments written, and most popular blog.
- Multipart file upload support for blog media.
- AI writing endpoints for summaries, title ideas, SEO tags, grammar fixes, and continuation suggestions.
- MongoDB Atlas or MongoDB-compatible database support through Spring Data MongoDB.

## Tech Stack

- Java 17 target
- Spring Boot 3.3.5
- Spring Security
- Spring Data MongoDB
- JWT with `jjwt`
- Lombok
- Maven

## Requirements

- JDK 17 or newer
- Maven wrapper included in the project
- MongoDB connection string
- Gemini API key if AI endpoints should call the AI provider

## Environment Setup

Create a local `.env` file from the example:

```powershell
Copy-Item .env.example .env
```

Then update `.env` with your real values:

```properties
SPRING_APPLICATION_NAME=ai-blog
SERVER_PORT=8081
SPRING_DATA_MONGODB_URI=mongodb+srv://<username>:<password>@<cluster-url>/<database>?appName=<app-name>
APP_JWT_SECRET=replace-with-a-long-random-secret
APP_JWT_EXPIRATION_MS=86400000
APP_UPLOAD_DIR=uploads
APP_GEMINI_API_KEY=your-gemini-api-key
APP_GEMINI_MODEL=gemini-2.5-flash
```

The real `.env` file is intentionally ignored by Git because it contains secrets.

## Run Locally

```powershell
.\mvnw.cmd spring-boot:run
```

The API runs on the port configured by `SERVER_PORT`.

## Test

```powershell
.\mvnw.cmd test
```

## Main API Routes

| Method | Route | Description |
| --- | --- | --- |
| `POST` | `/api/auth/signup` | Register a new user |
| `POST` | `/api/auth/login` | Login and receive a JWT |
| `GET` | `/api/blogs` | List published blogs |
| `GET` | `/api/blogs/{id}` | Get a blog by ID and increment views when published |
| `GET` | `/api/blogs/mine` | List authenticated user's blogs |
| `POST` | `/api/blogs` | Create a blog |
| `PUT` | `/api/blogs/{id}` | Update owned blog |
| `DELETE` | `/api/blogs/{id}` | Delete owned blog |
| `GET` | `/api/comments/{blogId}` | List comments for a blog |
| `POST` | `/api/comments/{blogId}` | Add a comment |
| `DELETE` | `/api/comments/{id}` | Delete owned comment |
| `POST` | `/api/social/blogs/{blogId}/like` | Toggle blog like |
| `POST` | `/api/social/blogs/{blogId}/bookmark` | Toggle blog bookmark |
| `POST` | `/api/social/authors/{authorId}/follow` | Toggle author follow |
| `GET` | `/api/social/profiles/{id}` | Get public user profile |
| `GET` | `/api/analytics/me` | Get authenticated user's analytics |
| `POST` | `/api/files/upload` | Upload a file |
| `POST` | `/api/ai/summary` | Generate a blog summary |
| `POST` | `/api/ai/titles` | Generate title ideas |
| `POST` | `/api/ai/tags` | Generate SEO tags |
| `POST` | `/api/ai/grammar` | Fix grammar and spelling |
| `POST` | `/api/ai/suggestions` | Generate continuation suggestions |

## Authentication

Protected routes require a bearer token:

```http
Authorization: Bearer <jwt-token>
```

Signup and login return the token in the response.
