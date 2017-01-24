import nltk

def parse_input(raw_text):
    return str(nltk.pos_tag(nltk.word_tokenize(raw_text)))
