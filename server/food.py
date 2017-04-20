import requests
import random

def findRestaurant():
	url = "https://developers.zomato.com/api/v2.1/search?entity_id=304&entity_type=city&collection_id=1"
	headers = {'user-key' : '2925f77ef2df77a5c91cce6bc9da4923'}
	response = requests.get(url, headers=headers)
	data = response.json()
	restNo = random.randint(0, 30)
	restaurantName = data['restaurants'][restNo]['restaurant']['name']
	restaurantCuis = data['restaurants'][restNo]['restaurant']['cuisines']
	restaurantAddr = data['restaurants'][restNo]['restaurant']['location']['address']
	returnStr = "How about " + restaurantName + " a " + restaurantCuis + " restaurant located at " + restaurantAddr + "!"
	return returnStr
