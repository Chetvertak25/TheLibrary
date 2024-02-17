# Название проекта
MVC Веб-приложение библиотеки 

## Технологии
- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Spring Boot](https://spring.io/projects/spring-boot/)
- [Spring Boot Starter Data JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Spring Boot Starter Security](https://spring.io/guides/gs/securing-web/)
- [Spring Boot Starter Thymeleaf](https://spring.io/guides/gs/serving-web-content/)
- [Spring Boot Starter Web](https://spring.io/guides/gs/serving-web-content/)
- [Thymeleaf Extras Spring Security 5](https://github.com/thymeleaf/thymeleaf-extras-springsecurity)
- [PostgreSQL](https://www.postgresql.org/)
- [Spring Boot Starter Test](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing)
- [Spring Security Test](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)
- [Hibernate Validator](https://hibernate.org/validator/)


## Запуск
Для запуска проекта необходимо выполнить следующие действия:

1. В файле `application.properties` укажите параметры подключения к базе данных.
2. Запустите скрипты `.sql` из папки `resources` для создания и заполнения таблиц базы данных.
3. Запустите проект.

## Инструкция
Это веб-приложение библиотеки с пользователями и книгами. 
Реализована аутентификация и авторизация с ролями `admin` и `user`. 
Для каждой роли предусмотрены различные права доступа и возможности.
При создании книги и человека поля проходят валидацию.
Пароли в базе данных хранятся в зашифрованном виде.

Для входа под администратором используйте данные:
- **Логин:** admin
- **Пароль:** admin

Для входа под пользователем с ролью `USER` используйте имя пользователя и пароль "password". Например:
- **Логин:** user5
- **Пароль:** password

## Возможности USER
- Регистрация новых пользователей с автоматическим присвоением роли `USER`.
- Аутентификация и выход из системы.
- Просмотр всех книг и поиск книг по названию.
- Просмотр статуса книги (занята / свободна) и взятие книги себе.
- При просрочке возврата книги она помечается красным шрифтом на странице "Мои книги".
- Возможность вернуть выбранные книги или все книги сразу.

## Возможности ADMIN
Все возможности пользователя плюс:
- Просмотр всех пользователей с их ролями, именем, возрастом и другими данными.
- Добавление новых пользователей и книг.
- Удаление пользователей и книг.
- Редактирование информации о пользователях и книгах, назначение ролей и владельцев книг.