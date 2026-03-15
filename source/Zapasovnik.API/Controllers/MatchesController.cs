using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Zapasovnik.API.DbContexts;
using Zapasovnik.API.DTOs;
using Zapasovnik.API.Entities;

namespace Zapasovnik.API.Controllers
{
    [Route("Zapasovnik")]
    [ApiController]
    public class MatchesController : ControllerBase
    {
        public MatchesDbContext DbContext { get; set; }

        public MatchesController()
        {
            DbContext = new();
        }

        // ------------------------------------
        // GET requests
        // ------------------------------------

        [HttpGet("TeamMatches")]
        public List<MatchesListDto> APITeamMatches()
        {
            List<Match> matches = DbContext.Matches.ToList();
            List<TeamMatch> teamMatches = DbContext.TeamsMatches.ToList();
            List<Team> teams = DbContext.Teams.ToList();

            List<MatchesListDto> resp = matches
                .Select(m => new MatchesListDto
                {
                    MatchDate = m.MatchDate,

                    MatchId = m.MatchId,

                    Team1 = teamMatches
                        .Where(tm => tm.MatchId == m.MatchId)
                        .Join(teams,
                                tm => tm.TeamId,
                                t => t.TeamId,
                                (tm, t) => t.TeamName)
                        .OrderBy(name => name)
                        .First(),

                    Team2 = teamMatches
                        .Where(tm => tm.MatchId == m.MatchId)
                        .Join(teams,
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

        // ------------------------------------
        // POST requests
        // ------------------------------------

        [HttpPost("MatchDetail")]
        public MatchDetailDto APIMatchDetail([FromBody] FavDto user)
        {
            try
            {
                List<Match> matches = DbContext.Matches.ToList();
                List<TeamMatch> teamMatches = DbContext.TeamsMatches.ToList();
                List<Team> teams = DbContext.Teams.ToList();
                List<League> leagues = DbContext.Leagues.ToList();
                List<UserFavMatch> userFavMatches = DbContext.UserFavMatches.ToList();

                MatchDetailDto resp = new()
                {
                    MatchId = user.EntityId,
                    Team1 = teamMatches
                        .Where(tm => tm.MatchId == user.EntityId)
                        .Join(teams,
                            tm => tm.TeamId,
                            t => t.TeamId,
                            (tm, t) => t.TeamName)
                        .OrderBy(team => team)
                        .First(),
                    Team2 = teamMatches
                        .Where(tm => tm.MatchId == user.EntityId)
                        .Join(teams,
                            tm => tm.TeamId,
                            t => t.TeamId,
                            (tm, t) => t.TeamName)
                        .OrderBy(team => team)
                        .Skip(1)
                        .First(),
                    League = matches
                        .Where(m => m.MatchId == user.EntityId)
                        .Join(leagues,
                            m => m.LeagueId,
                            l => l.LeagueId,
                            (m, l) => l.LeagueName)
                        .First(),
                    Date = matches
                        .Where(m => m.MatchId == user.EntityId)
                        .Select(m => Convert.ToString(m.MatchDate)!)
                        .First(),
                    IsFavorite = userFavMatches
                        .FirstOrDefault(ufm => ufm.MatchId == user.EntityId && ufm.UserId == user.UserId) != null
                };

                return resp;
            }
            catch
            {
                return null!;
            }
        }

        [Authorize(Roles = "True")]
        [HttpPost("AddMatch")]
        public bool APIAddMatch([FromBody] MatchDto newObject)
        {
            try
            {
                List<Match> matches = DbContext.Matches.ToList();
                List<Team> teams = DbContext.Teams.ToList();
                List<League> leagues = DbContext.Leagues.ToList();

                int leagueId = leagues
                    .Where(l => l.LeagueName == newObject.League)
                    .First()
                    .LeagueId;

                Match match = new()
                {
                    LeagueId = leagueId,
                    MatchDate = Convert.ToDateTime(newObject.Date)
                };

                DbContext.Matches.Add(match);
                DbContext.SaveChanges();
                matches = DbContext.Matches.ToList();

                int matchId = matches
                    .Where(m => m.LeagueId == match.LeagueId && m.MatchDate == match.MatchDate)
                    .First()
                    .MatchId;

                TeamMatch t1 = new()
                {
                    MatchId = matchId,
                    TeamId = teams
                        .Where(t => t.TeamName == newObject.Team1)
                        .First()
                        .TeamId
                };

                TeamMatch t2 = new()
                {
                    MatchId = matchId,
                    TeamId = teams
                        .Where(t => t.TeamName == newObject.Team2)
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
        [HttpPost("FavMatch")]
        public List<MatchesListDto> APIFavMatches([FromBody] int userId)
        {
            List<TeamMatch> teamMatches = DbContext.TeamsMatches.ToList();
            List<Team> teams = DbContext.Teams.ToList();
            List<UserFavMatch> userFavMatches = DbContext.UserFavMatches.ToList();

            List<MatchesListDto> resp = userFavMatches
                .Where(ufm => ufm.UserId == Convert.ToInt32(userId))
                .Join(DbContext.Matches,
                    fav => fav.MatchId,
                    m => m.MatchId,
                    (fav, m) => m)
                .Select(mwt => new MatchesListDto
                {
                    MatchDate = mwt.MatchDate,
                    MatchId = mwt.MatchId,
                    Team1 = teamMatches
                        .Where(tm => tm.MatchId == mwt.MatchId)
                        .Join(teams,
                                tm => tm.TeamId,
                                t => t.TeamId,
                                (tm, t) => t.TeamName)
                        .OrderBy(name => name)
                        .First(),
                    Team2 = teamMatches
                        .Where(tm => tm.MatchId == mwt.MatchId)
                        .Join(teams,
                                tm => tm.TeamId,
                                t => t.TeamId,
                                (tm, t) => t.TeamName)
                        .OrderBy(name => name)
                        .Skip(1)
                        .First()
                })
                .ToList();
            return resp;
        }

        [Authorize(Roles = "False")]
        [HttpPost("AddFavMatch")]
        public bool APIAddFavMatch([FromBody] FavDto match)
        {
            try
            {
                UserFavMatch userFavMatch = new()
                {
                    MatchId = match.EntityId,
                    UserId = match.UserId
                };

                DbContext.UserFavMatches.Add(userFavMatch);
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
        public bool APIDelFavPlayer([FromBody] FavDto match)
        {
            try
            {
                UserFavMatch userFavMatch = new()
                {
                    MatchId = match.EntityId,
                    UserId = match.UserId
                };

                DbContext.UserFavMatches.Remove(userFavMatch);
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
        [HttpPatch("EditMatch/{id}")]
        public bool APIEditMatch(int id, [FromBody] MatchDto editedMatch)
        {
            try
            {
                List<Match> matches = DbContext.Matches.ToList();
                List<TeamMatch> teamMatches = DbContext.TeamsMatches.ToList();
                List<Team> teams = DbContext.Teams.ToList();
                List<League> leagues = DbContext.Leagues.ToList();

                Match match = matches
                    .Where(m => m.MatchId == id)
                    .First();

                int leagueId = leagues
                    .Where(l => l.LeagueName == editedMatch.League)
                    .First()
                    .LeagueId;

                match.MatchDate = Convert.ToDateTime(editedMatch.Date);
                match.LeagueId = leagueId;

                DbContext.Matches.Update(match);
                DbContext.SaveChanges();
                matches = DbContext.Matches.ToList();

                List<TeamMatch> oldTM = teamMatches
                    .Where(tm => tm.MatchId == id)
                    .ToList();
                
                DbContext.TeamsMatches.RemoveRange(oldTM);
                DbContext.SaveChanges();

                TeamMatch t1 = new()
                {
                    MatchId = id,
                    TeamId = teams
                        .Where(t => t.TeamName == editedMatch.Team1)
                        .First()
                        .TeamId
                };

                TeamMatch t2 = new()
                {
                    MatchId = id,
                    TeamId = teams
                        .Where(t => t.TeamName == editedMatch.Team2)
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

        // ------------------------------------
        // DELETE requests
        // ------------------------------------

        [Authorize(Roles = "True")]
        [HttpDelete("DeleteMatch/{id}")]
        public bool APIDeleteMatch(int id)
        {
            try
            {
                List<Match> matches = DbContext.Matches.ToList();
                List<TeamMatch> teamMatches = DbContext.TeamsMatches.ToList();
                List<UserFavMatch> userFavMatches = DbContext.UserFavMatches.ToList();

                List<UserFavMatch> favMatches = userFavMatches
                    .Where(ufm => ufm.MatchId == id)
                    .ToList();
                if (favMatches.Count > 0)
                {
                    userFavMatches.RemoveAll(ufm => ufm.MatchId == id);
                    DbContext.UserFavMatches.RemoveRange(favMatches);
                    DbContext.SaveChanges();
                }

                List<TeamMatch> oldTeamMatches = teamMatches
                    .Where(tm => tm.MatchId == id)
                    .ToList();
                if (oldTeamMatches.Count > 0)
                {
                    teamMatches.RemoveAll(tm => tm.MatchId == id);
                    DbContext.TeamsMatches.RemoveRange(oldTeamMatches);
                    DbContext.SaveChanges();
                }

                Match match = matches
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
    }
}
