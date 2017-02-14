# Using the Weather package

The `/whisk.system/weather` package offers a convenient way to call the IBM Weather Insights API.

The package includes the following action.

| Entity | Type | Parameters | Description |
| --- | --- | --- | --- |
| `/whisk.system/weather` | package | apiKey | Services from IBM Weather Insights API  |
| `/whisk.system/weather/forecast` | action | apiKey, latitude, longitude, timePeriod | forecast for specified time period|

While not required, it's suggested that you create a package binding with the `apiKey` value. This way you don't need to specify the key every time you invoke the actions in the package.

## Getting a weather forecast for a location

The `/whisk.system/weather/forecast` action returns a weather forecast for a location by calling an API from The Weather Company. The parameters are as follows:

- `apiKey`: An API key for The Weather Company that is entitled to invoke the forecast API.
- `latitude`: The latitude coordinate of the location.
- `longitude`: The longitude coordinate of the location.
- `timeperiod`: Time period for the forecast. Valid options are '10day' - (default) Returns a daily 10-day forecast , '24hour' - Returns an hourly 2-day forecast, , 'current' - Returns the current weather conditions, 'timeseries' - Returns both the current observations and up to 24 hours of past observations, from the current date and time. 


Here is an example of creating a package binding and then getting a 10-day forecast.

1. Create a package binding with your API key.

  ```
  $ wsk package bind /whisk.system/weather myWeather --param apiKey 'MY_WEATHER_API'
  ```

2. Invoke the `forecast` action in your package binding to get the weather forecast.

  ```
  $ wsk action invoke myWeather/forecast --blocking --result --param latitude '43.7' --param longitude '-79.4'
  ```

  ```
  {
      "forecasts": [
          {
              "dow": "Wednesday",
              "max_temp": -1,
              "min_temp": -16,
              "narrative": "Chance of a few snow showers. Highs -2 to 0C and lows -17 to -15C.",
              ...
          },
          {
              "class": "fod_long_range_daily",
              "dow": "Thursday",
              "max_temp": -4,
              "min_temp": -8,
              "narrative": "Mostly sunny. Highs -5 to -3C and lows -9 to -7C.",
              ...
          },
          ...
      ],
  }
  ```

