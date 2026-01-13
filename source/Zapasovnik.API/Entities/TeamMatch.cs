using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Zapasovnik.API.Entities
{
 [Table("tbTeamsMatches")]
 public class TeamMatch
 {
 [Column("FK_team_id")]
 public int TeamId { get; set; }

 [Column("FK_match_id")]
 public int MatchId { get; set; }

 [ForeignKey("TeamId")]
 public virtual Team Team { get; set; }

 [ForeignKey("MatchId")]
 public virtual Match Match { get; set; }
 }
}