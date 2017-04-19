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
        "I can tell you a joke.",
    ]

    return "\n".join(help_strs)

def thanks():

    thankyou_strs = [
        "You're welcome!",
        "No problem!",
        "Pleasure to serve you :)",
        "I'm glad I could help!"
    ]

    return random.choice(thankyou_strs)

def analyze(session_history):
    # retrieve last message from session history
    if len(session_history) == 0:
        return "I don't have any messages to analyze!"
    last_message = session_history[-1]

    response_str = "Your last query was \"{}\".\n".format(last_message.user_message)

    if last_message.was_keyword_matched:
        response_str = response_str + "The keyword \"{}\" was matched in your command.\n"\
                                      .format(last_message.matched_keyword)
    else:
        response_str = response_str + "No keyword was matched in your command.\n"

    response_str = response_str + "Closest matched rule phrase was \"{}\".\n"\
                                  .format(last_message.trigger_phrase)
    response_str = response_str + "Similarity value was {}.\n".format(last_message.similarity)

    response_str = response_str + "Handler function called was {}."\
                                  .format(last_message.triggered_func)
    return response_str
