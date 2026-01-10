using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System;
using Microsoft.EntityFrameworkCore;

namespace Zapasovnik.API.Entities
{
    [Table("tbTeamsPlayers")]
    [PrimaryKey(nameof(TeamId), nameof(PlayerId))]
    public class TeamPlayer
    {
        [Column("teams_players_id")]
        public int TeamsPlayersId { get; set; }

        [Column("FK_team_id")]
        public int TeamId { get; set; }

        [Column("FK_player_id")]
        public int PlayerId { get; set; }

        [Column("since")]
        public DateTime Since { get; set; } = DateTime.Now;
    }
}