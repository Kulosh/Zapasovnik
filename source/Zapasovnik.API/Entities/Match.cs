using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System;
using System.Collections.Generic;

namespace Zapasovnik.API.Entities
{
    [Table("tbMatches")]
    public class Match
    {
        [Key]
        [Column("match_id")]
        public int MatchId { get; set; }

        [Column("match_date")]
        public DateTime? MatchDate { get; set; }

        [Column("FK_league_id")]
        public int? LeagueId { get; set; }
    }
}