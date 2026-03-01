using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Zapasovnik.API.Security;

namespace Zapasovnik.API.Entities
{
    [Table("tbUsers")]
    public class User
    {
        [Key]
        [Column("user_id")]
        public int UserId { get; set; }

        [Column("user_name")]
        public string UserName { get; set; }

        [Column("user_email")]
        public string ?UserEmail { get; set; }

        [Column("user_password")]
        public string UserPassword { get; set; }

        [Column("admin")]
        public bool Admin { get; set; } = false;
    }
}
