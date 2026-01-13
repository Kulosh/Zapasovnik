using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Collections.Generic;

namespace Zapasovnik.API.Entities
{
 [Table("tbUsers")]
 public class User
 {
 [Key]
 [Column("user_id")]
 public int UserId { get; set; }

 [Column("user_name")]
 public string UserName { get; set; }

 [Column("user_email")]
 public string UserEmail { get; set; }

 [Column("user_password")]
 public string UserPassword { get; set; }

 public virtual ICollection<UserFavPlayer> FavPlayers { get; set; }
 public virtual ICollection<UserFavMatch> FavMatches { get; set; }
 public virtual ICollection<UserFavTeam> FavTeams { get; set; }
 }
}