import nltk

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

    print("input query:", raw_text)
    print("parsed input:", raw_text.split())
    for word in raw_text.split():
        if match(word, "weather", "raining", "snowing", "sunny"):
            return "Weather keyword found"
        elif match(word, "hello", "hi", "hey", "yo"):
            return "Greeting found"
        else:
            return "No keyword matched."

    #return "nouns: {}\nverbs: {}".format(nouns, verbs)

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
