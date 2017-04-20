import json
import conversation
from commands import KEYWORDS, PHRASES
from message import Message
from nltk.metrics.distance import edit_distance

sessions = {}

def parse_query(raw_text, location, timestamp, session_token):

    print("-------------------------------------")

    session_token = str(session_token)

    # Handle session
    if session_token not in sessions:
        print("Creating new session list")
        sessions[session_token] = list()

    print("Retrieving session history")
    session_history = sessions[session_token]

    response = {} # build response dict (to be returned as JSON)
    message = Message(raw_text) # meta message object for logging

    print("input query:", raw_text)
    print("session token:", session_token)

    min_dist = 9999
    handled = False

    for phrase in PHRASES:
        dist = edit_distance(raw_text.lower(), phrase)
        #print("Comparing: ", raw_text, key, "=", dist)
        if dist < min_dist:
            min_dist = dist
            command_func = PHRASES[phrase]
            handled = True
            message.set_similarity(dist, phrase)

    if not handled:
        response_text = "Sorry, I didn't quite understand that!"
    else:
        if command_func == conversation.analyze:
            response_text = command_func(session_history)
        else:
            response_text = command_func()

    response["text"] = response_text
    response["command"] = None

    # Store this message into session history
    message.server_response = response_text
    message.triggered_func = str(command_func)
    session_history.append(message)

    print("-------------------------------------")

    # Convert response dict into JSON string and return
    response_str = json.dumps(response)
    return response_str

def parse_voice(form):
    pass
