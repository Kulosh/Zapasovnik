using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System;
using System.Collections.Generic;

namespace Zapasovnik.API.Entities
{
    [Table("tbPlayers")]
    public class Player
    {
        [Key]
        [Column("player_id")]
        public int PlayerId { get; set; }

        [Column("player_fname")]
        public required string FirstName { get; set; }

        [Column("player_lname")]
        public required string LastName { get; set; }

        [Column("player_born")]
        public DateTime PlayerBorn { get; set; }
    }
}