using System.ComponentModel.DataAnnotations.Schema;

namespace Zapasovnik.API
{
    public class MatchWithTeamsDto
    {
        //public int MatchId { get; set; }
        public DateTime? MatchDate { get; set; }
        public string? Team1 { get; set; }
        public string? Team2 { get; set; }
    }
}
