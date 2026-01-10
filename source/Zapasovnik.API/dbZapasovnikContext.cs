using Microsoft.EntityFrameworkCore;

namespace Zapasovnik.API
{
    public class dbZapasovnikContext : DbContext
    {
        public DbSet<Test> Tests { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseMySQL("server=db.kulosh.eu;Port=20533;database=dbZapasovnik;uid=dbZapasovnik;password=wxm4mup0BVF4dzr-xam");
        }
    }
}
