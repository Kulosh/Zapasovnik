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
    public class MatchesController : ControllerBase
    {
        public TeamMatchesDb DbContext { get; set; }
        public List<Team> Teams { get; set; }
        public List<TeamMatch> TeamsMatches { get; set; }
        public List<Match> Matches { get; set; }
        public List<League> Leagues { get; set; }
        public List<UserFavMatch> UserFavMatches { get; set; }

        public MatchesController()
        {
            DbContext = new();

            Teams = DbContext.Teams.ToList();
            TeamsMatches = DbContext.TeamsMatches.ToList();
            Matches = DbContext.Matches.ToList();
            Leagues = DbContext.Leagues.ToList();
            UserFavMatches = DbContext.UserFavMatches.ToList();
        }

        [HttpGet("TeamMatches")]
        public List<MatchesListDto> APITeamMatches()
        {
            List<MatchesListDto> resp = Matches
                .Select(m => new MatchesListDto
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
                        .First(),

                    Team2 = TeamsMatches
                        .Where(tm => tm.MatchId == m.MatchId)
                        .Join(Teams,
                                tm => tm.TeamId,
                                t => t.TeamId,
                                (tm, t) => t.TeamName)
                        .OrderBy(name => name)
                        .Skip(1)
                        .First()
                })
                .OrderBy(x => x.MatchDate)
                .ToList();

            return resp;
        }

        [HttpPost("MatchDetail")]
        public MatchDetailDto APIMatchDetail([FromBody] FavDto user)
        {
            try
            {
                MatchDetailDto match = new()
                {
                    MatchId = user.EntityId,
                    Team1 = TeamsMatches
                        .Where(tm => tm.MatchId == user.EntityId)
                        .Join(Teams,
                            tm => tm.TeamId,
                            t => t.TeamId,
                            (tm, t) => t.TeamName)
                        .OrderBy(team => team)
                        .First(),
                    Team2 = TeamsMatches
                        .Where(tm => tm.MatchId == user.EntityId)
                        .Join(Teams,
                            tm => tm.TeamId,
                            t => t.TeamId,
                            (tm, t) => t.TeamName)
                        .OrderBy(team => team)
                        .Skip(1)
                        .First(),
                    League = Matches
                        .Where(m => m.MatchId == user.EntityId)
                        .Join(Leagues,
                            m => m.LeagueId,
                            l => l.LeagueId,
                            (m, l) => l.LeagueName)
                        .First(),
                    Date = Matches
                        .Where(m => m.MatchId == user.EntityId)
                        .Select(m => Convert.ToString(m.MatchDate)!)
                        .First(),
                    IsFavorite = UserFavMatches
                        .FirstOrDefault(ufm => ufm.MatchId == user.EntityId && ufm.UserId == user.UserId) != null
                };

                return match;
            }
            catch
            {
                return null!;
            }
        }

        [Authorize(Roles = "True")]
        [HttpPost("AddMatch")]
        public bool APIAddMatch([FromBody] MatchDto newMatch)
        {
            try
            {

                int leagueId = Leagues
                .Where(l => l.LeagueName == newMatch.League)
                .First()
                .LeagueId;

                Match match = new()
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

                TeamMatch t1 = new()
                {
                    MatchId = matchId,
                    TeamId = Teams
                        .Where(t => t.TeamName == newMatch.Team1)
                        .First()
                        .TeamId
                };

                TeamMatch t2 = new()
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

        [Authorize(Roles = "False")]
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

        [Authorize(Roles = "False")]
        [HttpPost("FavMatch")]
        public List<MatchesListDto> APIFavMatches(int userId)
        {
            List<MatchesListDto> favMatches = UserFavMatches
                .Where(ufm => ufm.UserId == Convert.ToInt32(userId))
                .Join(DbContext.Matches,
                    fav => fav.MatchId,
                    m => m.MatchId,
                    (fav, m) => m)
                .Select(mwt => new MatchesListDto
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
                        .First(),
                    Team2 = TeamsMatches
                        .Where(tm => tm.MatchId == mwt.MatchId)
                        .Join(Teams,
                                tm => tm.TeamId,
                                t => t.TeamId,
                                (tm, t) => t.TeamName)
                        .OrderBy(name => name)
                        .Skip(1)
                        .First()
                })
                .ToList();
            return favMatches;
        }

        [Authorize(Roles = "True")]
        [HttpPatch("EditMatch/{id}")]
        public bool APIEditMatch(int id, [FromBody] MatchDto newMatch)
        {
            try
            {
                Match match = Matches
                    .Where(m => m.MatchId == id)
                    .First();

                int leagueId = Leagues
                    .Where(l => l.LeagueName == newMatch.League)
                    .First()
                    .LeagueId;

                match.MatchDate = Convert.ToDateTime(newMatch.Date);
                match.LeagueId = leagueId;

                DbContext.Matches.Update(match);
                DbContext.SaveChanges();
                Matches = DbContext.Matches.ToList();

                List<TeamMatch> oldTM = TeamsMatches
                    .Where(tm => tm.MatchId == id)
                    .ToList();
                
                DbContext.TeamsMatches.RemoveRange(oldTM);
                DbContext.SaveChanges();

                TeamMatch t1 = new()
                {
                    MatchId = id,
                    TeamId = Teams
                        .Where(t => t.TeamName == newMatch.Team1)
                        .First()
                        .TeamId
                };

                TeamMatch t2 = new()
                {
                    MatchId = id,
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

        [Authorize(Roles = "True")]
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

        [Authorize(Roles = "False")]
        [HttpPost("DeleteFavMatch")]
        public bool APIDelFavPlayer([FromBody] UserFavMatch delUserFavMatch)
        {
            try
            {
                DbContext.UserFavMatches.Remove(delUserFavMatch);
                DbContext.SaveChanges();
                return true;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
                Console.WriteLine(e.Message);
                return false;
            }
        }
    }
}
