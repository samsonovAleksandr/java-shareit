# java-shareit
Учебный проект Яндекс.Практикум

## Описание
![spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![postgres](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white
) ![ide](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white
) ![java](https://img.shields.io/badge/Java11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
) ![markdown](https://img.shields.io/badge/Markdown-000000?style=for-the-badge&logo=markdown&logoColor=white
) ![junit](https://img.shields.io/badge/junit5-DC143C?style=for-the-badge&logo=junit5&logoColor=white
) ![maven](https://img.shields.io/badge/Apache_Maven-008000?style=for-the-badge&logo=apachemaven&logoColor=white) ![docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) ![rest](https://img.shields.io/badge/REST-FFA500?style=for-the-badge&logo=airbrakedotio&logoColor=white)

Сервис для обмена вещами с другими пользователями. Он даёт пользователям возможность рассказывать, какими вещами они готовы поделиться, а так же находить нужную вещь и брать её в аренду на какое-то время.

## Архитектура

Сервис состоит из двух модулей:

+ ### gateway

  + Получение и валидация запросов пользователя.
  + Отправка полученного запрос на server.
  + Получение ответа от server.

+ ### server

  + Добавление/удаление пользователей, обновление данных, удаление.
  + Добавление/удаление/редактирование вещей.
  + Бронировать вещь на определённые даты и закрывает к ней доступ на время бронирования от других желающих.
  + Поиск вещей по названию или описанию, с возможностью фильтрации и постраничной выдачей.
  + Подтверждение или отклонение заявок на аренду владельцем.
  + Создание запроса на вещь, которая отсутствует в приложении.
  + Получение списка всех бронирований пользователя, с возможностью сортировки и фильтрации. 
  + Получение списка всех бронирований вещей, принадлежащих пользователю, с возможностью сортировки и фильтрации.
  + Получение списка всех вещей пользователя.
  + Добавление отзыва пользователем на вещь после того, как взял её в аренду.
  + Получение пользователем всех его запросов, с возможностью фильтрации и сортировки.

## База данных
![data_base](server/src/main/resources/QuickDBD-export.png)