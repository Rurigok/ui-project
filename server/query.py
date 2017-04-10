import json
import commands

command_table = {}

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

    # Register command modules
    commands = commands.command_table
    # End command module registration

    handled = False
    for token in tokenized:
        for keyword, command_func in command_table:
            if token.lower() == keyword:
                response_text = command_func(raw_text)
                handled = True

    if not handled:
        response_text = default_handler(response, raw_text)

    response["text"] = "Server received request: [q={}] [l={}] [t={}]".format(raw_text, location, timestamp)
    response["command"] = None

    # Convert response dict into JSON string and return
    response_str = json.dumps(response)
    return response_str

def parse_voice(form):
    pass
