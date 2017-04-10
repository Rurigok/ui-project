import requests

def forecast():
    response = requests.get("http://api.wunderground.com/api/ce2b0073d2591e60/forecast/q/TX/San_Antonio.json")
    data = response.json()
    forecast = data['forecast']['txt_forecast']['forecastday'][0]['fcttext']
    return forecast

def forecastMetric():
    response = requests.get("http://api.wunderground.com/api/ce2b0073d2591e60/forecast/q/TX/San_Antonio.json")
    data = response.json()
    forecast = data['forecast']['txt_forecast']['forecastday'][0]['fcttext_metric']
    return forecast
