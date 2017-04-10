import weather
import jokes
import conversation

command_table = {
    "how's the weather": weather.forecast,
    "what's the weather like": weather.forecast,

    "hello": conversation.greeting,
    "hi": conversation.greeting,
    "hey": conversation.greeting,
    "hola": conversation.greeting,
    "what's up": conversation.greeting,
    "yo": conversation.greeting,

    "bye": conversation.farewell,
    "cya": conversation.farewell,
    "later": conversation.farewell,

    "tell me a joke": jokes.crackJoke,
    "joke": jokes.crackJoke,
}
