namespace Zapasovnik.API.DTOs
{
    public class TeamDetail
    {
        public int TeamId { get; set; }
        public string TeamName { get; set; }
        public DateTime TeamEstablished { get; set; }
        public bool isFavorite { get; set; }
    }
}
