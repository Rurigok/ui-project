import weather
import jokes
import conversation
import query
import food

KEYWORDS = {
    ("weather", "rain", "snow", "jacket", "sunny", "hot", "cold"): weather.forecast,
    ("hi", "hello", "hey", "greetings", "yo", "sup"): conversation.greeting,
    ("bye", "farwell", "cya", "later", "goodbye"): conversation.farewell,
    ("help"): conversation.help,
    ("joke"): jokes.crackJoke,
    ("restaurant", "food", "hungry"): food.findRestaurant
}

PHRASES = {
    "how's the weather": weather.forecast,
    "what's the weather like": weather.forecast,
    "weather": weather.forecast,
    "need a jacket": weather.forecast,
    "how hot is it": weather.forecast,
    "show me the weather": weather.forecast,
    "is it raining": weather.forecast,
    "how cold is it outside": weather.forecast,
    "is it snowing": weather.forecast,

    "hello": conversation.greeting,
    "hi": conversation.greeting,
    "hey": conversation.greeting,
    "hola": conversation.greeting,
    "what's up": conversation.greeting,
    "yo": conversation.greeting,
    "hey there": conversation.greeting,
    "hi there": conversation.greeting,
    "greetings": conversation.greeting,
    "how do you do": conversation.greeting,
    "how are you": conversation.greeting,

    "bye": conversation.farewell,
    "cya": conversation.farewell,
    "later": conversation.farewell,
    "goodbye": conversation.farewell,
    "that'll be all": conversation.farewell,
    "that's all": conversation.farewell,

    "help": conversation.help,
    "what can you do": conversation.help,
    "help me": conversation.help,
    "what can i do": conversation.help,

    "what's your name": conversation.name,
    "what do I call you": conversation.name,
    "name": conversation.name,
    "what is your name": conversation.name,

    "thanks": conversation.thanks,
    "thank you": conversation.thanks,

    "tell me a joke": jokes.crackJoke,
    "joke": jokes.crackJoke,
    "show me a joke": jokes.crackJoke,

    "analysis": conversation.analyze,
    "why did you say that": conversation.analyze,
    "analyze last command": conversation.analyze,

    "what should I eat today": food.findRestaurant,
    "what's a good place to eat": food.findRestaurant,
    "what's a good restaurant around me": food.findRestaurant,
    "food": food.findRestaurant,
    "hungry": food.findRestaurant
    "restaurant": food.findRestaurant
    "eat": food.findRestaurant
    "i'm hungry": food.findRestaurant,
    "where can i eat": food.findRestaurant
}
