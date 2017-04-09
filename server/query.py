import nltk
import json

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

    tokenized = raw_text.split()

    response["text"] = "Server received request: [q={}] [l={}] [t={}]".format(raw_text, location, timestamp)
    response["command"] = None

    # Convert response dict into JSON string and return
    response_str = json.dumps(response)
    return response_str

def parse_voice(form):
    pass

def match(word, *keywords):
    """Returns true if word is sufficiently close to any keyword.

    Args:
        word: word to test
        *keywords: a variable number of pre-defined keywords

    Returns:
        true if word sufficiently matches any keyword, false otherwise
    """
    for k in keywords:
        if word == k:
            return True
    return False
