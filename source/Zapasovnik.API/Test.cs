using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Zapasovnik.API
{
    [Table("tbTest")]
    public class Test
    {
        [Key]
        [Column("Id")]
        public int Id { get; set; }
        [Column("Subject")]
        public string Subject { get; set; }
    }
}
