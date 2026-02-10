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
    public class TeamMatchesController : ControllerBase
    {
        public TeamMatchesDb DbContext { get; set; }
        public List<Team> Teams { get; set; }
        public List<TeamMatch> TeamsMatches { get; set; }
        public List<Match> Matches { get; set; }

        public TeamMatchesController()
        {
            DbContext = new();

            Teams = DbContext.Teams.ToList();
            TeamsMatches = DbContext.TeamsMatches.ToList();
            Matches = DbContext.Matches.ToList();
        }




        [HttpGet]
        public List<MatchWithTeamsDto> APITeamMatches()
        {
            List<MatchWithTeamsDto> rows = Matches
                .Select(m => new MatchWithTeamsDto
                {
                    MatchDate = m.MatchDate,

                    Team1 = TeamsMatches
                        .Where(tm => tm.MatchId == m.MatchId)
                        .Join(Teams,
                                tm => tm.TeamId,
                                t => t.TeamId,
                                (tm, t) => t.TeamName)
                        .OrderBy(name => name)
                        .FirstOrDefault(),

                    Team2 = TeamsMatches
                        .Where(tm => tm.MatchId == m.MatchId)
                        .Join(Teams,
                                tm => tm.TeamId,
                                t => t.TeamId,
                                (tm, t) => t.TeamName)
                        .OrderBy(name => name)
                        .Skip(1)
                        .FirstOrDefault()
                })
                .OrderBy(x => x.MatchDate)
                .ToList();

            return rows;
        }
    }
}
