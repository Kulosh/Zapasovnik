namespace Zapasovnik.API.DTOs
{
    public class PlayerDetailDto
    {
        public int Id { get; set; }
        public required string Fname { get; set; }
        public required string Lname { get; set; }
        public required string Birth { get; set; }
        public string? Team { get; set; }
        public bool IsFavorite { get; set; }
    }
}
