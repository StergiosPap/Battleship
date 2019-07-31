# Battleship
The classic guessing game for 2 players. (Java)

The game is played on ruled rectangle grids on which each player's fleet of ships are marked. The locations of the fleets are concealed from the other player. Players alternate turns calling "shots" at the other player's ships, and the objective of the game is to destroy the opposing player's fleet. (from Wikipedia)

This version consists of the following phases:

1) Initialization: At the start of a new game, the user must set the grid size (it will be the same for both players). Both rows and columns must be between 10 and 15. In addition, he decides if the game will be played until the end or for a specific amount of rounds.
Finally, he has to choose one of three modes (Player vs Player, Player vs Computer, Computer vs Computer).

2) Ship Deployment: Each player can choose to deploy their ships manually or automatically (obviously a computer-controlled player always deploys automatically). In manual deployment, for each ship, the player must choose a square for the front and the direction (horizontal or vertical). The ships can be adjacent to each other but they cannot collide or be placed outside of the grid. Each square has a unique letter for its row and a unique number for its column. In order to select a square, the player must type the corresponding letter and number (without space in between).

3) Battle: After the ships have been positioned, both boards get concealed and the game proceeds in a series of rounds. In each round, each player takes a turn to announce a target square in the opponent's grid, in an attempt to identify a location that contains a ship.
If the shot manages to hit an opposing ship, the game returns a message without giving away the type of the ship. When all of the squares of a ship have been hit, the type of the ship is revealed and the player gains points depending on it (as seen below). After the last round, the player with the most points wins the game. Of course the game can end earlier if someone's fleet has been completely destroyed.

In this version, each player's fleet consists of 7 ships, with different lengths and values:
* 2 Aircraft Carriers (Length: 5) - 5 Points
* 3 Destroyers (Length: 3) - 2 Points
* 2 Submarines (Length: 1) - 3 Points


Special commands: A player can type in a special command instead of taking their turn. These commands are the following:

* Save: Saves the current state of the game in the specified '.txt' file.
* Load: Loads an existing game from the specified '.txt' file.
* Help: Displays a text with a brief explanation of the game and its functions.
* Peek: Displays both players' fields unfiltered (useful for revising).
* Exit: Closes the game.
