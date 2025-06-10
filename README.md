# Recipe Recommender - Backend

This is the backend server for the Recipe Recommender web application. It provides RESTful APIs for managing users, recipes, ingredients, and personalized recipe recommendations.

## 🚀 Tech Stack

- Java 17+
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- PostgreSQL
- JUnit 5, Mockito

## 📁 Project Structure

- `controller/` - REST controllers exposing API endpoints
- `service/` - Business logic layer
- `repository/` - Data access layer using Spring Data JPA
- `model/` - Entity classes mapped to the PostgreSQL database
- `dto/` - Data Transfer Objects for API communication
- `config/` - Application configuration (e.g. security, CORS)

## 🧪 Testing

- Framework: JUnit 5
- Mocking: Mockito
- Key Tests:
  - Recipe management (add, update, delete)
  - Favorites (add/remove/get)
  - Recipe search and recommendations

## 📌 API Endpoints

### Recipe Creator

- `POST /api/creator/add-recipe` – Add new recipe
- `GET /api/creator/my-recipes/{creatorId}` – List recipes by creator
- `PUT /api/creator/update-recipe/{id}` – Update recipe
- `DELETE /api/creator/delete-recipe/{id}` – Delete recipe

### Home Cook

- `POST /api/user/get-recommendations` – Get recipes by ingredients
- `GET /api/user/favorites/{userId}` – Get favorite recipes
- `POST /api/user/favorites/add` – Add to favorites
- `DELETE /api/user/favorites/remove/{recipeId}` – Remove favorite

## ⚙️ Database

- PostgreSQL
- ER model includes tables for:
  - `Users`, `Recipes`, `Ingredients`, `Favorites`, `Roles`

## 🛡 Security

- Role-based access control (Admin, Home Cook, Recipe Creator)
- Authentication using username/password

## 🔮 Future Improvements

- Machine learning-based recommendations
- Admin dashboard
- Advanced filtering (e.g. cuisine type, dietary tags)
