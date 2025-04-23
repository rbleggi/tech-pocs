# 🚀 Tech POCs

Welcome to the **Tech POCs repository**!  
This repository contains **Proof of Concept (POC) projects** demonstrating different technologies and architectures.

Each POC is **self-contained**, with its own dependencies, build configurations, and documentation.

---

## 📌 Implementations

| 🧩 System                 | 📄 Description                                                                        | 📐 [Pattern](https://refactoring.guru/)    | 🛠️ Implementations                                                                                                            |
|---------------------------|---------------------------------------------------------------------------------------|---------------|--------------------------------------------------------------------------------------------------------------------------------|
| 🎯 **Tax System**         | System where different products have different tax rates per state and year.          | Specification | 🟢 **Kotlin**📘 [README](kotlin/tax-system/README.md)<br>🔵 **Scala 3**📘 [README](scala-3/tax-system/README.md)               |
| 🧭 **Logger Router**      | Logs to FS, ELK or custom sinks with sync/async modes – all through the same API.     | Strategy      | 🔵 **Scala 3**📘 [README](scala-3/logger-router/README.md)                                                                     |
| 🚚 **Logistic Pricing**   | Calculates dynamic freight costs based on size, volume, and transport type.           | Strategy      | 🔵 **Scala 3**📘 [README](scala-3/logistic-pricing/README.md)                                                                  |
| 🍽 **Restaurant Queue**   | Queue system that calculates how long each dish will take to prepare.                 | Command       | 🔵 **Scala 3**📘 [README](scala-3/restaurant-queue/README.md)                                                                  |
| 🎟 **Ticket Booking**     | Allows selecting show, date, seat, and zone while enforcing capacity.                 | Builder       | 🔵 **Scala 3**📘 [README](scala-3/ticket-booking/README.md)                                                                    |
| 🖨️ **Template Renderer** | Renders templates as HTML, PDF or CSV using the same source template.                 | Factory       | 🟢 **Kotlin**📘 [README](kotlin/template-renderer/README.md)<br>🔵 **Scala 3**📘 [README](scala-3/template-renderer/README.md) |
| 🎸 **Guitar Factory**     | Custom guitar builder with specs, models, operating systems and inventory management. | Builder       | 🔵 **Scala 3**📘 [README](scala-3/guitar-factory/README.md)                                                                    |
| 🎬 **Movie Recommender**  | Movie recommender engine using The Movies Dataset from Kaggle.                        |               | 🟢 **Python**📘 [README](python/movie-recommender/README.md)                                                                   |
| 🛒 **Grocery TODO List**  | Flexible grocery list with undo/redo functionality.                                   | Command       | 🔵 **Scala 3**📘 [README](scala-3/grocery-todo-list/README.md)                                                                 |

---

## 📚 Scala Study Materials

In addition to the POCs, this repository also includes a dedicated folder for studying and exploring Scala. The
`scala-study` directory contains learning notes and guides ranging from basics to advanced topics, libraries, and Spring
integration.

| 🗂️ Topic        | 📄 Description                                    | 🔗 Link                                              |
|------------------|---------------------------------------------------|------------------------------------------------------|
| 📘 **Index**     | Overview and entry point for all Scala materials  | [README.md](scala-study/README.md)                   |
| 🟡 **Basics**    | Fundamental concepts of the Scala language        | [README-Basics.md](scala-study/README-Basics.md)     |
| 🟠 **Advanced**  | Advanced techniques, patterns, and concepts       | [README-Advanced.md](scala-study/README-Advanced.md) |
| 🔵 **Libraries** | Useful libraries and tools in the Scala ecosystem | [README-Libs.md](scala-study/README-Libs.md)         |
| 🟢 **Spring**    | Using Scala with the Spring Framework             | [README-Spring.md](scala-study/README-Spring.md)     |
