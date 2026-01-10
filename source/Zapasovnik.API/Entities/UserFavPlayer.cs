using System.ComponentModel.DataAnnotations.Schema;

namespace Zapasovnik.API.Entities
{
 [Table("tbUsersFavPlayers")]
 public class UserFavPlayer
 {
 [Column("FK_user_id")]
 public int UserId { get; set; }

 [Column("FK_player_id")]
 public int PlayerId { get; set; }

 public User User { get; set; }
 public Player Player { get; set; }
 }
}