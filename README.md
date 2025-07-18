# 📦 Data Management System (Shipping Orders)

A **Java-based console + JavaFX GUI application** to manage small-package shipping orders (e.g., e-commerce or Amazon-style workflows). This project emphasizes **object-oriented design**, a **layered architecture**, and strong **input validation**.

---

## 🚀 Features

- Create, view, update, and delete shipping orders
- Real-time input validation (no blanks, invalid names, or bad numbers)
- Robust error handling with meaningful feedback
- Weight range enforced: 0.1–150 lbs
- Distance limit enforced: up to 3000 miles
- Smart shipping cost calculation using realistic rates
- Clean, dark-themed JavaFX interface inspired by **OBS Studio**
- Clear visual indicators for success, warnings, and errors

---

## 📐 Layered Architecture

The codebase is cleanly structured into three layers:

```
├── UI Layer (JavaFX + console)
├── Logic Layer (ShippingOrderManager.java)
│
└── Data Layer (in-memory via ArrayList<ShippingOrder>)
```

---

### ✅ UI Layer
- JavaFX GUI with an OBS-inspired dark theme
- Command-line fallback with `Scanner` + `System.out`
- Full CRUD buttons and TableView display
- Alert dialogs for validation and confirmations

### ✅ Logic Layer
- Centralized in `ShippingOrderManager`
- Handles all business rules and CRUD operations

### ✅ Data Layer
- In-memory `ArrayList<ShippingOrder>` storage
- Easily swappable for JDBC-based SQL backend

---

## 📊 UML Diagram

Class structure and relationships:

![UML Diagram](UML/UML_Latest.jpeg)

---

## 🗺️ Test Flow Diagram (Flowchart)

Full test/validation logic shown here:

![Flowchart](UML/FlowChartFinal.svg)

---

## 🧪 Phase 1 – Logic and Input Validation

Implemented core logic with plain Java classes. Business rules like valid weight/distance were enforced with strong input validation and exception handling for smooth execution.

---

## 🧪 Phase 2 – JUnit Testing

JUnit tests validate all logic from Phase 1. Covers normal cases, edge inputs, and error handling to ensure method reliability.

▶️ **Video Demo**: [Watch on YouTube](https://www.youtube.com/watch?v=42L02K4EYRU)

---

## 🖥️ Phase 3 – JavaFX GUI Integration

Transitioned to a modern, user-friendly JavaFX interface with dark mode, TableView components, and sleek visuals.

▶️ **Video Demo**: [Watch on YouTube](https://www.youtube.com/watch?v=EZ-ITMU57j8)

---

## 🖼️ Sample GUI Preview

![GUI Sample](UML/GUI.png)

---

## ⚙️ JavaFX Setup Instructions

To run the GUI in IntelliJ:

1. Download JavaFX SDK: [gluonhq.com/products/javafx](https://gluonhq.com/products/javafx/)
2. Extract the SDK
3. In IntelliJ:
    - Go to **File > Project Structure > Libraries**
    - Add all `.jar` files from the `lib` folder
4. Add VM options in **Run > Edit Configurations**:
    - Add all `--module-path <your_path_here>/lib --add-modules javafx.controls,javafx.fxml` (Replace `<your_path_here>` with the actual path where you placed the JavaFX SDK.)


> If VM options aren’t visible, click  
> **Modify Options > Add VM options**

---

## 🧠 Input Validation Examples

- **Names**: Alphabetic only, max 30 characters
- **Weight**: Between `0.1` and `150` lbs
- **Distance**: Between `1` and `3000` miles
- **Blank/invalid entries** trigger warnings
- **Bad IDs** trigger clear error messages

---

## 📝 Latest Updates

- ✅ Phase 1: Business logic + validation complete
- ✅ Phase 2: All core methods tested via JUnit
- ✅ Phase 3: Full-featured JavaFX UI implemented
- 🎨 Enhanced styling with shadows and effects
- 🏁 Finalized JDBC integration for live data handling

---

## ✅ Phase 4 – Adding a Database

JDBC support is live! App now persists data to MySQL:

- JDBC driver added
- `MainController.java` prompts for DB login
- UI displays connection errors cleanly
- Seamless JavaFX ↔ SQL communication
- Modular backend ready for future data sources

---

## 🔮 Future Enhancements

### Phase 5 – Advanced Features (Planned)

- Search, filter, and sort orders by customer, shipper, weight, etc.
- Export data to CSV or PDF
- Role-based login with sessions
- Admin dashboard with visual metrics

---

## 👨‍💻 Author

**Julio Lopez**  
📎 [LinkedIn Profile](https://www.linkedin.com/in/julio-lopez-380937282/)

---

> Built for clarity, modularity, and scale — this system is test-driven and designed with clean architecture principles, ready for real-world deployment.
