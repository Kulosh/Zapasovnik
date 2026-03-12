# Zapasovnik

<img src="./source/Zapasovnik.APP/app/src/main/res/drawable/logo.png" height="128">

---

## Table of contents

- [Zapasovnik](#zapasovnik)
  - [Table of contents](#table-of-contents)
  - [Introduction](#introduction)
    - [About the project](#about-the-project)
    - [What is *Zapasovnik*](#what-is-zapasovnik)
    - [About the code](#about-the-code)
      - [Backend](#backend)
      - [Frontend](#frontend)
      - [Database](#database)

---

## Introduction

### About the project

*Zapasovnik* is a student project focused on improving **C#** and **ASP.NET** skills and get to know a new programming language, in this case **Kotlin**.

### What is *Zapasovnik*

*Zapasovnik* is an app, which delivers info about football players, matches and teams. With the ability to create user account, you can even add players, matches and teams to your favorites and keep closer eye on them.

---

### About the code

App is divided into 2 parts: backend (`Zapasovnik.API`) and frontend (`Zapasovnik.APP`). Also, there is a database running on `MariaDB`.

Structure of the code is explained [here]()

*Note: App is running on .NET 8*

#### Backend

Backend is build on **ASP.NET Core** with following nuggets added:

- `Microsoft.EntityFrameworkCore` - 8.0.22
- `Microsoft.AspNetCore.Authentication.JwtBearer` - 8.0.24
- `MySql.EntityFrameworkCore` - 8.0.20

#### Frontend

There were 2 languages to choose from when it came to Android app development. First was the OG **Java** and second was **Kotlin**.

Android Devs recommend **Kotlin** for new development, because it takes care of the well-known and frequent errors contained in **Java**.

Now, *Android studio*, recommended IDE for native Android app development, has the ability to automatically add package dependencies but we still manually added the following dependencies (mainly due to versions) into `Zapasovnik.APP/app/build.gradle.kts`.

- `com.squareup.retrofit2:retrofit:3.0.0`
- `com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0`
- `com.squareup.okhttp3:okhttp:4.11.0`

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

---
