using Microsoft.AspNetCore.Mvc;
using Zapasovnik.API.DbContexts;
using Zapasovnik.API.DTOs;
using Zapasovnik.API.Entities;
using Zapasovnik.API.Security;

namespace Zapasovnik.API.Controllers
{
    [Route("Zapasovnik/[controller]")]
    [ApiController]
    public class LoginController : ControllerBase
    {
        public UsersOnlyDb DbContext { get; set; }
        public List<User> Users { get; set; }

        public LoginController()
        {
            DbContext = new();
            Users = DbContext.Users.ToList();
        }

        [HttpPost]
        public IActionResult APIUser([FromBody] RegisterDto incomeUser)
        {
            incomeUser.UserPassword = PasswordHelper.HashPassword(incomeUser.UserPassword);

            if (Users.Where(u => u.UserName == incomeUser.UserName).Select(u => u.UserPassword).FirstOrDefault() != incomeUser.UserPassword)
            {
                return Unauthorized($"{JwtTokenGen.GenerateJwtToken(-1, "", "", false)}");
            }

            User user = Users.Where(u => u.UserName == incomeUser.UserName && u.UserPassword == incomeUser.UserPassword).First();

            string token = JwtTokenGen.GenerateJwtToken(user.UserId, user.UserName, user.UserEmail!, user.Admin);

            return Ok($"{token}");
        }
    }
}
