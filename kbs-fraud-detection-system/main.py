print("💳 Fraud Detection System (KBS)\n")

amount = float(input("Enter transaction amount: "))
location = input("Enter location (usual/unusual): ").lower()
time = input("Enter time (day/night): ").lower()

print("\n--- Analysis Result ---")

if amount > 100000 and location == "unusual":
    result = "⚠️ HIGH RISK: Fraud Detected"
    reason = "Large transaction in unusual location"
elif amount > 50000 and time == "night":
    result = "⚠️ MEDIUM RISK"
    reason = "High amount at unusual time"
else:
    result = "✅ LOW RISK: Transaction Safe"
    reason = "Normal transaction pattern"

print(result)
print("Reason:", reason)