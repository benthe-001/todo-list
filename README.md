# Todo API

API REST de gestion de tâches développée avec **Spring Boot**,
suivant les principes du **Domain Driven Design (DDD)**
et de l'architecture **Modulith**.

---

## Stack technique

| Technologie          | Version     | Rôle                          |
|----------------------|-------------|-------------------------------|
| Java                 | 21          | Langage principal             |
| Spring Boot          | 3.5         | Framework principal           |
| Spring Data JPA      | -           | Accès base de données         |
| H2 Database          | En mémoire  | Base de données embarquée     |
| Lombok               | -           | Réduction du code boilerplate |
| SpringDoc OpenAPI    | 2.3.0       | Documentation Swagger         |
| Maven                | 3.8+        | Gestion des dépendances       |

---

## Architecture du projet

Le projet suit une architecture **Modulith + DDD** organisée en couches avec un seul Bounded Context : `task`.

```
src/main/java/com/todo/todo_api/
|
+-- task/
    |
    +-- domain/
    |   +-- Task.java
    |   +-- TaskStatus.java
    |
    +-- application/
    |   +-- TaskService.java
    |   +-- dto/
    |       +-- TaskRequest.java
    |       +-- TaskResponse.java
    |
    +-- infrastructure/
    |   +-- TaskRepository.java
    |
    +-- presentation/
        +-- TaskController.java
```

### Principe des couches

- **domain** → contient les règles métier pures, sans dépendance framework
- **application** → orchestre le domaine, contient les DTOs
- **infrastructure** → tout ce qui est technique (BDD, JPA)
- **presentation** → reçoit les requêtes HTTP et retourne les réponses

---

## Prérequis

- Java 21+
- Maven 3.8+

---

## Lancer l'application

**Cloner le projet :**

```bash
git clone https://gitlab.com/backend3242218/to-do-list.git
cd to-do-list
```

**Démarrer l'application :**

```bash
mvn spring-boot:run -DskipTests
```

L'application démarre sur : `http://localhost:8081`

---

##  Documentation Swagger

URL : `http://localhost:8081/swagger-ui/index.html`

La documentation interactive permet de :
- Visualiser tous les endpoints disponibles
- Lire la description détaillée de chaque opération
- Tester les requêtes directement depuis le navigateur
- Voir les exemples de requêtes et de réponses
- Connaître tous les codes HTTP retournés

---

## ️ Console H2 (base de données)

URL : `http://localhost:8081/h2-console`

| Champ    | Valeur                  |
|----------|-------------------------|
| JDBC URL | `jdbc:h2:mem:tododb`    |
| Username | `sa`                    |
| Password | *(laisser vide)*        |

> La base H2 est en mémoire. Les données sont perdues à chaque redémarrage.

---

## Endpoints disponibles

| Méthode    | URL                        | Description                        |
|------------|----------------------------|------------------------------------|
| `POST`     | `/api/tasks`               | Créer une nouvelle tâche           |
| `GET`      | `/api/tasks`               | Lister toutes les tâches           |
| `GET`      | `/api/tasks?status=TODO`   | Filtrer les tâches par statut      |
| `GET`      | `/api/tasks/{id}`          | Récupérer une tâche par son ID     |
| `PUT`      | `/api/tasks/{id}`          | Mettre à jour une tâche existante  |
| `DELETE`   | `/api/tasks/{id}`          | Supprimer une tâche                |

---

## Exemples de requêtes

### Créer une tâche

```http
POST /api/tasks
Content-Type: application/json

{
  "title": "Apprendre Spring Boot",
  "description": "Suivre le tutoriel complet sur DDD",
  "status": "TODO"
}
```

**Réponse 201 Created :**

```json
{
  "id": 1,
  "title": "Apprendre Spring Boot",
  "description": "Suivre le tutoriel complet sur DDD",
  "status": "TODO"
}
```

---

### Lister toutes les tâches

```http
GET /api/tasks
```

**Réponse 200 OK :**

```json
[
  {
    "id": 1,
    "title": "Apprendre Spring Boot",
    "description": "Suivre le tutoriel complet sur DDD",
    "status": "TODO"
  },
  {
    "id": 2,
    "title": "Écrire les tests unitaires",
    "description": "Couvrir tous les cas du service",
    "status": "IN_PROGRESS"
  }
]
```

---

### Filtrer par statut

```http
GET /api/tasks?status=IN_PROGRESS
```

---

### Mettre à jour une tâche

```http
PUT /api/tasks/1
Content-Type: application/json

{
  "title": "Apprendre Spring Boot",
  "description": "Tutoriel terminé avec succès !",
  "status": "DONE"
}
```

**Réponse 200 OK :**

```json
{
  "id": 1,
  "title": "Apprendre Spring Boot",
  "description": "Tutoriel terminé avec succès !",
  "status": "DONE"
}
```

---

### Supprimer une tâche

```http
DELETE /api/tasks/1
```

**Réponse 204 No Content**

---

## Statuts disponibles

| Statut        | Description          |
|---------------|----------------------|
| `TODO`        | Tâche à faire        |
| `IN_PROGRESS` | Tâche en cours       |
| `DONE`        | Tâche terminée       |

---

## Lancer les tests

```bash
mvn test -Dtest=TaskServiceTest -DskipTests=false
```

**Résultat attendu :**

### Cas testés

| Test | Description |
|---|---|
| `create_devraitRetournerTaskResponse` | Création d'une tâche |
| `findAll_sansFiltre_devraitRetournerToutesLesTaches` | Liste sans filtre |
| `findAll_avecFiltre_devraitRetournerTachesParStatut` | Liste avec filtre |
| `findById_devraitRetournerTaskResponse` | Récupération par ID |
| `findById_tacheInexistante_devraitLeverException` | ID inexistant |
| `update_devraitModifierEtRetournerTaskResponse` | Mise à jour |
| `delete_devraitAppelerDeleteById` | Suppression |