using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Reflection.Metadata.Ecma335;
using System.Text.Json.Nodes;
using Zapasovnik.API.DbContexts;
using Zapasovnik.API.DTOs;
using Zapasovnik.API.Entities;

namespace Zapasovnik.API.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class ZapasovnikController : ControllerBase
    {
        public List<League> Leagues { get; set; }

        public dbZapasovnikContext DbContext { get; set; }


        public ZapasovnikController()
        {
            DbContext = new();
            Leagues = DbContext.Leagues.ToList();
        }

        [HttpGet("Leagues")]
        public IEnumerable<League> APILeagues()
        {
            return Leagues.ToArray();
        }

        [HttpPost("Leagues")]
        public IEnumerable<League> APILeagues(string leagueName)
        {
            League newLeague = new League { LeagueName = leagueName };
            DbContext.Leagues.Add(newLeague);
            DbContext.SaveChanges();
            Leagues = DbContext.Leagues.ToList();
            return Leagues.ToArray();
        }
    }
}
