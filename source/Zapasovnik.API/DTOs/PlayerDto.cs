namespace Zapasovnik.API.DTOs
{
    public class PlayerDto
    {
        public required string FName { get; set; }
        public required string LName { get; set; }
        public required string Birth { get; set; }
        public string? Team { get; set; }
    }
}
