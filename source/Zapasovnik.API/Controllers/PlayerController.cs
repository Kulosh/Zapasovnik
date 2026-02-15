using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Zapasovnik.API.DbContexts;
using Zapasovnik.API.DTOs;
using Zapasovnik.API.Entities;

namespace Zapasovnik.API.Controllers
{
    [Route("Zapasovnik")]
    [ApiController]
    public class PlayerController : ControllerBase
    {
        public UserFavPlayersDb DbContext { get; set; }
        public List<Player> Players { get; set; }
        public List<TeamPlayer> TeamsPlayers { get; set; }
        public List<Team> Teams { get; set; }
        public List<UserFavPlayer> UsersFavPlayers { get; set; }

        public PlayerController()
        {
            DbContext = new();

            UsersFavPlayers = DbContext.UsersFavPlayers.ToList();
            Teams = DbContext.Teams.ToList();
            Players = DbContext.Players.ToList();
            TeamsPlayers = DbContext.TeamsPlayers.ToList();
        }

        [HttpPost("FavPlayer")]
        public List<FavPlayersDto> APIFavPlayers([FromBody] UserDto userId)
        {
            List<FavPlayersDto> rows = UsersFavPlayers
                .Where(f => f.UserId == Convert.ToInt32(userId.UserId))
                .Join(DbContext.Players,
                    fav => fav.PlayerId,
                    p => p.PlayerId,
                    (fav, p) => p)
                .Select(p => new FavPlayersDto
                {
                    Id = p.PlayerId,
                    FName = p.FirstName,
                    LName = p.LastName,

                    Team = DbContext.TeamsPlayers
                        .Where(tp => tp.PlayerId == p.PlayerId)
                        .Join(DbContext.Teams,
                            tp => tp.TeamId,
                            t => t.TeamId,
                            (tp, t) => t.TeamName)
                        .OrderBy(name => name)
                        .FirstOrDefault()!
                })
                .ToList();

            return rows;
        }

        [HttpGet("Players")]
        public IEnumerable<FavPlayersDto> APIPlayers()
        {
            var rows = Players
                .Select(p => new FavPlayersDto
                {
                    Id = p.PlayerId,
                    FName = p.FirstName,
                    LName = p.LastName,

                    Team = TeamsPlayers
                        .Where(tp => tp.PlayerId == p.PlayerId)
                        .Join(Teams,
                              tp => tp.TeamId,
                              t => t.TeamId,
                              (tp, t) => t.TeamName)
                        .OrderBy(name => name)
                        .FirstOrDefault()!,
                })
                .OrderBy(x => x.LName)
                .ThenBy(x => x.FName);

            return rows;
        }

        [HttpGet("Players/{id}")]
        public PlayerDetailDto APIPlayerDetail(int id)
        {
            var player = Players.FirstOrDefault(p => p.PlayerId == id);
            if (player == null)
            {
                return null!;
            }
            var team = TeamsPlayers
                .Where(tp => tp.PlayerId == player.PlayerId)
                .Join(Teams,
                      tp => tp.TeamId,
                      t => t.TeamId,
                      (tp, t) => t.TeamName)
                .OrderBy(name => name)
                .FirstOrDefault()!;
            return new PlayerDetailDto
            {
                Id = player.PlayerId,
                Fname = player.FirstName,
                Lname = player.LastName,
                Birth = Convert.ToString(player.PlayerBorn),
                Team = team
            };
        }
    }
}
