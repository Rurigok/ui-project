from flask import Flask
from flask import request

import intella

app = Flask(__name__)

@app.route("/")
def home():
    return "Looks like you're lost, friend!"

@app.route("/response/<text>", methods=["GET"])
def respond_get(text):
    return intella.parse_input(text)

@app.route("/response", methods=["POST"])
def respond_post():
    return intella.parse_input(request.form("text"))

if __name__ == '__main__':
    app.run()
