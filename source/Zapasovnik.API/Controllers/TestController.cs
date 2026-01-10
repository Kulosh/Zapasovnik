using Microsoft.AspNetCore.Mvc;

namespace Zapasovnik.API.Controllers
{
    [ApiController]
    [Route("[controller]")]
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
    }
}
