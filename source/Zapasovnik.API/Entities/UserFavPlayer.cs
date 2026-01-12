using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations.Schema;

namespace Zapasovnik.API.Entities
{
    [Table("tbUsersFavPlayers")]
    [PrimaryKey(nameof(UserId), nameof(PlayerId))]
    public class UserFavPlayer
    {
        [Column("FK_user_id")]
        public int UserId { get; set; }

        [Column("FK_player_id")]
        public int PlayerId { get; set; }
    }
}