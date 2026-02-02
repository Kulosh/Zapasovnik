namespace Zapasovnik.API.DTOs
{
    public class ChangePasswordDto
    {
        public string Username { get; set; }
        public string Old { get; set; }
        public string New { get; set; }
    }
}
