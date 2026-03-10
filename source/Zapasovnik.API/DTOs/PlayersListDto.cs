namespace Zapasovnik.API.DTOs
{
    public class PlayersListDto
    {
        public int Id { get; set; }
        public required string FName { get; set; }
        public required string LName { get; set; }
        public string? Team { get; set; }
    }
}
