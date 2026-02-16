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
    public class DeleteFavMatchController : ControllerBase
    {
        public TeamMatchesDb DbContext { get; set; }

        public DeleteFavMatchController()
        {
            DbContext = new();
        }


        [HttpPost]
        public bool APIDelFavPlayer([FromBody] UserFavMatch delUserFavMatch)
        {
            try
            {
                DbContext.UserFavMatches.Remove(delUserFavMatch);
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
