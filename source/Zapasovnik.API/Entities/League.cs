using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Zapasovnik.API.Entities
{
    [Table("tbLeagues")]
    public class League
    {
        [Key]
        [Column("league_id")]
        public int LeagueId { get; set; }

        [Column("league_name")]
        public required string LeagueName { get; set; }
    }
}