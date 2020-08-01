using Newtonsoft.Json;

namespace BatteryViewer.Models
{
    public class BatteryState
    {
        [JsonProperty("percentage")]
        public int Percentage { get; set; }
    }
}
