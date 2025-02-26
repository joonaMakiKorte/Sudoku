# 🧩 Sudoku Game

A **simple Sudoku game** built with **JavaFX**, featuring a modern GUI with customizable themes.

## ✨ Features
- **Light & Dark Theme** – Choose between two visually appealing styles.
- **Smart Board Generation** – Uses a mix of **randomization** and a **backtracking algorithm** to generate valid Sudoku puzzles.
- **Multiple Difficulty Levels** – Four difficulty options, with fewer pre-filled cells as difficulty increases.
- **Three Mistakes Rule** – The game ends after three incorrect moves.
- **Hint System** – Players can use up to **three hints per round** to fill a random cell.
- **Brute-Force Solver** – A built-in **auto-solve** feature that requires entering the password **"1234"** to activate.

## 🖥️ Technology Used
- **JavaFX** for the graphical user interface.
- **FXML & CSS** for styling and layout.
- **Backtracking Algorithm** for Sudoku board generation and brute-force solving.

## 🛠️ Getting Started

### Prerequisites

Before running this Sudoku application, ensure that you have the following installed:

- **Java Development Kit (JDK)**: The project uses JavaFX, which is compatible with JDK versions 8 through 17. You can download the appropriate JDK from [Oracle](https://www.oracle.com/java/) or [Adoptium](https://adoptium.net/).
- **JavaFX SDK**: If you are using JDK 11 or newer, you need to download the JavaFX SDK separately from [OpenJFX](https://openjfx.io/).

### Installation and Running the Application

Follow these steps to download and run the application:

1. **Clone the Repository**
   ```sh
   git clone https://github.com/joonaMakiKorte/Sudoku.git
   ```
   This command will create a local copy of the repository on your machine.

2. **Navigate to the Project Directory**
   ```sh
   cd Sudoku
   ```

3. **Build the Project**
   The project uses Maven as its build tool. To compile and package the application, run:
   ```sh
   ./mvnw clean install
   ```
   If you are on Windows, use:
   ```sh
   mvnw.cmd clean install
   ```
   This command will compile the code, run tests, and package the application into a JAR file.

4. **Run the Application**
   After building, execute the application using:
   ```sh
   java -jar target/Sudoku-1.0-SNAPSHOT.jar
   ```
   Ensure that the version number in the JAR file name matches the version specified in the `pom.xml` file.

## 🎮 Usage

Upon launching the application:

- **Select Theme**: Choose between Light or Dark mode from the theme menu.
- **Choose Difficulty**: Select a difficulty level to start a new game.
- **Gameplay**:
  - Click on a cell to select it.
  - Enter a number (1-9) to fill the cell.
  - Use the "Hint" button to fill a random cell (limited to three hints per game).
  - The game ends after three incorrect moves.
- **Auto-Solve**: To activate the brute-force solver, enter the password "1234" when prompted.

## 📁 Project Structure

The project's structure is organized as follows:

- **src/main/**
  - **java/com/example/sudokuguifx/** 
    - **SudokuGUI.java**: The main entry point of the application.
    - **GenerateBoard.java**: Handles the generation of Sudoku boards.
    - **Solver.java**: Implements the backtracking algorithm for solving puzzles.
    - **Controllers**: Manages user interactions and UI updates.
  - **resources/**
    - **com/example/sudokuguifx/**
      - **fxml files**: Defines the layouts of the views.
    - **images**: Icons for the different UI elements.

## 🔎 Troubleshooting

- **JavaFX Compatibility**: If you encounter errors related to JavaFX, make sure your JDK version includes JavaFX (JDK 8) or manually link the JavaFX SDK if using JDK 11+.
- **Maven Wrapper Execution**: If you have permission issues with `./mvnw`, give it execute permissions:
  ```sh
  chmod +x mvnw
  ```
  Alternatively, if you have Maven installed globally, use `mvn` instead of `./mvnw`.
- **Windows Users**: Use `mvnw.cmd` instead of `./mvnw` and adjust file paths accordingly.

By following these instructions, you should be able to successfully run the Sudoku application. If you encounter issues, check the project's dependencies and environment setup.

### Enjoy solving Sudoku with an intuitive and polished JavaFX experience! 🚀
