using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System;

namespace Zapasovnik.API.Entities
{
    [Table("tbTeamsPlayersArchive")]
    public class TeamPlayerArchive
    {
        [Key]
        [Column("teams_players_archive_id")]
        public int TeamsPlayersArchiveId { get; set; }

        [Column("team_id")]
        public int TeamId { get; set; }

        [Column("player_id")]
        public int PlayerId { get; set; }

        [Column("since")]
        public DateTime Since { get; set; }

        [Column("till")]
        public DateTime Till { get; set; } = DateTime.Now;
    }
}