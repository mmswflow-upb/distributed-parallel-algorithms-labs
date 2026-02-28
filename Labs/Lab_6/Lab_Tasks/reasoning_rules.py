"""
reasoning_rules.py
------------------
Exercises 2 & 3: rule engine for 10 diseases.

The rules are stored in a CSV, one row per disease.
Each disease has threshold ranges for the categories:
  emergency, warning, normal, below_normal
If no category matches -> none

If more than one category matches, the MOST severe is chosen:
emergency > warning > normal > below_normal > none
"""

from __future__ import annotations
from dataclasses import dataclass
from typing import Dict, Optional, List, Tuple
import csv
from pathlib import Path

SEVERITY_ORDER = ["emergency", "warning", "normal", "below_normal", "none"]


@dataclass(frozen=True)
class Env:
    temperature: float
    humidity: float
    illumination: float
    noise_level: float


def _parse_float(value: str) -> Optional[float]:
    value = (value or "").strip()
    if value == "":
        return None
    return float(value)


def _in_range(x: float, min_v: Optional[float], max_v: Optional[float]) -> bool:
    if min_v is not None and x < min_v:
        return False
    if max_v is not None and x > max_v:
        return False
    return True


def _category_matches(row: Dict[str, str], category: str, env: Env) -> bool:
    return all(
        [
            _in_range(env.temperature, _parse_float(row.get(f"{category}_temp_min")), _parse_float(row.get(f"{category}_temp_max"))),
            _in_range(env.humidity, _parse_float(row.get(f"{category}_humidity_min")), _parse_float(row.get(f"{category}_humidity_max"))),
            _in_range(env.illumination, _parse_float(row.get(f"{category}_illum_min")), _parse_float(row.get(f"{category}_illum_max"))),
            _in_range(env.noise_level, _parse_float(row.get(f"{category}_noise_min")), _parse_float(row.get(f"{category}_noise_max"))),
        ]
    )


def load_rules_from_csv(csv_path: str | Path) -> List[Dict[str, str]]:
    csv_path = Path(csv_path)
    if not csv_path.exists():
        raise FileNotFoundError(f"CSV file not found: {csv_path}")
    with open(csv_path, "r", encoding="utf-8", newline="") as f:
        reader = csv.DictReader(f)
        rows = list(reader)
    if not rows:
        raise ValueError("CSV has no rows.")
    if "disease" not in rows[0]:
        raise ValueError("CSV must include a 'disease' column.")
    return rows


def evaluate_disease(row: Dict[str, str], env: Env) -> Tuple[str, str]:
    name = row["disease"]
    for sev in SEVERITY_ORDER[:-1]:
        if _category_matches(row, sev, env):
            return name, sev
    return name, "none"


def evaluate_all(rows: List[Dict[str, str]], env: Env) -> List[Tuple[str, str]]:
    return [evaluate_disease(r, env) for r in rows]


def most_severe(results: List[Tuple[str, str]], top_k: int = 3) -> List[Tuple[str, str]]:
    idx = {s: i for i, s in enumerate(SEVERITY_ORDER)}
    scored = sorted(results, key=lambda x: idx[x[1]])
    non_none = [r for r in scored if r[1] != "none"]
    if non_none:
        return non_none[:top_k]
    return scored[:top_k]
