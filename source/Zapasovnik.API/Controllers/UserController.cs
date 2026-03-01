using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using Zapasovnik.API.DbContexts;
using Zapasovnik.API.DTOs;
using Zapasovnik.API.Entities;
using Zapasovnik.API.Security;

namespace Zapasovnik.API.Controllers
{
    [Route("Zapasovnik/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        public dbZapasovnikContext DbContext { get; set; }
        public List<User> Users { get; set; }

        public UserController()
        {
            DbContext = new();
            Users = DbContext.Users.ToList();
        }

        [HttpPost]
        public IActionResult APIUser([FromBody] User incomeUser)
        {
            incomeUser.UserPassword = PasswordHelper.HashPassword(incomeUser.UserPassword);

            if (Users.Where(u => u.UserName == incomeUser.UserName).Select(u => u.UserPassword).FirstOrDefault() != incomeUser.UserPassword)
            {
                return Unauthorized(JwtTokenGen.GenerateJwtToken(-1, "", "", false));
            }

            User user = Users.Where(u => u.UserName == incomeUser.UserName && u.UserPassword == incomeUser.UserPassword).First();

            string token = JwtTokenGen.GenerateJwtToken(user.UserId, user.UserName, user.UserEmail!, user.Admin);

            return Ok(token);
        }

        [Authorize(Roles = "True")]
        [HttpGet]
        public IActionResult APIGetUsers()
        {
            return Ok( Users.ToList() );
        }
    }
}
