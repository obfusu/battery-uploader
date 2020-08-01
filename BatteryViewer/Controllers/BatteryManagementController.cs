using BatteryViewer.Models;

using Microsoft.AspNetCore.Mvc;

namespace BatteryViewer.Controllers
{
    [ApiController]
    [Route("api/battery")]
    public class BatteryManagementController : ControllerBase
    {
        private BatteryState batteryState;

        public BatteryManagementController(BatteryState batteryState)
        {
            this.batteryState = batteryState;
        }

        [HttpGet]
        public BatteryState Get()
        {
            return batteryState;
        }

        [HttpGet("report")]
        public BatteryState Set([FromQuery(Name = "battery")] string batteryPercentage)
        {
            if (int.TryParse(batteryPercentage, out var percentage))
            {
                batteryState.Percentage = percentage;
            }
            return batteryState;
        }
    }
}
