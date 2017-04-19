import weather
import jokes
import conversation

KEYWORDS = {
    ("weather", "rain", "snow", "jacket", "sunny", "hot", "cold"): weather.forecast,
    ("hi", "hello", "hey", "greetings", "yo", "sup"): conversation.greeting,
    ("bye", "farwell", "cya", "later", "goodbye"): conversation.farewell,
    ("help"): conversation.help,
    ("joke"): jokes.crackJoke,
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

    "help": conversation.help,
    "what can you do": conversation.help,
    "help me": conversation.help,
    "what can i do": conversation.help,

    "tell me a joke": jokes.crackJoke,
    "joke": jokes.crackJoke,
    "show me a joke": jokes.crackJoke,
}
