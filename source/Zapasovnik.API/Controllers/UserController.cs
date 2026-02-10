using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Zapasovnik.API.DbContexts;
using Zapasovnik.API.DTOs;
using Zapasovnik.API.Entities;

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
    }
}
