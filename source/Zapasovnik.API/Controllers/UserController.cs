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
        public UserDto APIUser([FromBody] User incomeUser)
        {
            UserDto user = new();

            incomeUser.UserPassword = PasswordHelper.HashPassword(incomeUser.UserPassword);

            if (Users.Where(u => u.UserName == incomeUser.UserName).Select(u => u.UserPassword).First() != incomeUser.UserPassword)
            {
                return new UserDto { Success = false, Email = "", UserId = -1, Username = "" };
            }

            user.Username = incomeUser.UserName;

            user.UserId = Users
                .Where(u => u.UserName == incomeUser.UserName)
                .Select(u => u.UserId)
                .FirstOrDefault();

            user.Email = Users
                .Where(u => u.UserName == incomeUser.UserName)
                .Select(u => u.UserEmail)
                .FirstOrDefault()!;

            if (user.Email == null) user.Success = false;
            else user.Success = true;

            return user;
        }

        private string GenerateJwtToken(string username)
        {
            var claims = new[]
            {
            new Claim(JwtRegisteredClaimNames.Sub, username),
            new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString())
        };

            var key = new SymmetricSecurityKey(JwtSecret.LoadSecrete());
            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

            var token = new JwtSecurityToken(
                issuer: "kulosh.eu",
                audience: "kulosh.eu",
                claims: claims,
                expires: DateTime.Now.AddMinutes(30),
                signingCredentials: creds);

            return new JwtSecurityTokenHandler().WriteToken(token);
        }
    }
}
