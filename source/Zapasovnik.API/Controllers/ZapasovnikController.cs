using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Reflection.Metadata.Ecma335;
using Zapasovnik.API.DTOs;
using Zapasovnik.API.Entities;

namespace Zapasovnik.API.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class ZapasovnikController : ControllerBase
    {
        public List<League> Leagues { get; set; }
        public List<Match> Matches { get; set; }
        public List<Team> Teams { get; set; }
        public List<TeamMatch> TeamsMatches { get; set; }
        public List<Player> Players { get; set; }
        public List<TeamPlayer> TeamsPlayers { get; set; }
        public List<TeamPlayerArchive> TeamsPlayersArchive { get; set; }
        public List<User> Users { get; set; }
        public List<UserFavPlayer> UsersFavPlayers { get; set; }
        public List<UserFavMatch> UsersFavMatches { get; set; }
        public List<UserFavTeam> UsersFavTeams { get; set; }


        public dbZapasovnikContext DbContext { get; set; }

        public ZapasovnikController()
        {
            DbContext = new();
            Leagues = DbContext.Leagues.ToList();
            Matches = DbContext.Matches.ToList();
            Teams = DbContext.Teams.ToList();
            TeamsMatches = DbContext.TeamsMatches.ToList();
            Players = DbContext.Players.ToList();
            TeamsPlayers = DbContext.TeamsPlayers.ToList();
            TeamsPlayersArchive = DbContext.TeamsPlayersArchive.ToList();
            Users = DbContext.Users.ToList();
            UsersFavPlayers = DbContext.UsersFavPlayers.ToList();
            UsersFavMatches = DbContext.UsersFavMatches.ToList();
            UsersFavTeams = DbContext.UsersFavTeams.ToList();
        }

        [HttpGet("Leagues")]
        public IEnumerable<League> APILeagues()
        {
            return Leagues.ToArray();
        }

        [HttpPost("Leagues")]
        public IEnumerable<League> APILeagues(string leagueName)
        {
            League newLeague = new League { LeagueName = leagueName };
            DbContext.Leagues.Add(newLeague);
            DbContext.SaveChanges();
            Leagues = DbContext.Leagues.ToList();
            return Leagues.ToArray();
        }



        [HttpGet("Matches")]
        public IEnumerable<Match> APIMatches()
        {
            return Matches.ToArray();
        }

        [HttpPost("Matches")]
        public IEnumerable<Match> APIMatches(int leagueId, DateTime matchDate)
        {
            Match newMatch = new Match { MatchDate = matchDate, LeagueId = leagueId };
            DbContext.Matches.Add(newMatch);
            DbContext.SaveChanges();
            Matches = DbContext.Matches.ToList();
            return Matches.ToArray();
        }



        [HttpGet("Players")]
        public IEnumerable<Player> APIPlayers()
        {
            return Players.ToArray();
        }

        [HttpPost("Players")]
        public IEnumerable<Player> APIPlayers(string firstName, string lastName, DateTime playerBorn)
        {
            Player newPlayer = new Player { FirstName = firstName, LastName = lastName, PlayerBorn = playerBorn };
            DbContext.Players.Add(newPlayer);
            DbContext.SaveChanges();
            Players = DbContext.Players.ToList();
            return Players.ToArray();
        }



        [HttpGet("Teams")]
        public IEnumerable<Team> APITeams()
        {
            return Teams.ToArray();
        }

        [HttpPost("Teams")]
        public IEnumerable<Team> APITeams(string teamName, DateTime teamEstablished)
        {
            Team newTeam = new Team { TeamName = teamName, TeamEstablished = teamEstablished };
            DbContext.Teams.Add(newTeam);
            DbContext.SaveChanges();
            Teams = DbContext.Teams.ToList();
            return Teams.ToArray();
        }


        [HttpGet("Users")]
        public IEnumerable<User> APIUsers()
        {
            return Users.ToArray();
        }

        [HttpPost("Users")]
        public IEnumerable<User> APIUsers(string userName, string ?userEmail, string userPassword)
        {
            User newUser = new User { UserName = userName, UserEmail = userEmail, UserPassword = userPassword };
            DbContext.Users.Add(newUser);
            DbContext.SaveChanges();
            Users = DbContext.Users.ToList();
            return Users.ToArray();
        }

        [HttpPost("Login")]
        public bool APILogin([FromBody] User incomeUser)
        {
            var user = Users
                .Where(u => u.UserName == incomeUser.UserName && u.UserPassword == incomeUser.UserPassword)
                .FirstOrDefault();
            return user != null;
        }

        [HttpPost("User")]
        public UserDto APIUser([FromBody] UserDto incomeUser)
        {
            incomeUser.Email = Users
                .Where(u => u.UserName == incomeUser.Username)
                .Select(u => u.UserEmail)
                .First()!;
            return incomeUser;
        }

        [HttpPost("chgpwd")]
        public bool APIChangePassword([FromBody] ChangePasswordDto chg)
        {
            User user = Users
                .Where(u => u.UserName == chg.Username)
                .First();

            if (user.UserPassword != chg.Old) return false;
            else
            {
                user.UserPassword = chg.New;

                DbContext.Users.Update(user);
                DbContext.SaveChanges();
                return true;
            }
        }

        [HttpGet("TeamMatches")]
        public IEnumerable<MatchWithTeamsDto> APITeamMatches()
        {
            var rows = Matches
                .Select(m => new MatchWithTeamsDto
                {
                    MatchDate = m.MatchDate,

                    Team1 = TeamsMatches
                        .Where(tm => tm.MatchId == m.MatchId)
                        .Join(Teams,
                                tm => tm.TeamId,
                                t => t.TeamId,
                                (tm, t) => t.TeamName)
                        .OrderBy(name => name)
                        .FirstOrDefault(),

                    Team2 = TeamsMatches
                        .Where(tm => tm.MatchId == m.MatchId)
                        .Join(Teams,
                                tm => tm.TeamId,
                                t => t.TeamId,
                                (tm, t) => t.TeamName)
                        .OrderBy(name => name)
                        .Skip(1)
                        .FirstOrDefault()
                })
                .OrderBy(x => x.MatchDate);

            return rows;

        }
    }
}
