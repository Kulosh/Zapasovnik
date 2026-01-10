using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations.Schema;

namespace Zapasovnik.API.Entities
{
    [Table("tbUsersFavTeams")]
    [PrimaryKey(nameof(UserId), nameof(TeamId))]
    public class UserFavTeam
    {
        [Column("FK_user_id")]
        public int UserId { get; set; }

        [Column("FK_team_id")]
        public int TeamId
        {
            get; set;
        }
    }
}