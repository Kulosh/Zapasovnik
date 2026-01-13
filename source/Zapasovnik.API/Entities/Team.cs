using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System;
using System.Collections.Generic;

namespace Zapasovnik.API.Entities
{
 [Table("tbTeams")]
 public class Team
 {
 [Key]
 [Column("team_id")]
 public int TeamId { get; set; }

 [Column("team_name")]
 public string TeamName { get; set; }

 [Column("team_established")]
 public DateTime TeamEstablished { get; set; }

 public virtual ICollection<TeamMatch> TeamMatches { get; set; }
 public virtual ICollection<TeamPlayer> TeamPlayers { get; set; }
 public virtual ICollection<UserFavTeam> UserFavTeams { get; set; }
 }
}