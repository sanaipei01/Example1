# ?? Smart Meal Planner

## Project Overview

The **Smart Meal Planner** is a **rule-based system** that recommends meals based on your **diet type, allergies, nutritional goals, and preferences**. It combines research, meal data, and a simple interface to provide **personalized meal suggestions**.

---

## Week 1 – Foundations & Research

**Member 1 – Theoretical Base**  
- Introduced expert systems and rule-based logic.  
- Explained system components: Knowledge base, Inference engine, User interface, IF–THEN rules, Forward vs backward chaining.  

**Member 2 – Domain Research**  
- Researched meal planning and its importance.  
- Covered diet types: vegetarian, vegan, high-protein, low-carb.  
- Reviewed basic nutrition: calories, macronutrients.  
- Highlighted challenges students face when planning meals.  

**Member 3 – Problem & System Planning**  
- Defined the problem and target users.  
- Proposed system design with: Inputs (diet, allergies, budget), Outputs (meal suggestions).  
- Created sample IF–THEN rules and a simple architecture diagram:  

User ? Input ? Rule Engine ? Meal Output

---

## Week 2 – Meal Dataset & Simple UI

**Member 1 – Rule Designer**  
- Created IF–THEN rules to filter meals based on diet, allergies, and goals.  

**Member 2 – Meal Data Creator**  
- Built a meal dataset (at least 15 meals) with details:  
  - Meal type: breakfast, lunch, dinner  
  - Diet: vegan, vegetarian, high_protein, low_carb  
  - Nutritional info: protein, contains ingredients  
- Made sure dataset matches the rules for correct filtering.  

**Member 3 – System Designer & UI Planner**  
- Designed a simple CLI interface.  
- Defined system flow:  

User Input ? Apply Rules ? Filter Meals ? Display Results

---

## How to Run

1. Make sure meals.json is in the project folder.  
2. Run the CLI script.  
3. Enter your diet, allergies, and goal to get meal suggestions.  

---

## Notes

- Rules must **match the meal dataset tags exactly** (e.g., high_protein).  
- Inputs must align with rules to filter meals correctly.  
- CLI is recommended for testing because it’s simple and quick.
