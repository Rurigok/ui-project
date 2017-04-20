class Message:

    was_keyword_matched = False
    matched_keyword = None
    similarity = 9999
    trigger_phrase = None
    triggered_func = None
    server_response = ""

    def __init__(self, user_message):
        self.user_message = user_message

    def set_keyword_match(self, keyword):
        self.was_keyword_matched = True
        self.matched_keyword = keyword

    def set_similarity(self, distance, phrase):
        self.similarity = distance
        self.trigger_phrase = phrase

    def __repr__(self):
        return "[{} : {}]".format(self.user_message, self.server_response)
