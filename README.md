# Zapasovnik

<img src="./source/Zapasovnik.APP/app/src/main/res/drawable/logo.png" height="128" alt="Zapasovnik logo">

---

## Table of contents

- [Zapasovnik](#zapasovnik)
  - [Table of contents](#table-of-contents)
  - [Introduction](#introduction)
    - [About the project](#about-the-project)
    - [What is *Zapasovnik*](#what-is-zapasovnik)
    - [About the code](#about-the-code)
      - [Backend](#backend-intro-back)
      - [Frontend](#frontend-intro-front)
  - [Code](#code)
    - [Structure](#structure)
      - [Backend](#backend-structure-back)
      - [Frontend](#frontend-structure-front)
      - [Database](#database)
    - [Examples](#examples)
      - [Backend](#backend-examples-back)

---

## Introduction

### About the project

*Zapasovnik* is a student project focused on improving **C#** and **ASP.NET** skills and get to know a new programming language, in this case **Kotlin**.

### What is *Zapasovnik*

*Zapasovnik* is an app, which delivers info about football players, matches and teams. With the ability to create user account, you can even add players, matches and teams to your favorites and keep closer eye on them.

---

### About the code

App is divided into 2 parts: backend (`Zapasovnik.API`) and frontend (`Zapasovnik.APP`). Also, there is a database running on `MariaDB`.

Structure of the code is explained [here](#structure)

*Note: App is running on .NET 8*

#### Backend {#intro-back}

Backend is build on **ASP.NET Core** with following nuggets added:

- `Microsoft.EntityFrameworkCore` - 8.0.22
- `Microsoft.AspNetCore.Authentication.JwtBearer` - 8.0.24
- `MySql.EntityFrameworkCore` - 8.0.20

#### Frontend {#intro-front}

There were 2 languages to choose from when it came to Android app development. First was the OG **Java** and second was **Kotlin**.

Android Devs recommend **Kotlin** for new development, because it takes care of the well-known and frequent errors contained in **Java**.

Now, *Android studio*, recommended IDE for native Android app development, has the ability to automatically add package dependencies but we still manually added the following dependencies (mainly due to versions) into `Zapasovnik.APP/app/build.gradle.kts`.

- `com.squareup.retrofit2:retrofit:3.0.0`
- `com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0`
- `com.squareup.okhttp3:okhttp:4.11.0`

---

## Code

### Structure

#### Backend {#structure-back}

```bash
Zapasovnik.API/
├── Controllers/
├── DbContexts/
├── DTOs/
├── Entities/
└── Security/
```

**`Controllers/`**

No surprise, this directory hold controllers. They are used as a messenger between frontend and database. Controller contains logic to obtain data from DB using LINQ queries in methods, or in this case, actions.

We implemented 7 controllers. 3 of them serves the users and 4 of them handles the data in DB.

The 3 "user-controllers" are following (Name should explain their role)

- `ChgPwdController.cs`
- `LoginController.cs`
- `RegisterController.cs`

The rest of the 7 are

- `LeagueController.cs` (Mainly for admin)
- `MatchesController.cs`
- `PlayerController.cs`
- `TeamController.cs`

---

**`DbContexts/`**

DbContexts are classes containing connection strings to DB and which table to load and work with, when those classes are called.

There are 5 DbContexts

- `LeaguesDbContext.cs`
- `MatchesDbContext.cs`
- `PlayersDbContext.cs`
- `TeamsDbContext.cs`
- `UsersOnlyDbContext.cs`

Each DbContext has its Controller, with `UsersOnlyDbContext.cs` serving the "user-controllers"

---

**`DTOs/`**

DTO (Data Transfer Object) are classes used in API <-> Frontend communication. They are used because they aren't related to `Entities/` which are bound DB tables.

For every "none-user-controller", there is generally 2 DTOs.

1. `[Entities]ListDto.cs` - Data to fill tables/lists
2. `[Entity]Dto.cs` - For creating new objects
3. `[Entity]DetailDto.cs` - Data for detail view

---

**`Entities/`**

This directory stores classes, which are templates for tables in DB. They should be only used with `DbSet<T>` in DbContexts

---

**`Security/`**

Here are stored classes which help to implement JWT and hashing.

- `JwtSecret.cs` - Loads JWT secrete from **User Secrets** or **Environmental Variables**
- `JwtTokenGen.cs` - Generates the intended Json Web Token
- `PasswordHelper.cs` - Hashes the passwords given by user and returns them

---

#### Frontend {#structure-front}

```bash
Zapasovnik.APP/
└── app/
    └── src/
        └── main/
            ├── java/../
            │   ├── activity/
            │   ├── model/
            │   ├── network/
            │   ├── network/
            │   └── viewModel/
            └── res/
                ├── drawable/
                ├── layout/
                ├── values/
                └── values-cs/
```

**`java/../activity/`**

The most important part of the frontend is this directory. Here are stored all classes which handle majority of the application logic together with user-interaction with layouts. They handle communication with API, some type of input validation, dynamic layout changes based on roles, etc.

Each layout has its own `[layout]Activity.kt` kotlin class.

---

**`java/../model/`**

`model` is similar to DTOs in backend. It stores data classes which are used in communication with API in form of `@Body` and `Response<>`. Similarly with DTOs, where for each controller were usually 3 files, here are 2 files, because there is no need for `[Entities]ListDto.cs`

In contrast to backend, there are also classes supporting JWT and storing data persistently in local storage. The files are

(For JWT)

- `JwtResponse.kt`
- `DecodedJwt.kt`

(For data store)

- `UserData.kt`

---

**`java/../network/`**

Key directory for communication with API. It consists of 2 files

1. `Api.kt` - Class storing all API targets with routes, needed parameters and expected response types
2. `RetrofitClient.kt` - Handles the connection with API

---

**`java/../viewModel/`**

Here are stored classes, which handle filling the lists (recyclerViews) in layouts.

---

**`res/`**

This directory stores general resources, like images, layouts, strings (with localization), etc.

---

#### Database

[dbDiagram](https://dbdiagram.io/d/dbZapasovnik-69576c8139fa3db27bf1bf39)

Database contains 10 main tables and 1 table, which is right now not implemented. The tables are following and based on those tables are created entities in backend.

- `tbUsers`
- `tbUsersFavMatches`
- `tbMatches`
- `tbUsersFavTeams`
- `tbTeams`
- `tbUsersFavPlayers`
- `tbPlayers`
- `tbTeamsMatches`
- `tbLeagues`
- `tbTeamsPlayers`

*Unused*

- `tbTeamsPlayersArchive`

Relations, columns and data types are specified in the dbDiagram specified at the beginning of this part

---

### Examples

#### Backend {#examples-back}
