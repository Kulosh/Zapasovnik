using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Zapasovnik.API.Entities
{
    [Table("tbTeams")]
    public class Team
    {
        [Key]
        [Column("team_id")]
        public int TeamId { get; set; }

        [Column("team_name")]
        public required string TeamName { get; set; }

        [Column("team_established")]
        public DateTime TeamEstablished { get; set; }
    }
}