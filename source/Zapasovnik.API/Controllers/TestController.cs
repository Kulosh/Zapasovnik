using Microsoft.AspNetCore.Mvc;

namespace Zapasovnik.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TestController : ControllerBase
    {
        public List<Test> Tests { get; set; }
        public dbZapasovnikContext DbContext { get; set; }

        public TestController()
        {
            DbContext = new();
            Tests = DbContext.Tests.ToList();
        }

        [HttpGet(Name = "GetTest")]
        public IEnumerable<Test> Get()
        {
            return Tests.ToArray();
        }

        [HttpPost(Name = "PostTest")]
        public IEnumerable<Test> Post(string subject)
        {
            Test newTest = new Test { Subject = subject };
            DbContext.Tests.Add(newTest);
            DbContext.SaveChanges();
            Tests = DbContext.Tests.ToList();
            return Tests.ToArray();
        }
    }
}
