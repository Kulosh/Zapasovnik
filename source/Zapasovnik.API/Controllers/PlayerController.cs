using Microsoft.AspNetCore.Authorization;
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

        [Authorize(Roles = "False")]
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

        [HttpPost("PlayerDetail")]
        public PlayerDetailDto APIPlayerDetail([FromBody] FavDto user)
        {
            var player = Players.FirstOrDefault(p => p.PlayerId == user.EntityId);
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
                Team = team,
                IsFavorite = UsersFavPlayers.Where(ufp => ufp.PlayerId == user.EntityId && ufp.UserId == user.UserId).FirstOrDefault() != null ? true : false
            };
        }

        [Authorize(Roles = "True")]
        [HttpDelete("DeletePlayer/{id}")]
        public bool APIDeletePlayer(int id)
        {
            try
            {
                var favPlayers = UsersFavPlayers.Where(ufp => ufp.PlayerId == id).ToList();

                if (favPlayers.Count > 0)
                {
                    UsersFavPlayers.RemoveAll(ufp => ufp.PlayerId == id);
                    DbContext.UsersFavPlayers.RemoveRange(DbContext.UsersFavPlayers.Where(ufp => ufp.PlayerId == id));
                    DbContext.SaveChanges();
                }

                TeamPlayer tp = TeamsPlayers.Where(tp => tp.PlayerId == id).FirstOrDefault();

                if (tp != null)
                {
                    DbContext.TeamsPlayers.Remove(tp);
                    DbContext.SaveChanges();
                }

                Player p = Players.Where(p => p.PlayerId == id).First();
                DbContext.Players.Remove(p);
                DbContext.SaveChanges();

                return true;
            } catch
            {
                return false;
            }
        }

        [Authorize(Roles = "False")]
        [HttpPost("AddFavPlayer")]
        public bool APIAddFavPlayer([FromBody] UserFavPlayer newFavPlayer)
        {
            try
            {
                DbContext.UsersFavPlayers.Add(newFavPlayer);
                DbContext.SaveChanges();
                return true;
            } catch
            {
                return false;
            }
        }
    }
}
