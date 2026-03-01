using System.Text;

namespace Zapasovnik.API.Security
{
    public class JwtSecret
    {
        public static byte[] LoadSecrete()
        {
            byte[] secrete;

            if (Environment.GetEnvironmentVariable("JwtSecrete") == null)
            {
                ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
                IConfiguration configuration = configurationBuilder
                    .AddUserSecrets<Program>()
                    .Build();
                secrete = Encoding.UTF8.GetBytes(configuration.GetSection("Zapasovnik")["JwtSecrete"]);
            }
            else
            {
                secrete = Encoding.UTF8.GetBytes(Environment.GetEnvironmentVariable("JwtSecrete")!);
            }

            return secrete;
        }
    }
}
