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
    public class ChgPwdController : ControllerBase
    {
        public dbZapasovnikContext DbContext { get; set; }
        public List<User> Users { get; set; }

        public ChgPwdController()
        {
            DbContext = new();
            Users = DbContext.Users.ToList();
        }

        [HttpPost]
        public bool APIChangePassword([FromBody] ChangePasswordDto chg)
        {
            User user = Users
                .Where(u => Convert.ToString(u.UserId) == chg.UserId)
                .First();

            if (user.UserPassword != chg.Old) return false;
            else
            {
                user.UserPassword = chg.New;

                DbContext.Users.Update(user);
                DbContext.SaveChanges();
                return true;
            }
        }
    }
}
