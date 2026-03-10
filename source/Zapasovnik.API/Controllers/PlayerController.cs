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
        public PlayersDb DbContext { get; set; }

        public PlayerController()
        {
            DbContext = new();
        }

        // ------------------------------------
        // GET requests
        // ------------------------------------

        [HttpGet("Players")]
        public List<PlayersListDto> APIPlayers()
        {
            List<Player> players = DbContext.Players.ToList();
            List<TeamPlayer> teamPlayers = DbContext.TeamsPlayers.ToList();
            List<Team> teams = DbContext.Teams.ToList();

            List<PlayersListDto> resp = players
                .Select(p => new PlayersListDto
                {
                    Id = p.PlayerId,
                    FName = p.FirstName,
                    LName = p.LastName,

                    Team = teamPlayers
                        .Where(tp => tp.PlayerId == p.PlayerId)
                        .Join(teams,
                              tp => tp.TeamId,
                              t => t.TeamId,
                              (tp, t) => t.TeamName)
                        .OrderBy(name => name)
                        .FirstOrDefault() ?? "No team",
                })
                .OrderBy(p => p.Id)
                .ThenBy(p => p.FName)
                .ThenBy(p => p.LName)
                .ToList();

            return resp;
        }

        // ------------------------------------
        // POST requests
        // ------------------------------------

        [HttpPost("PlayerDetail")]
        public PlayerDetailDto APIPlayerDetail([FromBody] FavDto user)
        {
            List<Player> players = DbContext.Players.ToList();
            List<TeamPlayer> teamPlayers = DbContext.TeamsPlayers.ToList();
            List<Team> teams = DbContext.Teams.ToList();
            List<UserFavPlayer> userFavPlayers = DbContext.UsersFavPlayers.ToList();

            Player player = players
                .First(p => p.PlayerId == user.EntityId);

            string? team = teamPlayers
                .Where(tp => tp.PlayerId == player.PlayerId)
                .Join(teams,
                      tp => tp.TeamId,
                      t => t.TeamId,
                      (tp, t) => t.TeamName)
                .FirstOrDefault();

            PlayerDetailDto resp = new()
            {
                Id = player.PlayerId,
                Fname = player.FirstName,
                Lname = player.LastName,
                Birth = $"{player.PlayerBorn}",
                Team = team ?? "No team",
                IsFavorite = userFavPlayers
                    .FirstOrDefault(ufp => ufp.PlayerId == user.EntityId && ufp.UserId == user.UserId) != null ? true : false
            };

            return resp;
        }

        [Authorize(Roles = "True")]
        [HttpPost("AddPlayer")]
        public bool AddPlayer([FromBody] PlayerDto newObject)
        {
            try
            {
                Player player = new()
                {
                    FirstName = newObject.FName,
                    LastName = newObject.LName,
                    PlayerBorn = Convert.ToDateTime(newObject.Birth)
                };
                DbContext.Players.Add(player);
                DbContext.SaveChanges();
                List<Player> players = DbContext.Players.ToList();

                if (newObject.Team == "") return true;

                int pId = DbContext.Players
                    .Last()
                    .PlayerId;
                int tId = DbContext.Teams
                    .Where(t => t.TeamName == newObject.Team)
                    .First()
                    .TeamId;

                TeamPlayer teamPlayer = new() { PlayerId = pId, TeamId = tId };
                DbContext.TeamsPlayers.Add(teamPlayer);
                DbContext.SaveChanges();

                return true;
            }
            catch
            {
                return false;
            }
        }

        [Authorize(Roles = "False")]
        [HttpPost("FavPlayer")]
        public List<PlayersListDto> APIFavPlayers([FromBody] int userId)
        {
            List<UserFavPlayer> userFavPlayers = DbContext.UsersFavPlayers.ToList();

            List<PlayersListDto> resp = userFavPlayers
                .Where(f => f.UserId == Convert.ToInt32(userId))
                .Join(DbContext.Players,
                    fav => fav.PlayerId,
                    p => p.PlayerId,
                    (fav, p) => p)
                .Select(p => new PlayersListDto
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
                .OrderBy(p => p.Id)
                .ThenBy(p => p.FName)
                .ThenBy(p => p.LName)
                .ToList();

            return resp;
        }

        [Authorize(Roles = "False")]
        [HttpPost("AddFavPlayer")]
        public bool APIAddFavPlayer([FromBody] FavDto newFav)
        {
            try
            {
                UserFavPlayer userFavPlayer = new()
                {
                    PlayerId = newFav.EntityId,
                    UserId = newFav.UserId,
                };

                DbContext.UsersFavPlayers.Add(userFavPlayer);
                DbContext.SaveChanges();
                return true;
            }
            catch
            {
                return false;
            }
        }


        [Authorize(Roles = "False")]
        [HttpPost("DeleteFavPlayer")]
        public bool APIDelFavPlayer([FromBody] UserFavPlayer delFavPlayer)
        {
            try
            {
                DbContext.UsersFavPlayers.Remove(delFavPlayer);
                DbContext.SaveChanges();
                return true;
            }
            catch
            {
                return false;
            }
        }

        // ------------------------------------
        // PATCH requests
        // ------------------------------------

        [Authorize(Roles = "True")]
        [HttpPatch("EditPlayer/{id}")]
        public bool APIEditPlayer(int id, [FromBody] PlayerDto editedObject)
        {
            try
            {
                List<Player> players = DbContext.Players.ToList();
                List<TeamPlayer> teamPlayers = DbContext.TeamsPlayers.ToList();
                List<Team> teams = DbContext.Teams.ToList();

                Player oldPlayer = players
                    .Where(p => p.PlayerId == id)
                    .First();

                Team? oldTeam = teams
                    .Where(t => t.TeamName == editedObject.Team)
                    .FirstOrDefault();

                TeamPlayer? oldTeamPlayer = teamPlayers
                    .Where(tp => tp.PlayerId == id && tp.TeamId == oldTeam?.TeamId)
                    .FirstOrDefault();

                if (oldPlayer.FirstName == editedObject.FName &&
                    oldPlayer.LastName == editedObject.LName &&
                    oldPlayer.PlayerBorn == DateTime.Parse(editedObject.Birth) &&
                    oldTeam?.TeamName == editedObject.Team
                ) return true;

                if (oldPlayer.FirstName != editedObject.FName ||
                    oldPlayer.LastName != editedObject.LName ||
                    oldPlayer.PlayerBorn != DateTime.Parse(editedObject.Birth)
                )
                {
                    oldPlayer.FirstName = editedObject.FName;
                    oldPlayer.LastName = editedObject.LName;
                    oldPlayer.PlayerBorn = DateTime.Parse(editedObject.Birth);

                    DbContext.Players.Update(oldPlayer);
                }

                if (oldTeam?.TeamName != editedObject.Team)
                {
                    if (oldTeamPlayer != null) DbContext.TeamsPlayers.Remove(oldTeamPlayer);

                    if (editedObject.Team != "")
                    {
                        TeamPlayer newTeamPlayer = new()
                        {
                            PlayerId = id,
                            TeamId = teams
                                .Where(t => t.TeamName == editedObject.Team)
                                .First()
                                .TeamId
                        };

                        DbContext.TeamsPlayers.Add(newTeamPlayer);
                    }
                }

                DbContext.SaveChanges();

                return true;
            }
            catch
            {
                return false;
            }
        }

        // ------------------------------------
        // DELETE requests
        // ------------------------------------

        [Authorize(Roles = "True")]
        [HttpDelete("DeletePlayer/{id}")]
        public bool APIDeletePlayer(int id)
        {
            try
            {
                List<UserFavPlayer> userFavPlayers = DbContext.UsersFavPlayers.ToList();
                List<TeamPlayer> teamPlayers = DbContext.TeamsPlayers.ToList();
                List<Player> players = DbContext.Players.ToList();

                if (userFavPlayers.Count > 0)
                {
                    DbContext.UsersFavPlayers.RemoveRange(userFavPlayers.Where(ufp => ufp.PlayerId == id));
                    DbContext.SaveChanges();
                }

                TeamPlayer? teamPlayer = teamPlayers
                    .Where(tp => tp.PlayerId == id)
                    .FirstOrDefault();

                if (teamPlayer != null)
                {
                    DbContext.TeamsPlayers.Remove(teamPlayer);
                    DbContext.SaveChanges();
                }

                Player player = players.Where(p => p.PlayerId == id).First();
                DbContext.Players.Remove(player);
                DbContext.SaveChanges();

                return true;
            } catch
            {
                return false;
            }
        }
    }
}
