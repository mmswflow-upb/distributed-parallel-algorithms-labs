"""
voice_bot.py
------------
Homework: Speech-To-Text + Text-To-Speech bot.

- SpeechRecognition for STT (Google recognizer)
- pyttsx3 for offline TTS
- Uses the same CSV rule engine to output top disease risks

Usage:
  python voice_bot.py --csv data/diseases_rules.csv
"""

import argparse
import os
import sys
from reasoning_rules import Env, load_rules_from_csv, evaluate_all, most_severe

try:
    import speech_recognition as sr
    import pyttsx3
except ImportError:
    sr = None
    pyttsx3 = None


def silence_alsa() -> None:
    """Permanently redirect C-level fd 2 to /dev/null to kill ALSA spam.
    Python's sys.stderr is re-pointed to the real terminal so Python
    error messages remain visible."""
    devnull = os.open(os.devnull, os.O_WRONLY)
    real_stderr_fd = os.dup(2)
    os.dup2(devnull, 2)
    os.close(devnull)
    sys.stderr = os.fdopen(real_stderr_fd, "w", buffering=1)


def speak(engine, text: str) -> None:
    if engine is None:
        print("[TTS not installed] " + text)
        return
    engine.say(text)
    engine.runAndWait()


def choose_device_index(prefer: str = "hw:0,7") -> int | None:
    names = sr.Microphone.list_microphone_names()
    for i, n in enumerate(names):
        if prefer.lower() in n.strip().lower():
            return i
    return None


def listen_once(recognizer, device_index) -> str:
    if device_index is None:
        mic = sr.Microphone()
    else:
        mic = sr.Microphone(device_index=device_index)
    with mic as source:
        recognizer.adjust_for_ambient_noise(source, duration=0.2)
        audio = recognizer.listen(source)
    return recognizer.recognize_google(audio).lower()


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--csv", default="data/diseases_rules.csv", help="Path to CSV rules file")
    args = parser.parse_args()

    rows = load_rules_from_csv(args.csv)

    print("Voice bot started.")
    print("Answer each question with a number. Say 'quit' at any prompt to stop.\n")

    if sr is None or pyttsx3 is None:
        print("Voice dependencies not installed. Install with:")
        print("  pip install SpeechRecognition pyttsx3 pyaudio")
        print("Falling back to text-only mode. Type 'quit' to stop.\n")
        params = [
            ("What is the temperature?", 30.0),
            ("What is the humidity?", 50.0),
            ("What is the illumination?", 300.0),
            ("What is the noise level?", 55.0),
        ]
        values = []
        for prompt, default in params:
            print(prompt)
            txt = input("> ").strip().lower()
            if txt in {"quit", "exit"}:
                return
            try:
                values.append(float(txt))
            except ValueError:
                print(f"Not a number, using default {default}.")
                values.append(default)
        env = Env(
            temperature=values[0],
            humidity=values[1],
            illumination=values[2],
            noise_level=values[3],
        )
        results = evaluate_all(rows, env)
        top = most_severe(results, top_k=3)
        summary = "Top risks are: " + ", ".join([f"{d} {s}" for d, s in top])
        print(summary)
        return

    silence_alsa()
    engine = pyttsx3.init()

    r = sr.Recognizer()
    device_index = choose_device_index("hw:0,7")
    if device_index is None:
        print("[WARN] 'hw:0,7' not found; using default mic.")

    params = [
        ("What is the temperature in degrees?", 30.0),
        ("What is the humidity percentage?", 50.0),
        ("What is the illumination level?", 300.0),
        ("What is the noise level?", 55.0),
    ]
    values = []

    for prompt, default in params:
        print(prompt)
        speak(engine, prompt)
        try:
            txt = listen_once(r, device_index)
            print("Heard: " + txt)
            if "quit" in txt or "exit" in txt:
                speak(engine, "Goodbye!")
                return
            number = default
            for w in txt.split():
                try:
                    number = float(w)
                    break
                except ValueError:
                    continue
            values.append(number)
        except Exception as e:
            print(f"Error: {e}")
            print(f"Could not understand. Using default {default}.")
            speak(engine, f"Could not understand. Using default {default}.")
            values.append(default)

    env = Env(
        temperature=values[0],
        humidity=values[1],
        illumination=values[2],
        noise_level=values[3],
    )

    results = evaluate_all(rows, env)
    top = most_severe(results, top_k=3)

    summary = "Top risks are: " + ", ".join([f"{d} {s}" for d, s in top])
    print(summary)
    speak(engine, summary)


if __name__ == "__main__":
    main()
