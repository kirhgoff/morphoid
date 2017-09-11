Morphoid
========

Player's spaceship crashes on a planet, inhabited by creatures with very unusual genetic laws. He learns to control them and use to his advantage.

Changing the biological forms of that planet on his way back home he learns things that change his understanding of the world.

Story
-----
* Player wakes up and looks around, finds broken ship, learns how to survive
* Creatures on the planet consist of huge cells that have specific functions with generations rapidly change genes
* He finds alien artefact which can manipulate cell's genes and learns how to modify creatures to build instruments
* He notices that planet is damaged by mystical threat, changing the fauna to complete annihilation
* He says the planet and faces himself on the other side of the battle
* He is in asylum recovering from madness (or something else)

Fauna
-----
Important feature of the game: creatures are presented as "fair" neural networks and the game world uses genetic approach to develop the state of the world's creatures. They learn to adopt to player's actions.

"Cells" are not the cells that we know as cells, this is part of fantasies of main hero, so game is not very specific how this all works.

Types(sample)
-------------
[Shrooms] - constantly growing by themselves, providing small amounts of energy. Cells: seed (likes darkness). 5 energy

[Ooze] - eat Shrooms and produce, energy decays. Cells: seed, sight, move, reproduce x2, consume(shrooms). 100 energy.

[Monster] - eat Ooze and produce, energy decays; have high sight and move faster. Cells: seed, sight x10, move x5, reproduce, consume(shrooms, ooze). 30 energy

TO_ADD: [Krevetko] - live on corpses, eatable with high energy.

There are sunny and dark areas, affecting growth of shrooms and speed of corpses decay

All creatures have a cell called Seed. It contains full information about creature and it is possible to use seed to reproduce creature.

Day and night
-------------

Day and night changes in the game world make all creatures die leaving only seeds, reproducing themselves in the morning. Some "magical laws" during the night related seeds can cross-over and/or mutate.

NOTICE: it is very possible it will be hard to find that balanced set of parameters that populations of creatures somehow pulsate of stable balance. It could be probably very big part of development.

Single player mode could use set of predefined parameter sets, prepared with the balance. The interesting part of the game should be the way player distorts the balance. Story could use night changes in the game laws to create different situations for player.

Manipulations
-------------
Player can reproduce creatures from altered Seeds using specific instruments. Spectre of his abilities will increase with the story - he will be finding new modifications of Seed gene that regulates how it is created.

Player can enter creatures (e.g. damaging the cell with time) and ride them

Constructions
-------------
[Gun] - protects area from any creatures. Consumes energy
[Car] - very fast creature with cell for transportation. Can shoot (Gun). Player can breed these cars and collect their seeds. Needs energy.
[Polar battery] - set of cells that produces energy.
[Contact][Wire] - attaches energy producer to energy consumer
[Seed transformer] - accepts a seed and allows user to open UI with gene editing tools. Gene manipulation consumes energy.

ASCII UI
--------
For simplicity in MVP game world is 2d and presented with ASCII characters board. Cell is presented with one char. All movements are discreet.



