
namespace Zapasovnik.API.DTOs
{
    public class MatchDetailDto
    {
        public int MatchId { get; set; }
        public string Team1 { get; set; }
        public string Team2 { get; set; }
        public string League { get; set; }
        public string Date { get; set; }
        public bool IsFavorite { get; set; }
    }
}
