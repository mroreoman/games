Wordle dictionary from [scholtes on github](https://gist.github.com/scholtes/94f3c0303ba6a7768b47583aff36654d#file-wordle-la-txt)
=================

Wordle is a web browser game that you can play [at this link](https://www.powerlanguage.co.uk/wordle/). You get 6 chances to guess a single 5 letter word and there is only 1 word per day.

This gist contains the contents of the dictionary Wordle uses as of January 27th, 2022. Wordle actually uses two dictionaries:

- `La` words that can be guessed and which can be the word of the day  
- `Ta` words that can be guessed but are never selected as the word of the day  

`La` contains 2,315 words, `Ta` contains 10,657 words. The lists are sorted alphabetically and only contain unique words. No word shows up in both lists, making for a total of 12,972 which can be guessed.

---

**Caution!** The word list `La` is sorted alphabetically here in this gist. However, Wordle's client-side JavaScript sorts the list in chronological order. Watch out for spoilers if you inspect the game's source!  