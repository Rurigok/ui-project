import nltk

def parse_query(raw_text, location, timestamp):
    return str(nltk.pos_tag(nltk.word_tokenize(raw_text)))

def parse_voice(form):
    pass
