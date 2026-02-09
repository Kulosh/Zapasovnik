namespace Zapasovnik.API.DTOs
{
    public class ChangePasswordDto
    {
        public string UserId { get; set; }
        public string Old { get; set; }
        public string New { get; set; }
    }
}
