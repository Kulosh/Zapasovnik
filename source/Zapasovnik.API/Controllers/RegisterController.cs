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
        public UserDto APIRegister([FromBody] User incomeUser)
        {
            UserDto newUser = new UserDto
            {
                Username = incomeUser.UserName,
                Email = incomeUser.UserEmail,
            };

            DbContext.Users.Add(incomeUser);
            DbContext.SaveChanges();

            newUser.UserId = DbContext.Users
                .Where(u => u.UserName == incomeUser.UserName)
                .Select(u => u.UserId)
                .FirstOrDefault();
            newUser.Success = true;
            return newUser;
        }
    }
}
