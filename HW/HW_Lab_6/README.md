README.txt (Homework)
=====================

Folder purpose
--------------
This folder contains the Homework solution for Lab 6:
Speech-to-Text (STT) + Text-to-Speech (TTS) bot, using the same disease rules
loaded from a CSV file.

What each file does
-------------------

1) voice_bot.py
   - Main homework program.
   - Uses:
       SpeechRecognition  -> listens to microphone and converts speech to text (STT)
       pyttsx3            -> speaks text out loud (TTS)
   - You speak a phrase like:
       "temperature 32 humidity 60 illumination 200 noise 70"
   - The program:
       (1) repeats what it heard (prints + speaks)
       (2) extracts the numbers after keywords
       (3) evaluates disease severities from the CSV
       (4) prints + speaks the top 3 most severe risks
   - Say “quit” to stop.

   Fallback behavior:
   - If voice libraries aren’t installed, it falls back to text-only echo mode
     (you type, it repeats).

2) reasoning_rules.py
   - Shared rule engine:
     loads CSV rules and evaluates severities for each disease.

3) data/diseases_rules.csv
   - The 10 diseases + threshold ranges (same format as in exercises).

4) requirements_voice.txt
   - Dependencies for voice mode:
       SpeechRecognition
       pyttsx3
       pyaudio

Setup (requirements)
--------------------
- Python 3.9+
- Voice dependencies (for real STT/TTS mode):

  pip install -r requirements_voice.txt

Important note about pyaudio
----------------------------
pyaudio can be difficult to install on some OS setups.
If installing pyaudio fails, you can still run voice_bot.py and it will switch
to text-only fallback mode automatically.

Run steps
---------
1) Open a terminal in THIS folder (Homework).
2) Install dependencies (recommended):

   pip install -r requirements_voice.txt

3) Run:

   python voice_bot.py --csv data/diseases_rules.csv

How to use it
-------------
- Speak clearly:
    "temperature 32 humidity 60 illumination 200 noise 70"
- The bot will respond with the top risks.
- Say:
    "quit"
  to exit.

Common issues
-------------
- Microphone not detected:
  Check OS microphone permissions or run the fallback text-only mode.
- “SpeechRecognition not installed”:
  Run:
    pip install -r requirements_voice.txt
