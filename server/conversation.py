import random

def greeting():

    greetings = [
        "Hi :)",
        "Hey!",
        "Hello!",
    ]

    greeting = random.choice(greetings) + "\n" + "What can I do for you?"
    return greeting

def farewell():

    farewells = [
        "Goodbye!",
        "Bye!",
    ]

    return random.choice(farewells)

def help():

    help_strs = [
        "I can give you the weather forecast for today.",
        "Ask me to tell you a joke.",
    ]

    return "\n".join(help_strs)
