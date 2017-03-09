from flask import Flask
from flask import request

import intella

app = Flask(__name__)

@app.route("/")
def home():
    return "Looks like you're lost, friend!"

@app.route("/q/<text>", methods=["GET"])
def text_query_get(text):
    return intella.parse_input(text)

@app.route("/q", methods=["POST"])
def text_query_post():
    return intella.parse_input(request.form("q"))

@app.route("/vq", methods=["POST"])
def voice_query_post():
    return intella.parse_voice(request.form)

if __name__ == '__main__':
    app.run()
