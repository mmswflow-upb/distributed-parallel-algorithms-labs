"""
bot_cli.py
----------
Exercise 3: bot-like interaction.
Loads 10 diseases from CSV, asks environment values, evaluates severity.
"""

import argparse
from reasoning_rules import Env, load_rules_from_csv, evaluate_all, most_severe


def ask_float(prompt: str, default: float) -> float:
    raw = input(f"{prompt} [{default}]: ").strip()
    if raw == "":
        return float(default)
    return float(raw)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--csv", default="data/diseases_rules.csv", help="Path to CSV rules file")
    args = parser.parse_args()

    rows = load_rules_from_csv(args.csv)

    print("Hi! I'm your Lab 6 health-environment bot.")
    print("I'll ask you for environment values and estimate risk severities.\n")

    temp = ask_float("Temperature (°C)", 30.0)
    hum = ask_float("Humidity (%)", 50.0)
    illum = ask_float("Illumination (lux)", 300.0)
    noise = ask_float("Noise level (dB)", 55.0)

    env = Env(temperature=temp, humidity=hum, illumination=illum, noise_level=noise)

    results = evaluate_all(rows, env)
    top = most_severe(results, top_k=5)

    print("\n== Results ==")
    for disease, sev in results:
        print(f"- {disease}: {sev}")

    print("\n== Most severe (top 5) ==")
    for disease, sev in top:
        print(f"* {disease}: {sev}")

    print("\nTip: get more sunlight and drink more water 🙂")


if __name__ == "__main__":
    main()
