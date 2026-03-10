using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Zapasovnik.API.Entities
{
    [Table("tbUsers")]
    public class User
    {
        [Key]
        [Column("user_id")]
        public int UserId { get; set; }

        [Column("user_name")]
        public required string UserName { get; set; }

        [Column("user_email")]
        public required string UserEmail { get; set; }

        [Column("user_password")]
        public required string UserPassword { get; set; }

        [Column("admin")]
        public bool Admin { get; set; } = false;
    }
}
