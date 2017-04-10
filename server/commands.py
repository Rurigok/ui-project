import weather
import conversation

command_table = {
    "how's the weather": weather.execute_weather_command,
    "what's the weather like": weather.execute_weather_command,

    "hello": conversation.greeting,
    "hi": conversation.greeting,
    "hey": conversation.greeting,
    "hola": conversation.greeting,
    "what's up": conversation.greeting,
    "yo": conversation.greeting,

    "bye": conversation.farewell,
    "cya": conversation.farewell,
    "later": conversation.farewell,
}
