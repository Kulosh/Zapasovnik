using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Zapasovnik.API.DbContexts;
using Zapasovnik.API.DTOs;
using Zapasovnik.API.Entities;

namespace Zapasovnik.API.Controllers
{
    [Route("Zapasovnik/[controller]")]
    [ApiController]
    public class FavPlayerController : ControllerBase
    {
        public UserFavPlayersDb DbContext { get; set; }
        public List<Player> Players { get; set; }
        public List<TeamPlayer> TeamsPlayers { get; set; }
        public List<Team> Teams { get; set; }
        public List<UserFavPlayer> UsersFavPlayers { get; set; }

        public FavPlayerController()
        {
            DbContext = new();

            UsersFavPlayers = DbContext.UsersFavPlayers.ToList();
            Teams = DbContext.Teams.ToList();
            Players = DbContext.Players.ToList();
            TeamsPlayers = DbContext.TeamsPlayers.ToList();
        }


        [HttpPost]
        public IEnumerable<FavPlayersDto> APIFavPlayers([FromBody] UserDto userId)
        {
            var rows = UsersFavPlayers
                .Where(f => f.UserId == Convert.ToInt32(userId.UserId))
                .Join(DbContext.Players,
                    fav => fav.PlayerId,
                    p => p.PlayerId,
                    (fav, p) => p)
                .Select(p => new FavPlayersDto
                {
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
    }
}
