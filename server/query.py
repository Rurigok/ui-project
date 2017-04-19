import json
from commands import KEYWORDS, PHRASES
from nltk.metrics.distance import edit_distance

def parse_query(raw_text, location, timestamp):
    # tags = nltk.pos_tag(nltk.word_tokenize(raw_text))
    # nouns = []
    # verbs = []
    #
    # for pair in tags:
    #     word = pair[0]
    #     part_of_speech = pair[1]
    #     if part_of_speech.startswith("N"):
    #         nouns.append(word)
    #     if part_of_speech.startswith("V"):
    #         verbs.append(word)

    response = {} # build response dict (to be returned as JSON)

    print("input query:", raw_text)
    print("parsed input:", raw_text.split())

    min_dist = 9999
    handled = False

    for phrase in PHRASES:
        dist = edit_distance(raw_text.lower(), phrase)
        #print("Comparing: ", raw_text, key, "=", dist)
        if dist < min_dist:
            min_dist = dist
            command_func = PHRASES[phrase]
            handled = True

    if not handled:
        response_text = "Sorry, I didn't quite understand that!"
    else:
        response_text = command_func()

    response["text"] = response_text
    response["command"] = None

    # Convert response dict into JSON string and return
    response_str = json.dumps(response)
    return response_str

def parse_voice(form):
    pass
