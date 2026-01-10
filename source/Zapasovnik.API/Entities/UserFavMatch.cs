using System.ComponentModel.DataAnnotations.Schema;

namespace Zapasovnik.API.Entities
{
 [Table("tbUsersFavMatches")]
 public class UserFavMatch
 {
 [Column("FK_user_id")]
 public int UserId { get; set; }

 [Column("FK_match_id")]
 public int MatchId { get; set; }

 public User User { get; set; }
 public Match Match { get; set; }
 }
}