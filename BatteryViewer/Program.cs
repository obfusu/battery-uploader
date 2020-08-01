using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;

using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Hosting;

namespace BatteryViewer
{
    public class Program
    {
        static string LocalIpAddressString = string.Empty;

        public static void Main(string[] args)
        {
            LocalIpAddressString = FindLocalIpv4Addresses();

            CreateHostBuilder(args).Build().Run();
        }

        private static string FindLocalIpv4Addresses()
        {
            string hostName = Dns.GetHostName();
            var addresses = Dns.GetHostEntry(hostName).AddressList;
            List<string> IpV4Addresses = new List<string>();
            foreach (IPAddress address in addresses)
            {
                // Only Ipv4
                if (address.AddressFamily == AddressFamily.InterNetwork)
                {
                    IpV4Addresses.Add(address.ToString());
                }
            }

            var builder = new StringBuilder();

            foreach (string address in IpV4Addresses)
            {
                builder.Append(";http://");
                builder.Append(address);
                builder.Append(":5000");
            }

            return builder.ToString();
        }

        public static IHostBuilder CreateHostBuilder(string[] args) =>
            Host.CreateDefaultBuilder(args)
                .ConfigureWebHostDefaults(webBuilder =>
                {
                    webBuilder.UseStartup<Startup>();
                    webBuilder.UseUrls($"http://localhost:5000{LocalIpAddressString}");
                });
    }
}
