"""
model.py
--------
Exercise 1: Implement the model classes from the diagram (Lab 6).

Classes:
Factor -> Person, HomeAppliance, Environment -> Internal, Weather
VirtualSpace aggregates persons, appliances, environment
Reasoning + DBConnection stubs
"""

from __future__ import annotations
from dataclasses import dataclass, field
from typing import List, Optional, Dict, Any
import time
import uuid


@dataclass
class Factor:
    id: str = field(default_factory=lambda: str(uuid.uuid4()))
    name: str = "Factor"
    status: str = "unknown"
    timestamp: float = field(default_factory=time.time)

    def show(self) -> None:
        print(f"id: {self.id}; name: {self.name}; status: {self.status}; time: {self.timestamp}")


@dataclass
class Disease:
    name: str
    severity: str  # emergency | warning | normal | below_normal | none

    def __str__(self) -> str:
        return f"{self.name} ({self.severity})"


@dataclass
class Person(Factor):
    habit: str = "unknown"
    location: str = "unknown"
    disease: Optional[Disease] = None

    def move(self, new_location: str) -> None:
        self.location = new_location
        self.status = "moved"

    def get_in(self) -> None:
        self.status = "inside"

    def get_out(self) -> None:
        self.status = "outside"

    def use(self, appliance_name: str) -> None:
        self.status = f"using {appliance_name}"


@dataclass
class HomeAppliance(Factor):
    location: str = "unknown"
    effect_level: int = 0

    def set_status(self, status: str) -> None:
        self.status = status


@dataclass
class Environment(Factor):
    temperature: float = 20.0
    humidity: float = 40.0
    illumination: float = 300.0
    noise_level: float = 30.0

    def get_environment_info(self) -> Dict[str, Any]:
        return {
            "temperature": self.temperature,
            "humidity": self.humidity,
            "illumination": self.illumination,
            "noise_level": self.noise_level,
        }

    def print_environment_info(self) -> None:
        info = self.get_environment_info()
        print(
            "temperature: {temperature}; humidity: {humidity}; illumination: {illumination}; noiseLevel: {noise_level}".format(
                **info
            )
        )


@dataclass
class Internal(Environment):
    size: float = 50.0

    def get_environment_from_appliance_effect(self, appliances: List[HomeAppliance]) -> None:
        # Minimal plausible behavior: "on" appliances reduce noise slightly.
        for a in appliances:
            if a.status.lower() in {"on", "running"}:
                self.noise_level = max(0.0, self.noise_level - min(5.0, a.effect_level * 0.05))


@dataclass
class Weather(Environment):
    level: str = "clear"

    def set_effect(self) -> None:
        lvl = self.level.lower()
        if lvl == "rainy":
            self.humidity = min(100.0, self.humidity + 20.0)
        elif lvl == "hot":
            self.temperature += 5.0
        elif lvl == "cold":
            self.temperature -= 5.0


@dataclass
class VirtualSpace:
    size: float
    location: str
    persons: List[Person] = field(default_factory=list)
    appliances: List[HomeAppliance] = field(default_factory=list)
    environment: Environment = field(default_factory=Environment)
    factors: Optional[List[Factor]] = None

    def show(self) -> None:
        print(f"size: {self.size}; location: {self.location}; factors: {self.factors}")

    def get_event(self) -> str:
        return "event_not_implemented"


class DBConnection:
    def __init__(self, connection_string: str):
        self.connection_string = connection_string

    def read(self) -> None:
        return

    def write(self) -> None:
        return

    def close(self) -> None:
        return


class Reasoning:
    def __init__(self, db_connection: DBConnection, ref_smart_home: VirtualSpace):
        self.db_connection = db_connection
        self.ref_smart_home = ref_smart_home

    def get_cases(self) -> None:
        return

    def do_reasoning(self) -> None:
        return

    def case_matching(self) -> None:
        return

    def get_environment_info(self) -> Environment:
        return self.ref_smart_home.environment
