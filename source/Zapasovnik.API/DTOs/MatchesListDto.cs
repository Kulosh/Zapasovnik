using System.ComponentModel.DataAnnotations.Schema;

namespace Zapasovnik.API.DTOs
{
    public class MatchesListDto
    {
        public int MatchId { get; set; }
        public DateTime MatchDate { get; set; }
        public required string Team1 { get; set; }
        public required string Team2 { get; set; }
    }
}
