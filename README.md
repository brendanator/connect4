Connect4
========

Features
--------

* Play against the computer as red or yellow
* Make the computer play against itself
* Different difficulty setting
* Take back moves

Implementation
--------------

* Negamax with alpha-beta pruning
* Zobrist hashing
* Positions represented by longs for efficient storage and bit manipulation

Evaluation function
-------------------

The following positions are evaluated. They are shown in order or descending weight

1. Four in a row 
2. Double threat 
3. Zugwang 
4. Threat count
5. Space
