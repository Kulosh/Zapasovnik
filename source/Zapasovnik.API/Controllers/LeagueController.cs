using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Reflection.Metadata.Ecma335;
using System.Text.Json.Nodes;
using Zapasovnik.API.DbContexts;
using Zapasovnik.API.DTOs;
using Zapasovnik.API.Entities;

namespace Zapasovnik.API.Controllers
{
    [ApiController]
    [Route("zapasovnik")]
    public class LeagueController : ControllerBase
    {
        public LeaguesDbContext DbContext { get; set; }


        public LeagueController()
        {
            DbContext = new();
        }

        [HttpGet("Leagues")]
        public List<League> APILeagues()
        {
            List<League> Leagues = DbContext.Leagues.ToList();
            return Leagues;
        }

        [HttpGet("LeagueDetail/{id}")]
        public League APILeague(int id)
        {
            League league = DbContext.Leagues.Where(l => l.LeagueId == id).First();

            return league;
        }

        [HttpPost("AddLeague")]
        public bool APIAddLeague(string leagueName)
        {
            List<League> Leagues = DbContext.Leagues.ToList();

            try
            {
                League newLeague = new League { LeagueName = leagueName };
                DbContext.Leagues.Add(newLeague);
                DbContext.SaveChanges();
                return true;
            } catch
            {
                return false;
            }
        }

        [HttpDelete("DeleteLeague/{id}")]
        public bool APIDeleteLeague(int id)
        {
            try
            {
                League league = DbContext.Leagues.Where(l => l.LeagueId == id).First();
                List<Match> matches = DbContext.Matches.Where(m => m.LeagueId == id).ToList();
                if (matches.Count > 0)
                {
                    foreach (Match match in matches)
                    {
                        List<TeamMatch> teamMatches = DbContext.TeamMatches.Where(tm => tm.MatchId == match.MatchId).ToList();
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
