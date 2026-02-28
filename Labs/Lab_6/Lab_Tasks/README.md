README.txt (Lab_Exercises)
=========================

Folder purpose
--------------
This folder contains the solutions for Lab 6 Exercises 1–3.

What each file does
-------------------

1) model.py  (Exercise 1)
   - Implements the OOP model from the class diagram:
     Factor -> Person, HomeAppliance, Environment -> Internal, Weather
   - VirtualSpace aggregates:
       persons + appliances + environment
   - DBConnection and Reasoning are included as stubs (structure/classes exist).

   In short: this is the “translate the diagram into Python classes” exercise.

2) reasoning_rules.py (Exercise 2)
   - Implements a small rule engine that evaluates disease severity based on
     environment values:
       temperature, humidity, illumination, noise_level
   - Uses severity categories:
       emergency, warning, normal, below_normal, none
   - For each disease, checks which category matches and picks the most severe.

3) bot_cli.py + data/diseases_rules.csv (Exercise 3)
   - A terminal “bot” that:
     (1) loads the 10 diseases + their thresholds from the CSV
     (2) asks you in the terminal for environment values
     (3) prints the severity for each disease
     (4) prints the top most severe results

How the CSV rules work (data/diseases_rules.csv)
------------------------------------------------
- Each row = one disease
- For each severity category (emergency/warning/normal/below_normal),
  the CSV stores min/max ranges for:
    temp, humidity, illumination, noise
- If an environment value is inside all provided ranges for a category,
  that category matches.
- The program chooses the most severe match in this order:
    emergency > warning > normal > below_normal > none

Setup (requirements)
--------------------
- Python 3.9+ (standard library only)

Run steps
---------
1) Open a terminal in THIS folder (Lab_Exercises).
2) Run:

   python bot_cli.py --csv data/diseases_rules.csv

3) Enter values when prompted (or press Enter to use defaults).
4) Read the printed results and the "most severe" top list.

Common issues
-------------
- “CSV file not found”:
  Make sure you run from inside the Lab_Exercises folder and the file exists at:
    data/diseases_rules.csv