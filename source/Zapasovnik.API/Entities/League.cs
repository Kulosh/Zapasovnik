using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Collections.Generic;

namespace Zapasovnik.API.Entities
{
    [Table("tbLeagues")]
    public class League
    {
        [Key]
        [Column("league_id")]
        public int LeagueId { get; set; }

        [Column("league_name")]
        public string LeagueName { get; set; }
    }
}