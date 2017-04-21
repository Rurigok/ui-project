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
                    "What do ghosts drink when they're sad? Boos",
                    "What's the least spoken language in the world? Sign language",
                    "Egyptologists have trouble identified mummies because of their encryption",
                    "I drank a bottle of food coloring the other day. I think I dyed a little inside",
                    "You know what's the best part about being object oriented? I get a lot of inheritance!",
                    "I used to want to be an archaeologist, but I didn't want my life to be in ruins"]
    return random.choice(jokes)

def lenny():
    return "( ͡° ͜ʖ ͡°)"

def fightClub():
    return "Don't talk about Fight Club"

def konami():
    return "30 bonus lives unlocked \n Just kidding I like your life just the way it is 💙"
