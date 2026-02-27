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
        public List<Team> Teams { get; set; }
        public List<UserFavTeam> UserFavTeams { get; set; }
        public List<TeamMatch> TeamMatches { get; set; }
        public List<TeamPlayer> TeamPlayers { get; set; }
        public List<Match> Matches { get; set; }

        public TeamController()
        {
            DbContext = new();
            Teams = DbContext.Teams.ToList();
        }

        [HttpGet("Teams")]
        public List<TeamsDto> APITeams()
        {
            List<TeamsDto> teams = Teams
                .Select(t => new TeamsDto
                {
                    TeamId = t.TeamId,
                    TeamName = t.TeamName
                })
                .ToList();

            return teams;
        }

        [HttpPost("TeamDetail")]
        public TeamDetail APITeamDetail([FromBody] FavDto user)
        {
            
            //Team team = Teams.Where(t => t.TeamId == id).FirstOrDefault();
            
            TeamDetail team = Teams
                .Where(t => t.TeamId == user.EntityId)
                .Select(t => new TeamDetail
                {
                    TeamId = t.TeamId,
                    TeamName = t.TeamName,
                    TeamEstablished = t.TeamEstablished,
                    isFavorite = false
                })
                .First();

            team.isFavorite = UserFavTeams.Where(uft => uft.TeamId == user.EntityId && uft.UserId == user.UserId).FirstOrDefault() != null ? true : false ;

            return team;
        }

        [HttpPost("AddTeam")]
        public bool APIAddTeam([FromBody] AddTeamDto team)
        {
            try
            {
                Team newTeam = new Team();

                newTeam.TeamName = team.TeamName;
                newTeam.TeamEstablished = Convert.ToDateTime(team.TeamEstablished);

                Teams.Add(newTeam);
                DbContext.Teams.Add(newTeam);
                DbContext.SaveChanges();
                return true;
            } catch
            {
                return false;
            }
        }

        [HttpDelete("DeleteTeam/{id}")]
        public bool APIDeleteTeam(int id)
        {
            TeamMatches = DbContext.TeamMatches.ToList();
            TeamPlayers = DbContext.TeamPlayers.ToList();
            UserFavTeams = DbContext.UserFavTeams.ToList();
            Matches = DbContext.Matches.ToList();

            try
            {
                List<UserFavTeam> favTeams = UserFavTeams.FindAll(t => t.TeamId == id);
                List<int> matchIds = TeamMatches
                        .Where(tm => tm.TeamId == id)
                        .Select(tm => tm.MatchId)
                        .ToList();
                List<TeamPlayer> teamPlayers = TeamPlayers.FindAll(t => t.TeamId == id);
                Team team = Teams.Where(t => t.TeamId == id).FirstOrDefault();

                if (favTeams.Count > 0 || favTeams != null)
                { 
                    DbContext.UserFavTeams.RemoveRange(favTeams);
                    DbContext.SaveChanges();
                }

                if (matchIds.Count > 0 || matchIds != null)
                {

                    foreach (int i in matchIds)
                    {
                        List<TeamMatch> tm = TeamMatches
                            .Where(tm => tm.MatchId == i)
                            .ToList();
                        DbContext.TeamMatches.RemoveRange(tm);
                        DbContext.SaveChanges();
                        Match m = Matches.Where(m => m.MatchId == i).FirstOrDefault();
                        DbContext.Matches.Remove(m);
                        DbContext.SaveChanges();
                    }
                }

                if (teamPlayers.Count > 0 || teamPlayers != null)
                {
                    DbContext.TeamPlayers.RemoveRange(teamPlayers);
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

        [HttpPost("FavTeams")]
        public List<TeamsDto> APIFavTeams([FromBody] UserDto userId)
        {
            UserFavTeams = DbContext.UserFavTeams.ToList();

            List<TeamsDto> favTeams = UserFavTeams
                .Where(t => t.TeamId == userId.UserId)
                .Join(DbContext.Teams,
                    fav => fav.TeamId,
                    t => t.TeamId,
                    (fav,t) => t)
                .Select(t => new TeamsDto
                {
                    TeamName = t.TeamName,
                    TeamId = t.TeamId
                })
                .ToList();

            return favTeams;
        }
    }
}
