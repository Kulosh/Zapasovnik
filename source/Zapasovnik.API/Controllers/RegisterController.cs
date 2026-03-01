using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Zapasovnik.API.DbContexts;
using Zapasovnik.API.DTOs;
using Zapasovnik.API.Entities;
using Zapasovnik.API.Security;

namespace Zapasovnik.API.Controllers
{
    [Route("Zapasovnik/[controller]")]
    [ApiController]
    public class RegisterController : ControllerBase
    {
        public dbZapasovnikContext DbContext { get; set; }
        public List<User> Users { get; set; }  

        public RegisterController()
        {
            DbContext = new();
            Users = DbContext.Users.ToList();
        }

        [HttpPost]
        public IActionResult APIRegister([FromBody] RegisterDto incomeUser)
        {
            incomeUser.UserPassword = PasswordHelper.HashPassword(incomeUser.UserPassword);

            User newUser = new User
            {
                UserName = incomeUser.UserName,
                UserEmail = incomeUser.UserEmail,
                UserPassword = incomeUser.UserPassword,
                Admin = false
            };

            DbContext.Users.Add(newUser);
            DbContext.SaveChanges();

            User user = Users.OrderBy(u => u.UserId).Last();

            string token = JwtTokenGen.GenerateJwtToken(user.UserId, user.UserName, user.UserEmail!, user.Admin);

            return Ok(token);
        }
    }
}
