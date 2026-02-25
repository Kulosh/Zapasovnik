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

        public TeamController()
        {
            DbContext = new();
            Teams = DbContext.Teams.ToList();
            TeamMatches = DbContext.TeamMatches.ToList();
            TeamPlayers = DbContext.TeamPlayers.ToList();
            UserFavTeams = DbContext.UserFavTeams.ToList();
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

        [HttpGet("TeamDetail/{id}")]
        public Team APITeamDetail(int id)
        {
            Team team = Teams.Where(t => t.TeamId == id).FirstOrDefault();

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
            try
            {
                List<UserFavTeam> favTeams = UserFavTeams.FindAll(t => t.TeamId == id);
                List<TeamMatch> teamMatches = TeamMatches.FindAll(t => t.TeamId == id);
                List<TeamPlayer> teamPlayers = TeamPlayers.FindAll(t => t.TeamId == id);
                Team team = Teams.Where(t => t.TeamId == id).FirstOrDefault();

                if (favTeams.Count > 0 || favTeams != null)
                {
                    DbContext.UserFavTeams.RemoveRange(favTeams);
                    DbContext.SaveChanges();
                }

                if (teamMatches.Count > 0 || teamMatches != null)
                {
                    DbContext.TeamMatches.RemoveRange(teamMatches);
                    DbContext.SaveChanges();
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
    }
}
