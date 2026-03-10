namespace Zapasovnik.API.DTOs
{
    public class ChgPwdDto
    {
        public int UserId { get; set; }
        public required string Old { get; set; }
        public required string New { get; set; }
    }
}
