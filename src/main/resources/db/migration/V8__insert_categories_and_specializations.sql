INSERT INTO categories (name) VALUES
                                  ('Веб-разработка'),
                                  ('Мобильная разработка'),
                                  ('Дизайн'),
                                  ('Маркетинг'),
                                  ('Копирайтинг'),
                                  ('Видео-монтаж'),
                                  ('Анализ данных'),
                                  ('Информационная безопасность'),
                                  ('Разработка игр'),
                                  ('Техническая поддержка');

INSERT INTO specializations (category_id, name)
SELECT id, 'Frontend-разработка' FROM categories WHERE name = 'Веб-разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Backend-разработка' FROM categories WHERE name = 'Веб-разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Fullstack-разработка' FROM categories WHERE name = 'Веб-разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Веб-дизайн' FROM categories WHERE name = 'Веб-разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'CMS-разработка' FROM categories WHERE name = 'Веб-разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'E-commerce решения' FROM categories WHERE name = 'Веб-разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Интеграция API' FROM categories WHERE name = 'Веб-разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Оптимизация производительности' FROM categories WHERE name = 'Веб-разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Безопасность веб-приложений' FROM categories WHERE name = 'Веб-разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Разработка Progressive Web App (PWA)' FROM categories WHERE name = 'Веб-разработка';

INSERT INTO specializations (category_id, name)
SELECT id, 'Android-разработка' FROM categories WHERE name = 'Мобильная разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'iOS-разработка' FROM categories WHERE name = 'Мобильная разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Кроссплатформенная разработка' FROM categories WHERE name = 'Мобильная разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Мобильный UI/UX дизайн' FROM categories WHERE name = 'Мобильная разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Разработка мобильных игр' FROM categories WHERE name = 'Мобильная разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Интеграция с API' FROM categories WHERE name = 'Мобильная разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Оптимизация производительности мобильных приложений' FROM categories WHERE name = 'Мобильная разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Тестирование мобильных приложений' FROM categories WHERE name = 'Мобильная разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Безопасность мобильных приложений' FROM categories WHERE name = 'Мобильная разработка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Архитектура мобильных приложений' FROM categories WHERE name = 'Мобильная разработка';

INSERT INTO specializations (category_id, name)
SELECT id, 'Графический дизайн' FROM categories WHERE name = 'Дизайн';
INSERT INTO specializations (category_id, name)
SELECT id, 'UX/UI дизайн' FROM categories WHERE name = 'Дизайн';
INSERT INTO specializations (category_id, name)
SELECT id, 'Веб-дизайн' FROM categories WHERE name = 'Дизайн';
INSERT INTO specializations (category_id, name)
SELECT id, 'Моушн-дизайн' FROM categories WHERE name = 'Дизайн';
INSERT INTO specializations (category_id, name)
SELECT id, 'Иллюстрация' FROM categories WHERE name = 'Дизайн';
INSERT INTO specializations (category_id, name)
SELECT id, 'Дизайн логотипов' FROM categories WHERE name = 'Дизайн';
INSERT INTO specializations (category_id, name)
SELECT id, 'Полиграфический дизайн' FROM categories WHERE name = 'Дизайн';
INSERT INTO specializations (category_id, name)
SELECT id, 'Фирменный стиль' FROM categories WHERE name = 'Дизайн';
INSERT INTO specializations (category_id, name)
SELECT id, '3D моделирование' FROM categories WHERE name = 'Дизайн';
INSERT INTO specializations (category_id, name)
SELECT id, 'Дизайн упаковки' FROM categories WHERE name = 'Дизайн';

INSERT INTO specializations (category_id, name)
SELECT id, 'Цифровой маркетинг' FROM categories WHERE name = 'Маркетинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'SEO-оптимизация' FROM categories WHERE name = 'Маркетинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'Контент-маркетинг' FROM categories WHERE name = 'Маркетинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'SMM' FROM categories WHERE name = 'Маркетинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'Email-маркетинг' FROM categories WHERE name = 'Маркетинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'PPC-реклама' FROM categories WHERE name = 'Маркетинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'Анализ рынка' FROM categories WHERE name = 'Маркетинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'Брендинг' FROM categories WHERE name = 'Маркетинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'Рекламное агентство' FROM categories WHERE name = 'Маркетинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'PR и коммуникации' FROM categories WHERE name = 'Маркетинг';

INSERT INTO specializations (category_id, name)
SELECT id, 'SEO-копирайтинг' FROM categories WHERE name = 'Копирайтинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'Креативный копирайтинг' FROM categories WHERE name = 'Копирайтинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'Технический копирайтинг' FROM categories WHERE name = 'Копирайтинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'Рекламные тексты' FROM categories WHERE name = 'Копирайтинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'Сценарии для видео' FROM categories WHERE name = 'Копирайтинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'Контент для блогов' FROM categories WHERE name = 'Копирайтинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'Копирайтинг для социальных сетей' FROM categories WHERE name = 'Копирайтинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'SEO-статьи' FROM categories WHERE name = 'Копирайтинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'Пресс-релизы' FROM categories WHERE name = 'Копирайтинг';
INSERT INTO specializations (category_id, name)
SELECT id, 'Описания товаров' FROM categories WHERE name = 'Копирайтинг';

INSERT INTO specializations (category_id, name)
SELECT id, 'Монтаж рекламного ролика' FROM categories WHERE name = 'Видео-монтаж';
INSERT INTO specializations (category_id, name)
SELECT id, 'Монтаж музыкального клипа' FROM categories WHERE name = 'Видео-монтаж';
INSERT INTO specializations (category_id, name)
SELECT id, 'Монтаж короткометражного фильма' FROM categories WHERE name = 'Видео-монтаж';
INSERT INTO specializations (category_id, name)
SELECT id, 'Цветокоррекция' FROM categories WHERE name = 'Видео-монтаж';
INSERT INTO specializations (category_id, name)
SELECT id, 'Визуальные эффекты' FROM categories WHERE name = 'Видео-монтаж';
INSERT INTO specializations (category_id, name)
SELECT id, 'Монтаж интервью' FROM categories WHERE name = 'Видео-монтаж';
INSERT INTO specializations (category_id, name)
SELECT id, 'Монтаж обучающих видео' FROM categories WHERE name = 'Видео-монтаж';
INSERT INTO specializations (category_id, name)
SELECT id, 'Монтаж корпоративного видео' FROM categories WHERE name = 'Видео-монтаж';
INSERT INTO specializations (category_id, name)
SELECT id, 'Монтаж YouTube видео' FROM categories WHERE name = 'Видео-монтаж';
INSERT INTO specializations (category_id, name)
SELECT id, 'Монтаж видеоблога' FROM categories WHERE name = 'Видео-монтаж';

INSERT INTO specializations (category_id, name)
SELECT id, 'Бизнес-аналитика' FROM categories WHERE name = 'Анализ данных';
INSERT INTO specializations (category_id, name)
SELECT id, 'Data Mining' FROM categories WHERE name = 'Анализ данных';
INSERT INTO specializations (category_id, name)
SELECT id, 'Big Data' FROM categories WHERE name = 'Анализ данных';
INSERT INTO specializations (category_id, name)
SELECT id, 'Data Visualization' FROM categories WHERE name = 'Анализ данных';
INSERT INTO specializations (category_id, name)
SELECT id, 'Machine Learning' FROM categories WHERE name = 'Анализ данных';
INSERT INTO specializations (category_id, name)
SELECT id, 'Statistical Analysis' FROM categories WHERE name = 'Анализ данных';
INSERT INTO specializations (category_id, name)
SELECT id, 'R Programming' FROM categories WHERE name = 'Анализ данных';
INSERT INTO specializations (category_id, name)
SELECT id, 'Python для анализа данных' FROM categories WHERE name = 'Анализ данных';
INSERT INTO specializations (category_id, name)
SELECT id, 'Анализ социальных данных' FROM categories WHERE name = 'Анализ данных';
INSERT INTO specializations (category_id, name)
SELECT id, 'Анализ продаж' FROM categories WHERE name = 'Анализ данных';

INSERT INTO specializations (category_id, name)
SELECT id, 'Пентестинг' FROM categories WHERE name = 'Информационная безопасность';
INSERT INTO specializations (category_id, name)
SELECT id, 'Криптография' FROM categories WHERE name = 'Информационная безопасность';
INSERT INTO specializations (category_id, name)
SELECT id, 'Безопасность сетей' FROM categories WHERE name = 'Информационная безопасность';
INSERT INTO specializations (category_id, name)
SELECT id, 'Мониторинг безопасности' FROM categories WHERE name = 'Информационная безопасность';
INSERT INTO specializations (category_id, name)
SELECT id, 'Аудит безопасности' FROM categories WHERE name = 'Информационная безопасность';
INSERT INTO specializations (category_id, name)
SELECT id, 'Информационная безопасность веб-приложений' FROM categories WHERE name = 'Информационная безопасность';
INSERT INTO specializations (category_id, name)
SELECT id, 'Управление инцидентами' FROM categories WHERE name = 'Информационная безопасность';
INSERT INTO specializations (category_id, name)
SELECT id, 'Защита от DDoS-атак' FROM categories WHERE name = 'Информационная безопасность';
INSERT INTO specializations (category_id, name)
SELECT id, 'Безопасность мобильных устройств' FROM categories WHERE name = 'Информационная безопасность';
INSERT INTO specializations (category_id, name)
SELECT id, 'Разработка безопасного ПО' FROM categories WHERE name = 'Информационная безопасность';

INSERT INTO specializations (category_id, name)
SELECT id, 'Разработка мобильных игр' FROM categories WHERE name = 'Разработка игр';
INSERT INTO specializations (category_id, name)
SELECT id, '3D-моделирование для игр' FROM categories WHERE name = 'Разработка игр';
INSERT INTO specializations (category_id, name)
SELECT id, 'Геймдизайн' FROM categories WHERE name = 'Разработка игр';
INSERT INTO specializations (category_id, name)
SELECT id, 'Разработка игрового движка' FROM categories WHERE name = 'Разработка игр';
INSERT INTO specializations (category_id, name)
SELECT id, 'Игровая анимация' FROM categories WHERE name = 'Разработка игр';
INSERT INTO specializations (category_id, name)
SELECT id, 'Разработка VR-игр' FROM categories WHERE name = 'Разработка игр';
INSERT INTO specializations (category_id, name)
SELECT id, 'Разработка AR-игр' FROM categories WHERE name = 'Разработка игр';
INSERT INTO specializations (category_id, name)
SELECT id, 'Монетизация игр' FROM categories WHERE name = 'Разработка игр';
INSERT INTO specializations (category_id, name)
SELECT id, 'Сетевая архитектура игр' FROM categories WHERE name = 'Разработка игр';
INSERT INTO specializations (category_id, name)
SELECT id, 'Тестирование игр' FROM categories WHERE name = 'Разработка игр';

INSERT INTO specializations (category_id, name)
SELECT id, 'Администрирование серверов' FROM categories WHERE name = 'Техническая поддержка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Поддержка пользователей' FROM categories WHERE name = 'Техническая поддержка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Обслуживание оборудования' FROM categories WHERE name = 'Техническая поддержка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Технический консалтинг' FROM categories WHERE name = 'Техническая поддержка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Обучение и поддержка ПО' FROM categories WHERE name = 'Техническая поддержка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Удалённая поддержка' FROM categories WHERE name = 'Техническая поддержка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Мониторинг систем' FROM categories WHERE name = 'Техническая поддержка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Техническая документация' FROM categories WHERE name = 'Техническая поддержка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Настройка сетевого оборудования' FROM categories WHERE name = 'Техническая поддержка';
INSERT INTO specializations (category_id, name)
SELECT id, 'Внедрение IT-решений' FROM categories WHERE name = 'Техническая поддержка';