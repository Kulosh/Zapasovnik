using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
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

        [HttpGet(Name = "GetLeagues")]
        public IEnumerable<League> GetLeagues()
        {
            return Leagues.ToArray();
        }

        [HttpPost(Name = "PostLeague")]
        public IEnumerable<League> PostLeague(string leagueName)
        {
            League newLeague = new League { LeagueName = leagueName };
            DbContext.Leagues.Add(newLeague);
            DbContext.SaveChanges();
            Leagues = DbContext.Leagues.ToList();
            return Leagues.ToArray();
        }
    }
}
