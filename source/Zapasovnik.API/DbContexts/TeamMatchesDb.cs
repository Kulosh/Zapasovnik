using Microsoft.EntityFrameworkCore;
using Zapasovnik.API.Entities;

namespace Zapasovnik.API.DbContexts
{
    public class TeamMatchesDb : DbContext
    {

        public DbSet<Match> Matches { get; set; }
        public DbSet<Team> Teams { get; set; }
        public DbSet<TeamMatch> TeamsMatches { get; set; }
        public DbSet<League> Leagues { get; set; }
        public DbSet<UserFavMatch> UserFavMatches { get; set; }

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
