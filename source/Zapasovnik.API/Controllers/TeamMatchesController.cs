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
    public class TeamMatchesController : ControllerBase
    {
        public TeamMatchesDb DbContext { get; set; }
        public List<Team> Teams { get; set; }
        public List<TeamMatch> TeamsMatches { get; set; }
        public List<Match> Matches { get; set; }
        public List<League> Leagues { get; set; }
        public List<UserFavMatch> UserFavMatches { get; set; }

        public TeamMatchesController()
        {
            DbContext = new();

            Teams = DbContext.Teams.ToList();
            TeamsMatches = DbContext.TeamsMatches.ToList();
            Matches = DbContext.Matches.ToList();
            Leagues = DbContext.Leagues.ToList();
            UserFavMatches = DbContext.UserFavMatches.ToList();
        }

        [HttpGet("TeamMatches")]
        public List<MatchWithTeamsDto> APITeamMatches()
        {
            List<MatchWithTeamsDto> rows = Matches
                .Select(m => new MatchWithTeamsDto
                {
                    MatchDate = m.MatchDate,

                    MatchId = m.MatchId,

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

        [HttpGet("MatchDetail/{id}")]
        public MatchDetailDto APIMatchDetail(int id)
        {
            try
            {
                MatchDetailDto match = Matches
                .Select(m => new MatchDetailDto
                {
                    Date = m.MatchDate.ToString()!,

                    MatchId = m.MatchId,

                    Team1 = TeamsMatches
                        .Where(tm => tm.MatchId == m.MatchId)
                        .Join(Teams,
                                tm => tm.TeamId,
                                t => t.TeamId,
                                (tm, t) => t.TeamName)
                        .OrderBy(name => name)
                        .First(),

                    Team2 = TeamsMatches
                        .Where(tm => tm.MatchId == m.MatchId)
                        .Join(Teams,
                                tm => tm.TeamId,
                                t => t.TeamId,
                                (tm, t) => t.TeamName)
                        .OrderBy(name => name)
                        .Skip(1)
                        .First(),
                    League = Matches
                        .Where(ml => ml.LeagueId == 1)
                        .Join(Leagues,
                            ml => ml.LeagueId,
                            l => l.LeagueId,
                            (ml, l) => l.LeagueName)
                        .First()
                })
                .First();

                return match;
            }
            catch
            {
                return null;
            }
        }

        [HttpPost("AddMatch")]
        public bool APIAddMatch([FromBody] AddMatchDto newMatch)
        {
            try
            {

                int leagueId = Leagues
                .Where(l => l.LeagueName == newMatch.League)
                .First()
                .LeagueId;

                Match match = new Match
                {
                    LeagueId = leagueId,
                    MatchDate = Convert.ToDateTime(newMatch.Date)
                };

                DbContext.Matches.Add(match);
                DbContext.SaveChanges();
                Matches = DbContext.Matches.ToList();

                int matchId = Matches
                    .Where(m => m.LeagueId == match.LeagueId && m.MatchDate == match.MatchDate)
                    .First()
                    .MatchId;

                TeamMatch t1 = new TeamMatch
                {
                    MatchId = matchId,
                    TeamId = Teams
                        .Where(t => t.TeamName == newMatch.Team1)
                        .First()
                        .TeamId
                };

                TeamMatch t2 = new TeamMatch
                {
                    MatchId = matchId,
                    TeamId = Teams
                        .Where(t => t.TeamName == newMatch.Team2)
                        .First()
                        .TeamId
                };

                DbContext.TeamsMatches.Add(t1);
                DbContext.TeamsMatches.Add(t2);
                DbContext.SaveChanges();

                return true;
            }
            catch
            {
                return false;
            }
        }

        [HttpDelete("DeleteMatch/{id}")]
        public bool APIDeleteMatch(int id)
        {
            try
            {
                List<UserFavMatch> favMatches = UserFavMatches
                    .Where(ufm => ufm.MatchId == id)
                    .ToList();
                if (favMatches.Count > 0)
                {
                    UserFavMatches.RemoveAll(ufm => ufm.MatchId == id);
                    DbContext.UserFavMatches.RemoveRange(favMatches);
                    DbContext.SaveChanges();
                }

                List<TeamMatch> teamMatches = TeamsMatches
                    .Where(tm => tm.MatchId == id)
                    .ToList();
                if (teamMatches.Count > 0)
                {
                    TeamsMatches.RemoveAll(tm => tm.MatchId == id);
                    DbContext.TeamsMatches.RemoveRange(teamMatches);
                    DbContext.SaveChanges();
                }

                Match match = Matches
                    .Where(m => m.MatchId == id)
                    .First();
                DbContext.Remove(match);
                DbContext.SaveChanges();

                return true;
            }
            catch
            {
                return false;
            }
        }

        [HttpPost("AddFavMatch")]
        public bool APIAddFavMatch([FromBody] UserFavMatch newFavMatch)
        {
            try
            {
                DbContext.UserFavMatches.Add(newFavMatch);
                DbContext.SaveChanges();
                return true;
            }
            catch
            {
                return false;
            }
        }

        [HttpPost("FavMatch")]
        public List<MatchWithTeamsDto> APIFavMatches([FromBody] UserDto userId)
        {
            List<MatchWithTeamsDto> favMatches = UserFavMatches
                .Where(ufm => ufm.UserId == Convert.ToInt32(userId.UserId))
                .Join(DbContext.Matches,
                    fav => fav.MatchId,
                    m => m.MatchId,
                    (fav, m) => m)
                .Select(mwt => new MatchWithTeamsDto
                {
                    MatchDate = mwt.MatchDate,
                    MatchId = mwt.MatchId,
                    Team1 = TeamsMatches
                        .Where(tm => tm.MatchId == mwt.MatchId)
                        .Join(Teams,
                                tm => tm.TeamId,
                                t => t.TeamId,
                                (tm, t) => t.TeamName)
                        .OrderBy(name => name)
                        .FirstOrDefault(),
                    Team2 = TeamsMatches
                        .Where(tm => tm.MatchId == mwt.MatchId)
                        .Join(Teams,
                                tm => tm.TeamId,
                                t => t.TeamId,
                                (tm, t) => t.TeamName)
                        .OrderBy(name => name)
                        .Skip(1)
                        .FirstOrDefault()
                })
                .ToList();
            return favMatches;
        }
    }
}
