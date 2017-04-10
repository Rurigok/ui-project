import random

def crackJoke():
    noJokes = 11 #Number of jokes currently in array
    jokes = ["Don't spell part backwards. It's a trap!",
                    "What's a pirate's favorite wavelength? Infrared. IR. Aye. Arrgh.",
                    "What do you call the security outside of a Samsung store? Guardians of the Galaxy",
                    "Why do fish live in salt water? Because pepper makes them sneeze",
                    "I was going to make a joke about sodium, but Na",
                    "Want to hear a joke about pizza? Never mind, it's too cheesy...",
                    "What do we want? Low-flying airplane noises! When do we want them? Neeeeeyoooow!",
                    "19 and 20 had a fight. 21",
                    "I have a horse named Mayo. Sometimes Mayo neighs!",
                    "A man walks into a bar and asks the bartender if they have any helicopter flavored chips. The bartender responds \"Sorry sir, we only have plane\"",
                    "I used to want to be an archaeologist, but I didn't want my life to be in ruins"]
    return random.choice(jokes)
