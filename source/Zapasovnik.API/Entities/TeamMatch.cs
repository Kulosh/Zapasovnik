using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Zapasovnik.API.Entities
{
    [Table("tbTeamsMatches")]
    [PrimaryKey(nameof(TeamId), nameof(MatchId))]
    public class TeamMatch
    {
        [Column("FK_team_id")]
        public int TeamId { get; set; }

        [Column("FK_match_id")]
        public int MatchId { get; set; }
    }
}