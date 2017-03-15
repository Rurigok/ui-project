import nltk

def parse_query(raw_text, location, timestamp):
    tags = nltk.pos_tag(nltk.word_tokenize(raw_text))
    # Parse nouns
    nouns = []
    verbs = []

    for pair in tags:
        word = pair[0]
        part_of_speech = pair[1]
        if part_of_speech.startswith("N"):
            nouns.append(word)
        if part_of_speech.startswith("V"):
            verbs.append(word)

    return "nouns: {}\nverbs: {}".format(nouns, verbs)

def parse_voice(form):
    pass
