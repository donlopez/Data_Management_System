# Data Management System (Shipping Orders)

## Overview

This Data Management System (DMS) allows users to manage shipping orders through a simple command-line interface. The system supports adding, updating, deleting, and viewing orders, while enforcing validation and calculating shipping costs.

The architecture follows a clean 3-layer structure:

- **UI Layer:** `Main` class (handles CLI interaction)
- **Logic Layer:** `ShippingOrderManager` class (business logic)
- **Data Layer:** `ShippingOrder` class (data model)

---

## UML Diagram

The following UML diagram shows the current architecture of the system:

![UML Diagram](UML/UML_Latest.jpeg)

---

## Test Flowchart

The following flowchart illustrates the test plan used for Module 6 (Software Test Plan), including validation logic and test cases for Add, Update, Delete, and View operations:

![Flowchart](UML/FlowChartFinal.svg)

---

## Latest Updates

- Added validation steps and error handling in all operations (Add, Update, Delete).
- Separated Update flow into distinct steps for weight and distance (per Prof. Evans' feedback).
- Added explicit **Calculate Shipping Cost** step after updating weight/distance.
- Cleaned and finalized the flowchart with styling improvements.
- Enhanced `ShippingOrderManager` and `Main` classes to fully align with updated flowchart and grading criteria.
- Display shipping cost with 2 decimal digits.
- Fixed maximum distance limit to **3000 miles**.

---

## Future Phases / Next Steps

- Add support for **persisting orders** to file (saving/loading between sessions).
- Implement unit tests for business logic methods.
- Add sorting and filtering options when viewing orders.
- Expand shipping cost calculation to include more advanced pricing rules.
- Implement a more robust **menu loop** with better user experience.
- Potentially add an option to **export order list** as CSV.

---

## Instructor Feedback Addressed

- ✅ Flowchart revised to avoid multiple conditions per decision box (Update flow now prompts separately for weight and distance).
- ✅ Shipping cost recalculated after update.
- ✅ Input validation included on all relevant fields.
- ✅ Flowchart reflects specific actions in the current app (not generic).

---

## 👨‍💻 Author

**Julio Lopez**

📎 [LinkedIn Profile](https://www.linkedin.com/in/julio-lopez-380937282/)

---

> This project was developed with a focus on clean separation of concerns, testability, and readiness for future GUI/database expansion. All logic methods return values to support unit testing and better system feedback.
