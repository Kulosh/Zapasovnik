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
        public UsersOnlyDbContext DbContext { get; set; }
        public List<User> Users { get; set; }

        public LoginController()
        {
            DbContext = new();
            Users = DbContext.Users.ToList();
        }

        [HttpPost]
        public IActionResult APIUser([FromBody] RegisterDto login)
        {
            login.UserPassword = PasswordHelper.HashPassword(login.UserPassword);

            if (Users
                .Where(u => u.UserName == login.UserName)
                .Select(u => u.UserPassword)
                .FirstOrDefault() != login.UserPassword)
            {
                return Unauthorized($"{JwtTokenGen.GenerateJwtToken(-1, "", "", false)}");
            }

            User user = Users
                .Where(u => u.UserName == login.UserName && u.UserPassword == login.UserPassword)
                .First();

            string token = JwtTokenGen.GenerateJwtToken(user.UserId, user.UserName, user.UserEmail, user.Admin);

            return Ok($"{token}");
        }
    }
}
