using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Zapasovnik.API.DbContexts;
using Zapasovnik.API.DTOs;
using Zapasovnik.API.Entities;

namespace Zapasovnik.API.Controllers
{
    [Route("Zapasovnik")]
    [ApiController]
    public class TeamController : ControllerBase
    {
        public TeamsDbContext DbContext { get; set; }

        public TeamController()
        {
            DbContext = new();
        }

        [HttpGet("Teams")]
        public List<TeamsListDto> APITeams()
        {
            List<Team> teams = DbContext.Teams.ToList();

            List<TeamsListDto> resp = teams
                .Select(t => new TeamsListDto
                {
                    TeamId = t.TeamId,
                    TeamName = t.TeamName
                })
                .ToList();

            return resp;
        }

        [HttpPost("TeamDetail")]
        public TeamDetail APITeamDetail([FromBody] FavDto user)
        {
            List<UserFavTeam> userFavTeams = DbContext.UserFavTeams.ToList();
            List<Team> teams = DbContext.Teams.ToList();

            TeamDetail resp = teams
                .Where(t => t.TeamId == user.EntityId)
                .Select(t => new TeamDetail
                {
                    TeamId = t.TeamId,
                    TeamName = t.TeamName,
                    TeamEstablished = t.TeamEstablished,
                    isFavorite = userFavTeams
                                    .FirstOrDefault(uft => uft.TeamId == user.EntityId && uft.UserId == user.UserId) != null
                })
                .First();

            return resp;
        }

        [Authorize(Roles = "True")]
        [HttpPost("AddTeam")]
        public bool APIAddTeam([FromBody] TeamDto team)
        {
            try
            {
                Team newTeam = new()
                {
                    TeamName = team.TeamName,
                    TeamEstablished = Convert.ToDateTime(team.TeamEstablished)
                };

                DbContext.Teams.Add(newTeam);
                DbContext.SaveChanges();

                return true;
            } catch
            {
                return false;
            }
        }

        [Authorize(Roles = "True")]
        [HttpDelete("DeleteTeam/{id}")]
        public bool APIDeleteTeam(int id)
        {
            List<TeamMatch> teamMatches = DbContext.TeamMatches.ToList();
            List<TeamPlayer> teamPlayers = DbContext.TeamPlayers.ToList();
            List<UserFavTeam> userFavTeams = DbContext.UserFavTeams.ToList();
            List<Match> matches = DbContext.Matches.ToList();
            List<Team> teams = DbContext.Teams.ToList();

            try
            {
                List<UserFavTeam> oldFavTeams = userFavTeams
                    .FindAll(t => t.TeamId == id);
                List<TeamMatch> oldTeamMatches = teamMatches
                        .Where(tm => tm.TeamId == id)
                        .ToList();
                List<TeamPlayer> oldTeamPlayers = teamPlayers
                    .FindAll(t => t.TeamId == id);
                Team team = teams
                    .Where(t => t.TeamId == id)
                    .First();

                if (oldFavTeams.Count > 0 || oldFavTeams != null)
                { 
                    DbContext.UserFavTeams.RemoveRange(oldFavTeams);
                    DbContext.SaveChanges();
                }

                if (oldTeamMatches.Count > 0 || oldTeamMatches != null)
                {

                    foreach (TeamMatch teamMatch in oldTeamMatches)
                    {
                        List<TeamMatch> tm = teamMatches
                            .Where(tm => tm.MatchId == teamMatch.MatchId)
                            .ToList();

                        DbContext.TeamMatches.RemoveRange(tm);
                        DbContext.SaveChanges();
                        
                        Match m = matches
                            .Where(m => m.MatchId == teamMatch.MatchId)
                            .First();

                        DbContext.Matches.Remove(m);
                        DbContext.SaveChanges();
                    }
                }

                if (oldTeamPlayers.Count > 0 || oldTeamPlayers != null)
                {
                    DbContext.TeamPlayers.RemoveRange(oldTeamPlayers);
                    DbContext.SaveChanges();
                }

                DbContext.Teams.Remove(team);
                DbContext.SaveChanges();

                return true;
            } catch
            {
                return false; 
            }
        }

        [Authorize(Roles = "False")]
        [HttpPost("AddFavTeam")]
        public bool APIAddFavTeam([FromBody] UserFavTeam team)
        {
            try
            {
                DbContext.UserFavTeams.Add(team);
                DbContext.SaveChanges();

                return true;
            } catch
            {
                return false;
            }
        }

        [Authorize(Roles = "False")]
        [HttpPost("DeleteFavTeam")]
        public bool APIDeleteFavTeam([FromBody] UserFavTeam team)
        {
            try
            {
                DbContext.UserFavTeams.Remove(team);
                DbContext.SaveChanges();

                return true;
            } catch
            {
                return false;
            }
        }

        [Authorize(Roles = "False")]
        [HttpPost("FavTeams")]
        public List<TeamsListDto> APIFavTeams([FromBody] int userId)
        {
            List<UserFavTeam> userFavTeams = DbContext.UserFavTeams.ToList();

            List<TeamsListDto> favTeams = userFavTeams
                .Where(uft => uft.UserId == userId)
                .Join(DbContext.Teams,
                    fav => fav.TeamId,
                    t => t.TeamId,
                    (fav,t) => t)
                .Select(t => new TeamsListDto
                {
                    TeamName = t.TeamName,
                    TeamId = t.TeamId
                })
                .ToList();

            return favTeams;
        }

        [Authorize(Roles = "True")]
        [HttpPatch("EditTeam/{id}")]
        public bool APIEditTeam(int id,[FromBody] TeamDto team)
        {
            try
            {
                List<Team> teams = DbContext.Teams.ToList();

                Team newTeam = teams.Where(t => t.TeamId == id).First();

                newTeam.TeamName = team.TeamName;
                newTeam.TeamEstablished = Convert.ToDateTime(team.TeamEstablished);

                DbContext.Teams.Update(newTeam);
                DbContext.SaveChanges();
                return true;
            }
            catch
            {
                return false;
            }
        }

    }
}
