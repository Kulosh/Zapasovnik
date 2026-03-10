namespace Zapasovnik.API.DTOs
{
    public class RegisterDto
    {
        public required string UserName { get; set; }
        public string? UserEmail { get; set; }
        public required string UserPassword { get; set; }
    }
}
