# Stock Portfolio Tracker

## Overview

A robust Spring Boot application designed for efficient stock portfolio management and tracking. This system provides real-time stock price updates, portfolio analytics, and comprehensive investment tracking capabilities.

## Features

### Core Functionality

- Real-time stock price tracking
- Portfolio value calculation
- Gain/Loss analysis
- Stock purchase and sale tracking
- Historical price tracking
- Portfolio performance analytics

### Technical Features

- RESTful API architecture
- Real-time data updates
- Secure data management
- Responsive web interface
- Database persistence
- Automated price updates

## Technology Stack

### Backend

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- Hibernate

### Database

- H2 Database (Development)
- MySQL (Production)

### Frontend

- Thymeleaf
- Bootstrap
- JavaScript
- jQuery

### Build Tools

- Maven
- Git

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL (for production)
- IDE (IntelliJ IDEA recommended)

### Installation

1. Clone the repository

```bash
git clone https://github.com/Ignatiusf07/StockPortFolio.git
```

2. Configure database

```properties
# application.properties
spring.datasource.url=jdbc:h2:file:./portfolio
spring.datasource.username=sa
spring.datasource.password=password
```

3. Build the project

```bash
mvn clean install
```

4. Run the application

```bash
mvn spring-boot:run
```

### Accessing the Application

- Web Interface: http://localhost:8081
- H2 Console: http://localhost:8081/h2-console
  - JDBC URL: jdbc:h2:file:./portfolio
  - Username: sa
  - Password: password

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/bp/PortFolio/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       ├── model/
│   │       └── config/
│   └── resources/
│       ├── static/
│       ├── templates/
│       └── application.properties
```

## API Documentation

### Stock Management

- GET `/api/stocks` - List all stocks
- POST `/api/stocks` - Add new stock
- PUT `/api/stocks/{id}` - Update stock
- DELETE `/api/stocks/{id}` - Delete stock
- GET `/api/stocks/{id}` - Get stock details

### Portfolio Analytics

- GET `/api/stocks/portfolio/stats` - Get portfolio statistics
- GET `/api/stocks/portfolio/performance` - Get performance metrics
- GET `/api/stocks/portfolio/history` - Get historical data

## Database Schema

### Stocks Table

```sql
CREATE TABLE stocks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticker VARCHAR(10) NOT NULL UNIQUE,
    quantity INT NOT NULL,
    purchase_price DECIMAL(10,2) NOT NULL,
    current_price DECIMAL(10,2) NOT NULL,
    purchase_date DATE NOT NULL,
    notes VARCHAR(1000),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Security Features

- Input validation
- SQL injection prevention
- XSS protection
- CSRF protection
- Secure session management

## Performance Optimizations

- Connection pooling
- Query optimization
- Caching implementation
- Efficient data loading

## Monitoring and Maintenance

- Logging implementation
- Error tracking
- Performance monitoring
- Database maintenance

## Future Enhancements

- Mobile application
- Advanced analytics
- Real-time notifications
- Market news integration
- Multiple portfolio support
- Automated trading suggestions

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## Best Practices

- Clean code architecture
- Comprehensive error handling
- Regular security updates
- Performance optimization
- Code documentation
- Unit testing

## License

This project is licensed under the MIT License - see the LICENSE file for details

## Contact

- Project Link: [https://github.com/Ignatiusf07/StockPortFolio](https://github.com/Ignatiusf07/StockPortFolio)
- Developer: Ignatius Francis
- Email: ignatiusf07@gmail.com

## Version History

- 1.0.0 - Initial release
- 1.1.0 - Added real-time price updates
- 1.2.0 - Enhanced portfolio analytics
- 1.3.0 - Security improvements
- 1.4.0 - Performance optimizations

## Support

For support, please open an issue in the GitHub repository or contact the development team.

## Acknowledgments

- Spring Boot Team
- Hibernate Team
- MySQL Community
- All contributors
