using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Zapasovnik.API.DbContexts;
using Zapasovnik.API.DTOs;
using Zapasovnik.API.Entities;

namespace Zapasovnik.API.Controllers
{
    [Route("Zapasovnik/[controller]")]
    [ApiController]
    public class DeleteFavPlayerController : ControllerBase
    {
        public UserFavPlayersDb DbContext { get; set; }

        public DeleteFavPlayerController()
        {
            DbContext = new();
        }


        [HttpPost]
        public bool APIDelFavPlayer([FromBody] UserFavPlayer delFavPlayer)
        {
            try
            {
                //UserFavPlayer existing = new UserFavPlayer { UserId = delFavPlayer.UserId, PlayerId = delFavPlayer.PlayerId };
                //Console.WriteLine($"{existing.PlayerId} - pid");
                //Console.WriteLine($"{existing.UserId} - uid");

                DbContext.UsersFavPlayers.Remove(delFavPlayer);
                DbContext.SaveChanges();
                return true;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
                Console.WriteLine(e.Message);
                return false;
            }
        }
    }
}
