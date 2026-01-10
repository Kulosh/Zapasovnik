using Microsoft.EntityFrameworkCore;

namespace Zapasovnik.API
{
    public class dbZapasovnikContext : DbContext
    {
        public DbSet<Test> Tests { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseMySQL();
        }
    }
}
