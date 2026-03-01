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
        public IActionResult APIRegister([FromBody] User incomeUser)
        {
            UserDto user= new UserDto
            {
                Username = incomeUser.UserName,
                Email = incomeUser.UserEmail,
            };

            incomeUser.UserPassword = PasswordHelper.HashPassword(incomeUser.UserPassword);

            DbContext.Users.Add(incomeUser);
            DbContext.SaveChanges();

            user.UserId = DbContext.Users
                .Where(u => u.UserName == incomeUser.UserName)
                .Select(u => u.UserId)
                .FirstOrDefault();
            user.Success = true;

            string token = JwtTokenGen.GenerateJwtToken(incomeUser.UserName);

            return Ok(new { token, user });
        }
    }
}
