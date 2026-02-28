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
from reasoning_rules import Env, load_rules_from_csv, evaluate_all, most_severe

try:
    import speech_recognition as sr
    import pyttsx3
except ImportError:
    sr = None
    pyttsx3 = None


def speak(text: str) -> None:
    if pyttsx3 is None:
        print("[TTS not installed] " + text)
        return
    engine = pyttsx3.init()
    engine.say(text)
    engine.runAndWait()


def listen_once(recognizer: "sr.Recognizer") -> str:
    if sr is None:
        raise RuntimeError("SpeechRecognition is not installed.")
    with sr.Microphone() as source:
        recognizer.adjust_for_ambient_noise(source, duration=0.2)
        audio = recognizer.listen(source)
    return recognizer.recognize_google(audio).lower()


def extract_number_after(words: list[str], keyword: str, default: float) -> float:
    for i, w in enumerate(words):
        if w == keyword and i + 1 < len(words):
            try:
                return float(words[i + 1])
            except ValueError:
                return default
    return default


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--csv", default="data/diseases_rules.csv", help="Path to CSV rules file")
    args = parser.parse_args()

    rows = load_rules_from_csv(args.csv)

    print("Voice bot started.")
    print("Say: 'temperature 32 humidity 60 illumination 200 noise 70'")
    print("Say: 'quit' to stop.\n")

    if sr is None or pyttsx3 is None:
        print("Voice dependencies not installed. Install with:")
        print("  pip install SpeechRecognition pyttsx3 pyaudio")
        print("Falling back to text-only echo. Type 'quit' to stop.\n")
        speak("Voice dependencies are not installed. Falling back to text mode.")
        while True:
            txt = input("> ").strip().lower()
            if txt in {"quit", "exit"}:
                break
            print("Did you say " + txt)
            speak(txt)
        return

    r = sr.Recognizer()

    while True:
        try:
            txt = listen_once(r)
            print("Did you say " + txt)
            speak(txt)

            if "quit" in txt or "exit" in txt:
                speak("Goodbye!")
                break

            words = txt.split()
            env = Env(
                temperature=extract_number_after(words, "temperature", 30.0),
                humidity=extract_number_after(words, "humidity", 50.0),
                illumination=extract_number_after(words, "illumination", 300.0),
                noise_level=extract_number_after(words, "noise", 55.0),
            )

            results = evaluate_all(rows, env)
            top = most_severe(results, top_k=3)

            summary = "Top risks are: " + ", ".join([f"{d} {s}" for d, s in top])
            print(summary)
            speak(summary)

        except Exception as e:
            print(f"Error: {e}")
            speak("Sorry, I could not understand. Please try again.")


if __name__ == "__main__":
    main()
