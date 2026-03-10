using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Zapasovnik.API.DbContexts;
using Zapasovnik.API.DTOs;
using Zapasovnik.API.Entities;

namespace Zapasovnik.API.Controllers
{
    [Authorize(Roles = "True")]
    [ApiController]
    [Route("zapasovnik")]
    public class LeagueController : ControllerBase
    {
        public LeaguesDbContext DbContext { get; set; }

        public LeagueController()
        {
            DbContext = new();
        }

        // ------------------------------------
        // GET requests
        // ------------------------------------

        [HttpGet("Leagues")]
        public List<League> APILeagues()
        {
            List<League> resp = DbContext.Leagues.ToList();

            return resp;
        }

        [HttpGet("LeagueDetail/{id}")]
        public League APILeague(int id)
        {
            League resp = DbContext.Leagues
                .Where(l => l.LeagueId == id)
                .First();

            return resp;
        }

        // ------------------------------------
        // POST requests
        // ------------------------------------

        [Authorize(Roles = "True")]
        [HttpPost("AddLeague")]
        public bool APIAddLeague([FromBody] LeagueDto newObject)
        {
            try
            {
                League newLeague = new League { LeagueName = newObject.LeagueName};

                DbContext.Leagues.Add(newLeague);
                DbContext.SaveChanges();

                return true;
            } catch
            {
                return false;
            }
        }

        // ------------------------------------
        // PATCH requests
        // ------------------------------------

        [Authorize(Roles = "True")]
        [HttpPatch("EditLeague/{id}")]
        public bool APIEditLeague(int id, [FromBody] LeagueDto editedObject)
        {
            try
            {
                List<League> leagues = DbContext.Leagues.ToList();

                League league = leagues
                    .Where(l => l.LeagueId == id)
                    .First();

                league.LeagueName = editedObject.LeagueName;

                DbContext.Leagues.Update(league);
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
        [HttpDelete("DeleteLeague/{id}")]
        public bool APIDeleteLeague(int id)
        {
            try
            {
                League league = DbContext.Leagues
                    .Where(l => l.LeagueId == id)
                    .First();
                List<Match> matches = DbContext.Matches
                    .Where(m => m.LeagueId == id)
                    .ToList();

                if (matches.Count > 0)
                {
                    foreach (Match match in matches)
                    {
                        List<TeamMatch> teamMatches = DbContext.TeamMatches
                            .Where(tm => tm.MatchId == match.MatchId)
                            .ToList();
                        DbContext.TeamMatches.RemoveRange(teamMatches);
                        DbContext.SaveChanges();
                    }

                    DbContext.Matches.RemoveRange(matches);
                    DbContext.SaveChanges();
                }

                DbContext.Leagues.Remove(league);
                DbContext.SaveChanges();

                return true;
            } catch
            {
                return false;
            }
        }
    }
}
