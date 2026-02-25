using Microsoft.EntityFrameworkCore;
using Zapasovnik.API.Entities;

namespace Zapasovnik.API.DbContexts
{
    public class TeamsDbContext : DbContext
    {
        public DbSet<Team> Teams { get; set; }
        public DbSet<UserFavTeam> UserFavTeams { get; set; }
        public DbSet<TeamMatch> TeamMatches { get; set; }
        public DbSet<TeamPlayer> TeamPlayers { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            string connection;

            if (Environment.GetEnvironmentVariable("ConnectionString") == null)
            {
                ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
                IConfiguration configuration = configurationBuilder
                    .AddUserSecrets<Program>()
                    .Build();
                connection = configuration.GetSection("Zapasovnik")["ConnectionString"];
            }
            else
            {
                connection = Environment.GetEnvironmentVariable("ConnectionString")!;
            }

            optionsBuilder.UseMySQL(connection);
        }
    }
}
