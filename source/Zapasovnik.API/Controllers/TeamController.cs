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
                return true;
            } catch
            {
                return false;
            }

        }
    }
}
