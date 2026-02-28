using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Zapasovnik.API.DbContexts;
using Zapasovnik.API.DTOs;
using Zapasovnik.API.Entities;

namespace Zapasovnik.API.Controllers
{
    [Route("Zapasovnik")]
    [ApiController]
    public class AddPlayerController : ControllerBase
    {
        public AddPlayerDb DbContext { get; set; }
        public List<Team> Teams { get; set; }
        public List<Player> Players { get; set; }
        public List<TeamPlayer> TeamPlayers { get; set; }

        public AddPlayerController()
        {
            DbContext = new();

            Teams = DbContext.Teams.ToList();
            Players = DbContext.Players.ToList();
            TeamPlayers = DbContext.TeamPlayers.ToList();
        }

        [HttpPost("AddPlayer")]
        public bool AddPlayer([FromBody] AddPlayerDto newPlayer)
        {
            try
            {
                Player player = new()
                {
                    FirstName = newPlayer.FName,
                    LastName = newPlayer.LName,
                    PlayerBorn = Convert.ToDateTime(newPlayer.Birth)
                };
                DbContext.Players.Add(player);
                DbContext.SaveChanges();
                Players = DbContext.Players.ToList();

                if (newPlayer.Team == "") return true;

                Player pId = Players
                    .Where(p => p.FirstName == player.FirstName && p.LastName == player.LastName && p.PlayerBorn == Convert.ToDateTime(player.PlayerBorn))
                    .First();

                Team team = Teams.Where(t => t.TeamName == newPlayer.Team).First();

                TeamPlayer teamPlayer = new() { PlayerId = pId.PlayerId, TeamId = team.TeamId, };
                DbContext.TeamPlayers.Add(teamPlayer);
                DbContext.SaveChanges();

                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return false;
            }
        }

        [HttpPatch("EditPlayer/{id}")]
        public bool APIEditPlayer(int id, [FromBody] AddPlayerDto newPlayer)
        {
            try
            {
                Player player = Players.Where(p => p.PlayerId == id).First();

                TeamPlayer oldTP = TeamPlayers.Where(tp => tp.PlayerId == id).First();
                DbContext.TeamPlayers.Remove(oldTP);
                DbContext.SaveChanges();

                player.FirstName = newPlayer.FName;
                player.LastName = newPlayer.LName;
                player.PlayerBorn = Convert.ToDateTime(newPlayer.Birth);

                DbContext.Players.Update(player);
                DbContext.SaveChanges();
                Players = DbContext.Players.ToList();

                if (newPlayer.Team == "") return true;

                Team team = Teams.Where(t => t.TeamName == newPlayer.Team).First();

                TeamPlayer teamPlayer = new() { PlayerId = id, TeamId = team.TeamId, };
                DbContext.TeamPlayers.Add(teamPlayer);
                DbContext.SaveChanges();

                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return false;
            }
        }
    }
}
