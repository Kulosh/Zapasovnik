namespace Zapasovnik.API.DTOs
{
    public class MatchDto
    {
        public required string Team1 { get; set; }
        public required string Team2 { get; set; }
        public required string League { get; set; }
        public required string Date { get; set; }
    }
}
