# Stock Portfolio Tracker

A modern web application for tracking and managing your stock portfolio. Built with Spring Boot and featuring a responsive UI with real-time charts and analytics.

## Features

- 📊 **Portfolio Overview**

  - Total portfolio value tracking
  - Total shares count
  - Number of different stocks
  - Visual portfolio allocation pie chart
  - Purchase history timeline chart

- 📈 **Stock Management**

  - Add new stocks with detailed information
  - Edit existing stock entries
  - Delete stocks from portfolio
  - Track purchase dates and prices
  - Add notes to stock entries

- 🎨 **Modern UI/UX**

  - Responsive design for all devices
  - Dark/Light mode support
  - Interactive charts and visualizations
  - Real-time updates
  - Infinite scroll for stock listings
  - Toast notifications for user feedback

- 🔄 **Data Visualization**
  - Portfolio allocation pie chart
  - Purchase history line chart
  - Interactive tooltips with detailed information
  - Responsive chart updates

## Tech Stack

- **Backend**

  - Spring Boot
  - Spring Data JPA
  - H2 Database
  - Maven

- **Frontend**
  - Thymeleaf
  - Bootstrap 5
  - Chart.js
  - JavaScript (ES6+)

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Modern web browser

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/StockPortfolio.git
   cd StockPortfolio
   ```

2. Build the project:

   ```bash
   mvn clean install
   ```

3. Run the application:

   ```bash
   mvn spring-boot:run
   ```

4. Access the application at `http://localhost:8081`

## Usage

### Adding a Stock

1. Fill in the stock details:
   - Ticker symbol (e.g., AAPL)
   - Quantity of shares
   - Purchase price per share
   - Purchase date (optional)
   - Notes (optional)
2. Click "Add Stock" to save

### Managing Stocks

- **Edit**: Click the pencil icon to modify stock details
- **Delete**: Click the trash icon to remove a stock
- **View Details**: Hover over notes to see full content

### Viewing Analytics

- Portfolio allocation is shown in the pie chart
- Purchase history is displayed in the line chart
- Hover over chart elements for detailed information

### Theme Toggle

- Click the moon/sun icon in the navigation bar to switch between dark and light modes

## Project Structure

```
StockPortfolio/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/bp/PortFolio/
│   │   │       ├── controller/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       └── service/
│   │   └── resources/
│   │       ├── static/
│   │       │   └── css/
│   │       └── templates/
│   └── test/
├── pom.xml
└── README.md
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Bootstrap 5 for the UI framework
- Chart.js for data visualization
- Spring Boot for the backend framework
- All contributors and users of the project
