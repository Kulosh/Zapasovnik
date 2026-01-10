using Microsoft.EntityFrameworkCore;
using Zapasovnik.API.Entities;

namespace Zapasovnik.API
{
    public class dbZapasovnikContext : DbContext
    {
        public DbSet<Test> Tests { get; set; }

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
            optionsBuilder.UseMySQL("");
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            // Composite keys
            modelBuilder.Entity<TeamMatch>().HasKey(tm => new { tm.TeamId, tm.MatchId });
            modelBuilder.Entity<TeamPlayer>().HasKey(tp => new { tp.TeamId, tp.PlayerId, tp.TeamsPlayersId });
            modelBuilder.Entity<UserFavPlayer>().HasKey(u => new { u.UserId, u.PlayerId });
            modelBuilder.Entity<UserFavMatch>().HasKey(u => new { u.UserId, u.MatchId });
            modelBuilder.Entity<UserFavTeam>().HasKey(u => new { u.UserId, u.TeamId });

            // Relationships
            modelBuilder.Entity<Match>()
                .HasOne(m => m.League)
                .WithMany(l => l.Matches)
                .HasForeignKey(m => m.LeagueId)
                .HasConstraintName("FK_tbMa_league_id");

            modelBuilder.Entity<TeamMatch>()
                .HasOne(tm => tm.Team)
                .WithMany(t => t.TeamMatches)
                .HasForeignKey(tm => tm.TeamId)
                .HasConstraintName("FK_tbTeMa_team_id");

            modelBuilder.Entity<TeamMatch>()
                .HasOne(tm => tm.Match)
                .WithMany(m => m.TeamMatches)
                .HasForeignKey(tm => tm.MatchId)
                .HasConstraintName("FK_tbTeMa_match_id");

            modelBuilder.Entity<TeamPlayer>()
                .HasOne(tp => tp.Team)
                .WithMany(t => t.TeamPlayers)
                .HasForeignKey(tp => tp.TeamId)
                .HasConstraintName("FK_tbTePl_team_id");

            modelBuilder.Entity<TeamPlayer>()
                .HasOne(tp => tp.Player)
                .WithMany(p => p.TeamPlayers)
                .HasForeignKey(tp => tp.PlayerId)
                .HasConstraintName("FK_tbTePl_player_id");

            modelBuilder.Entity<UserFavPlayer>()
                .HasOne(u => u.User)
                .WithMany(us => us.FavPlayers)
                .HasForeignKey(u => u.UserId)
                .HasConstraintName("FK_tbUsFaPl_user_id");

            modelBuilder.Entity<UserFavPlayer>()
                .HasOne(u => u.Player)
                .WithMany(p => p.UserFavPlayers)
                .HasForeignKey(u => u.PlayerId)
                .HasConstraintName("FK_tbUsFaPl_player_id");

            modelBuilder.Entity<UserFavMatch>()
                .HasOne(u => u.User)
                .WithMany(us => us.FavMatches)
                .HasForeignKey(u => u.UserId)
                .HasConstraintName("FK_tbUsFaMa_user_id");

            modelBuilder.Entity<UserFavMatch>()
                .HasOne(u => u.Match)
                .WithMany(m => m.UserFavMatches)
                .HasForeignKey(u => u.MatchId)
                .HasConstraintName("FK_tbUsFaMa_match_id");

            modelBuilder.Entity<UserFavTeam>()
                .HasOne(u => u.User)
                .WithMany(us => us.FavTeams)
                .HasForeignKey(u => u.UserId)
                .HasConstraintName("FK_tbUsFaTe_user_id");

            modelBuilder.Entity<UserFavTeam>()
                .HasOne(u => u.Team)
                .WithMany(t => t.UserFavTeams)
                .HasForeignKey(u => u.TeamId)
                .HasConstraintName("FK_tbUsFaTe_team_id");

            // Defaults for MySQL timestamps
            modelBuilder.Entity<TeamPlayer>()
                .Property(tp => tp.Since)
                .HasDefaultValueSql("NOW()");

            modelBuilder.Entity<TeamPlayerArchive>()
                .Property(tp => tp.Till)
                .HasDefaultValueSql("NOW()");
        }
    }
}
