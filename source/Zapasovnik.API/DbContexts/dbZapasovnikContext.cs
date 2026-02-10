using Microsoft.EntityFrameworkCore;
using Zapasovnik.API.Entities;

namespace Zapasovnik.API.DbContexts
{
    public class dbZapasovnikContext : DbContext
    {
        public DbSet<League> Leagues { get; set; }
        public DbSet<Match> Matches { get; set; }
        public DbSet<Team> Teams { get; set; }
        public DbSet<TeamMatch> TeamsMatches { get; set; }
        public DbSet<Player> Players { get; set; }
        public DbSet<TeamPlayer> TeamsPlayers { get; set; }
        public DbSet<TeamPlayerArchive> TeamsPlayersArchive { get; set; }
        public DbSet<User> Users { get; set; }
        public DbSet<UserFavPlayer> UsersFavPlayers { get; set; }
        public DbSet<UserFavMatch> UsersFavMatches { get; set; }
        public DbSet<UserFavTeam> UsersFavTeams { get; set; }

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
