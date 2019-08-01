# Battleship
The classic guessing game for 2 players. (Java)

The game is played on ruled rectangle grids on which each player's fleet of ships are marked. The locations of the fleets are concealed from the other player. Players alternate turns calling "shots" at the other player's ships, and the objective of the game is to destroy the opposing player's fleet. (from Wikipedia)

This version consists of the following **phases:**

1) Initialization: At the start of a new game, the user must set the grid size (it will be the same for both players). Both rows and columns must be between 10 and 15. In addition, he decides if the game will be played until the end or for a specific amount of rounds.
Finally, he has to choose one of three modes (Player vs Player, Player vs Computer, Computer vs Computer).

2) Ship Deployment: Each player can choose to deploy their ships manually or automatically (obviously a computer-controlled player always deploys automatically). In manual deployment, for each ship, the player must choose a square for the front and the direction (horizontal or vertical). The ships can be adjacent to each other but they cannot collide or be placed outside of the grid. Each square has a unique letter for its row and a unique number for its column. In order to select a square, the player must type the corresponding letter and number (without space in between).

3) Battle: After the ships have been positioned, both boards get concealed and the game proceeds in a series of rounds. In each round, each player takes a turn to announce a target square in the opponent's grid, in an attempt to identify a location that contains a ship.
If the shot manages to hit an opposing ship, the game returns a message without giving away the type of the ship. When all of the squares of a ship have been hit, the type of the ship is revealed and the player gains points depending on it (as seen below). After the last round, the player with the most points wins the game. Of course the game can end earlier if someone's fleet has been completely destroyed.

In this version, each player's fleet consists of 7 ships, with different lengths and values:
* 2 Aircraft Carriers (Length: 5) - 5 Points
* 3 Destroyers (Length: 3) - 2 Points
* 2 Submarines (Length: 1) - 3 Points


**Special commands:** A player can type in one of the following special commands instead of taking their turn:

* Save: Saves the current state of the game in the specified '.txt' file.
* Load: Loads an existing game from the specified '.txt' file.
* Help: Displays a text with a brief explanation of the game and its functions.
* Peek: Displays both players' fields unfiltered (useful for revising).
* Exit: Closes the game.


**Location marks:** Each individual square in a board is represented by an object of the class Location. These objects contain a char variable that indicates the state of the square. More specifically:

* . : The location is empty. Also, when the concealed board is shown, it may contain a ship but it has not been checked.
* o : It has been checked but it was empty.
* x : There is a ship that has been hit, but is not yet sinking (it doesn't reveal the type).
* A/D/S : The ship is sinking. The letter depends on the type.
* a/d/s : Only visible in manual deployment, peek mode and final reveal. There is a ship but it has not been hit. 


**Computer's strategy:** Initially, the first strategy for a computer-controlled player was to make shots totally at random. 	
As expected, this strategy produces very poor results. Games take a long time to complete, as the majority of squares have to be hit in order to ensure that all the ships are sunk. A visibly better strategy (the one that has been implemented in this program) is to simulate the human logic at some extent. At first, the shots will be fired randomly, just as before. But once a part of the ship has been hit, it is only logical to search its adjacent squares trying to strike the rest of it. In order to achieve that, each (computer-controlled) player has a list with potential targets. After a hit, all four surrounding grid squares are added to the list. During its next turn, the computer prioritizes these targets. Once there are no more targets in the list, the shots are fired randomly again. It is important to mention that this algorithm does not check whether a ship has been entirely destroyed. As a result, it has to check every possible adjacent square before moving on and make sure that no other ship is next to the one that was targeted at the beginning. This strategy is still far from perfect but it produces much better results than the "totally random" one. As an experiment, I simulated 1000 games (for both strategies) between 2 computer players in a 10x10 grid. On average, each game played with the first strategy required 94 rounds to be completed, but with the second one, that number dropped to 71. Finally, I ran an additional simulation of more than 10000 games, in which each player used a different strategy. As expected, the second one was emerged victorious in just over 90% of the cases.
