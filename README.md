# 📦 Data Management System (Shipping Orders)

This project is a **Java-based console + JavaFX GUI application** for managing small-package shipping orders (like Amazon or e-commerce). It demonstrates strong **object-oriented design**, **layered architecture**, and robust **input validation**.

---

## 🚀 Features

- Add, view, update, and delete shipping orders
- Input validation: blocks blank entries, non-names, or invalid numbers
- Error handling for all operations (missing ID, bad format, etc.)
- Weight validation: 0.1–150 lbs
- Distance validation: up to 3000 miles
- Cost calculation based on realistic shipping rate
- Consistent and user-friendly JavaFX GUI
- Styled theme inspired by **OBS** (bluish dark theme with modern shadows)
- Clear messages for all actions (success, warning, failure)

---

## 📐 Layered Architecture

The app is divided into three layers for maintainability and scalability:

```
├── UI Layer (JavaFX + console)
├── Logic Layer (ShippingOrderManager.java)
│
└── Data Layer (in-memory via ArrayList<ShippingOrder>)
```

---

### ✅ UI Layer
- JavaFX GUI with stylish dark theme similar to OBS Studio
- Command-line fallback using `Scanner` and `System.out`
- Buttons for CRUD
- TableView to list orders
- Dialogs for validation, confirmations, and errors

### ✅ Logic Layer
- Encapsulated in `ShippingOrderManager`
- Performs all CRUD operations
- Returns boolean/object results
- Enforces business rules: weight, distance, ID lookups

### ✅ Data Layer
- Stores orders in a dynamic in-memory list
- Easily replaceable with SQL backend (JDBC-ready structure)

---

## 📊 UML Diagram

The following diagram shows the class relationships and method design:

![UML Diagram](UML/UML_Latest.jpeg)

---

## 🗺️ Test Flow Diagram (Flowchart)

The following flowchart illustrates the full test flow and input validation logic:

![Flowchart](UML/FlowChartFinal.svg)

---

## 🖼️ Sample GUI Preview

The JavaFX GUI phase currently looks like this:

![GUI Sample](UML/GUI.png)

---

## 🧠 Input Validation Examples

- **Customer/Shipper names** must contain only letters and spaces
- **Weight** must be a valid number between `0.1` and `150`
- **Distance** must be a whole number between `1` and `3000`
- **Blank entries** or malformed input trigger warnings
- **Invalid order IDs** show appropriate error messages

---

## 📝 Latest Updates

- Phase 1 (Logic) finished with robust validation and error handling.
- Phase 2 (JUnit testing) implemented for all critical business rules.
- Phase 3 (GUI) completed with a modern, user-friendly OBS-inspired theme.
- Buttons have subtle drop shadows, hover transitions, and consistent colors.
- JavaFX TableView now includes units (e.g., lb, mi, $) and 2-decimal price formatting.
- Smoothed styling with improved fonts and better focus loss handling on selection.
- Application is fully ready for next step of JDBC database integration.

---

## 🔮 Future Enhancements

### Phase 4 – Database Integration
- JDBC support with SQL schema
- Load/save orders to a persistent backend
- Pre-validation before database write operations

---

## 👨‍💻 Author

**Julio Lopez**

📎 [LinkedIn Profile](https://www.linkedin.com/in/julio-lopez-380937282/)

---

> This project was developed with a focus on clean separation of concerns, testability, and modern UI. Each logic method is test-driven and designed for future growth with a professional-quality look and feel.

