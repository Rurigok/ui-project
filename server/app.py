from flask import Flask
from flask import request

import sys
import query

app = Flask(__name__)

@app.route("/")
def home():
    return "Looks like you're lost, friend!"

@app.route("/q/<q>")
def text_query(q):
    return query.parse_query(q, None, None, None)

@app.route("/q", methods=["POST"])
def text_query_post():
    query_text = request.form["q"]
    location = request.form["l"]
    timestamp = request.form["t"]
    session_token = request.form["session_token"]
    return query.parse_query(query_text, location, timestamp, session_token)

@app.route("/vq", methods=["POST"])
def voice_query_post():
    return query.parse_voice(request.form)

if __name__ == '__main__':

    if sys.version_info < (3, 2):
        print("Must use python 3.2 or greater.")
        exit()

    app.run(port=4567)
