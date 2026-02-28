"""
employee_project_demo.py
------------------------
Implementation from the slides:
- Project (name, budget)
- Employee (name, list of projects)
- Manager & Programmer override calculate_salary()

Salary rules (slides):
- manager: 4000 + compute_total_budget() * 10 / 100
- programmer: 3000 + get_no_projects() * 500
"""

from dataclasses import dataclass, field
from typing import List


@dataclass
class Project:
    name: str = "Proj"
    budget: int = 100

    def get_budget(self) -> int:
        return self.budget

    def __str__(self) -> str:
        return f"{self.name}({self.budget})"


@dataclass
class Employee:
    name: str = "Emp"
    projects: List[Project] = field(default_factory=list)

    def get_no_projects(self) -> int:
        return len(self.projects)

    def add_project(self, project: Project) -> None:
        self.projects.append(project)

    def compute_total_budget(self) -> int:
        return sum(p.get_budget() for p in self.projects)

    def calculate_salary(self) -> float:
        return 0.0

    def show_projects(self) -> None:
        if not self.projects:
            print(f"{self.name}: no projects")
            return
        print(f"{self.name}: projects -> " + ", ".join(str(p) for p in self.projects))


class Manager(Employee):
    def calculate_salary(self) -> float:
        return 4000 + (self.compute_total_budget() * 10) / 100


class Programmer(Employee):
    def calculate_salary(self) -> float:
        return 3000 + self.get_no_projects() * 500


if __name__ == "__main__":
    projects = [Project(name="Proj1", budget=100), Project(name="Proj2", budget=200)]
    man = Manager(name="manager1", projects=projects.copy())
    pro = Programmer(name="programmer1", projects=projects.copy())

    man.show_projects()
    pro.show_projects()

    print("Manager salary:", man.calculate_salary())
    print("Programmer salary:", pro.calculate_salary())

    man.add_project(Project(name="Proj3", budget=500))
    print("\nProject added to manager only.")
    man.show_projects()
    pro.show_projects()

    print("New Manager salary:", man.calculate_salary())
    print("New Programmer salary:", pro.calculate_salary())
