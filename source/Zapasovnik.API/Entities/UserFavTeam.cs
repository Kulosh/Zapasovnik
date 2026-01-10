using System.ComponentModel.DataAnnotations.Schema;

namespace Zapasovnik.API.Entities
{
 [Table("tbUsersFavTeams")]
 public class UserFavTeam
 {
 [Column("FK_user_id")]
 public int UserId { get; set; }

 [Column("FK_team_id")]
 public int TeamId { get; set; }

 public User User { get; set; }
 public Team Team { get; set; }
 }
}