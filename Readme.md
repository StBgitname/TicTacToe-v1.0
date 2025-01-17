# Tic Tac Toe with simple Reinforcement Learning algorithm

This is a small project to get acquainted with the Q-Learning algorithm.


## Features

- GUI
- A human player can play against an AI agent.
- after each game, the AI stores the new Q-Values in a csv-file
- training of the AI by playing multiple games against an automated opponent
- automated opponent can be random or advanced (using the same Q-values as the learning AI - currently not working properly)
- all comments are in german :)

## ToDo

- compare Board states to account for mirrored and rotated states
  (This would greatly reduce the size of the stored data and lead to more efficient learning)
- fix advanced AI issue
- random starting player
