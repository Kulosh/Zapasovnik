namespace Zapasovnik.API.DTOs
{
    public class PlayerDetailDto
    {
        public int Id { get; set; }
        public string Fname { get; set; }
        public string Lname { get; set; }
        public string Birth { get; set; }
        public string Team { get; set; }
        public bool IsFavorite { get; set; }
    }
}
