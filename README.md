****# Zapasovnik

<img src="./source/Zapasovnik.APP/app/src/main/res/drawable/logo.png" height="128" alt="Zapasovnik logo">

---

## Table of contents

- [Table of contents](#table-of-contents)
- [Introduction](#introduction)
  - [About the project](#about-the-project)
  - [What is *Zapasovnik*](#what-is-zapasovnik)
  - [About the code](#about-the-code)
    - [Backend {#intro-back}](#backend-intro-back)
    - [Frontend {#intro-front}](#frontend-intro-front)
- [Code](#code)
  - [Structure](#structure)
    - [Backend {#structure-back}](#backend-structure-back)
    - [Frontend {#structure-front}](#frontend-structure-front)
    - [Database](#database)
  - [Examples](#examples)
    - [Backend {#examples-back}](#backend-examples-back)
      - [DbContext](#dbcontext)
      - [Entities and DTOs](#entities-and-dtos)
      - [Security](#security)
        - [Hashing](#hashing)
        - [JWT](#jwt)
      - [Controllers](#controllers)
    - [Frontend {#examples-front}](#frontend-examples-front)
      - [Activity](#activity)
      - [Network](#network)
      - [ViewModel](#viewmodel)

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

##### DbContext

DbContext consists of the parts.

1. `DbSet<T>` - Table to load
2. `OnConfiguring()` - Connection parameters

In `UsersOnlyDbContext.cs` to load `tbUsers`, we use the following `DbSet<T>` as a class property.

```csharp
public DbSet<User> Users { get; set; }
```

Because connection string is stored either in `UserSecrets` or `EnvironmentalVariable`, we use the following lookup for this parameter.

```csharp
protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
{
    string ?connection;

    if (Environment.GetEnvironmentVariable("ConnectionString") == null)
    {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        IConfiguration configuration = configurationBuilder
            .AddUserSecrets<Program>()
            .Build();
        connection = configuration.GetSection("Zapasovnik")["ConnectionString"];
    }
    else
    {
        connection = Environment.GetEnvironmentVariable("ConnectionString");
    }

    optionsBuilder.UseMySQL(connection!);
}
```

---

##### Entities and DTOs

Entities and DTOs share similar structure, except from Entities having attributes used for data operations with DB.

Here is an example of a Entity for `tbTeamsPlayers`, which handles M:N relations.

```csharp
[Table("tbTeamsPlayers")]    // Name of the table in DB
[PrimaryKey(nameof(TeamId), nameof(PlayerId))]     // Combined PK
public class TeamPlayer
{
    [Column("teams_players_id")]    // Name of the column in DB
    public int TeamsPlayersId { get; set; }

    [Column("FK_team_id")]
    public int TeamId { get; set; }

    [Column("FK_player_id")]
    public int PlayerId { get; set; }

    [Column("since")]
    public DateTime Since { get; set; } = DateTime.Now;    // Setting default value
}
```

---

##### Security

###### Hashing

Hashing is done using `SHA256` like this.

```csharp
public class PasswordHelper
{
    public static string HashPassword(string password)
    {
        using (SHA256 sha256 = SHA256.Create())
        {
            byte[] bytes = sha256.ComputeHash(Encoding.UTF8.GetBytes(password));

            // convert byte[] to string (hex)
            StringBuilder builder = new StringBuilder();

            foreach (byte b in bytes)
            {
                builder.Append(b.ToString("x2"));
            }

            return builder.ToString();
        }
    }
}
```

---

###### JWT

JWT token generation is done with support of two classes.

1. `JwtSecret.cs` - Loads and returns secret
2. `JwtTokenGen.cs` - Generates and returns the token

`JwtSecret` contains the same loading mechanism as `DbContext`, which looks for `UserSecrets` or `EnvironmentalVariable`.

`JwtTokenGen` is based on documentation found on internet, except claims, which contain "custom" parameters, such as "role" and "uid".

```csharp
var claims = new List<Claim>
{
    new Claim(JwtRegisteredClaimNames.Sub, username),
    new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
    new Claim(JwtRegisteredClaimNames.Email, email),
    new Claim("role", admin.ToString()),
    new Claim("uid", id.ToString())
};
```

---

##### Controllers

Each controller has a appropriate DbContext as a class property, which is then initiated and set in the controller constructor.

Data loading is left to controller actions, which loads only the necessary data.

Example of `MatchesController.cs`

```csharp
[Route("Zapasovnik")]
[ApiController]
public class MatchesController : ControllerBase
{
  public MatchesDb DbContext { get; set; }
  
  public MatchesController()
  {
      DbContext = new();
  }

  // ------------------------------------
  // GET requests
  // ------------------------------------

  // If this method was only for logged in users,
  // i.e. users with JWT, there would be [Authorize]
  // as following
    
  // [Authorize]
  [HttpGet("TeamMatches")]
  public List<MatchesListDto> APITeamMatches()
  {
    List<Match> matches = DbContext.Matches.ToList();
    List<TeamMatch> teamMatches = DbContext.TeamsMatches.ToList();
    List<Team> teams = DbContext.Teams.ToList();

    List<MatchesListDto> resp = matches
      .Select(m => new MatchesListDto
      {
        MatchDate = m.MatchDate,

        MatchId = m.MatchId,

        Team1 = teamMatches
          .Where(tm => tm.MatchId == m.MatchId)
          .Join(teams,
            tm => tm.TeamId,
            t => t.TeamId,
            (tm, t) => t.TeamName)
          .OrderBy(name => name)
          .First(),

        Team2 = teamMatches
          .Where(tm => tm.MatchId == m.MatchId)
          .Join(teams,
            tm => tm.TeamId,
            t => t.TeamId,
            (tm, t) => t.TeamName)
          .OrderBy(name => name)
          .Skip(1)
          .First()
      })
      .OrderBy(x => x.MatchDate)
      .ToList();

    return resp;
  }
}
```

#### Frontend {#examples-front}

##### Activity

All logic and interactions within the layouts are handled by classes in `activity/`.

For interactions like `click` there are event listeners on the intended elements and looks like this:

```kotlin
teamsButton.setOnClickListener {
  val intent = Intent(this, TeamsActivity::class.java)
  startActivity(intent)
}
```

You may see that perhaps all event listeners have the 2 lines of code in them as they are in the example. Their intent (:D) is to find activity class, which has at its beginning the intended layout, and start it.

---

When it comes to getting data from local storage or API, we need to use something called `lifecycleScope.launch`. This allows us to work with asynchronous operations. It can be called on creation of the activity or on `click` or any other moment you need.

Here is an example of `lifecycleScope.launch` use-case in `HomeActivity.kt` to load matches from API.

```kotlin
lifecycleScope.launch {
  val teamMatches: List<Match> = RetrofitClient.api.getTeamMatches() // Call to API

  recyclerView.adapter = HomeMatchTableAdapter(teamMatches) { match ->
    val intent = Intent(this@HomeActivity, MatchDetailActivity::class.java)
    intent.putExtra("id", match.Id)
    startActivity(intent)
  }
}
```

---

##### Network

`network/` stores files used for communication with API. `RetrofitClient.kt` , which uses Retrofit to send/get data from API based on given parameters.

```kotlin
object RetrofitClient {
  private const val BASE_URL = "https://api.kulosh.eu/zapasovnik/"

  val api: Api by lazy {
    Retrofit
      .Builder()
      .baseUrl(BASE_URL) // Base URL of API
      .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())) // Tells API that transferred data are JSON
      .build()
      .create(Api::class.java)
  }
}
```

Secondary there is an interface file `Api.kt`. This file contains all API actions or targets to which requests are sent. It also contains parameters like `@Body`, `@Header` if JWT is needed, `@Path` if `id` is being sent or return data type.

The API target could look like this

```kotlin
interface Api {
  @GET("LeagueDetail/{id}")
  suspend fun getLeagueDetail(
    @Path("id") id: Int,
    @Header("Authorization") authorization: String
  ): Response<League>

...

}
```

---

##### ViewModel

`viewModel/` contains logic used by `recyclerView` to fill columns in tables.

```kotlin
class HomeMatchTableAdapter(
  // Data source
  private val matches: List<Match>,
  // Sends match which was clicked on
  private val onMatchClick: (Match) -> Unit
) : RecyclerView.Adapter<HomeMatchTableAdapter.HomeMatchTableHolder>() {

  // Choose all columns
  class HomeMatchTableHolder(view: View): RecyclerView.ViewHolder(view) {
    val team1: TextView = view.findViewById(R.id.Team1)
    val date: TextView = view.findViewById(R.id.Date)
    val team2: TextView = view.findViewById(R.id.Team2)
    val row: LinearLayout = view.findViewById(R.id.matchRow)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMatchTableHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.matches_table_row, parent, false)
    return HomeMatchTableHolder(view)
  }

  // Fills data into the table
  override fun onBindViewHolder(holder: HomeMatchTableHolder, position: Int) {
    val match = matches[position]
    holder.team1.text = match.Team1
    holder.date.text = match.Date
    holder.team2.text = match.Team2
    holder.row.setOnClickListener {
      onMatchClick(match)
    }
  }

  override fun getItemCount() = matches.size
}
```
