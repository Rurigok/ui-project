import requests

def forecast():
    response = requests.get("http://api.wunderground.com/api/ce2b0073d2591e60/forecast/q/TX/San_Antonio.json")
    data = response.json()
    forecast_str = "Here's a forecast for today:\n" + data['forecast']['txt_forecast']['forecastday'][0]['fcttext']
    return forecast_str

def forecastMetric():
    response = requests.get("http://api.wunderground.com/api/ce2b0073d2591e60/forecast/q/TX/San_Antonio.json")
    data = response.json()
    forecast_str = "Here's a forecast for today:\n" + data['forecast']['txt_forecast']['forecastday'][0]['fcttext_metric']
    return forecast_str
