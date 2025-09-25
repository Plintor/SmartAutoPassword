## SmartAutoPassword

# RU
 SmartAutoPassword (AutoLogin) 1.12.2 + сохранение паролей и 'шифрование' (с заменой и Base64)

## Команды
- `-ap` - Включить авто ввод пароля
- `-sreg <длина пароля>` - Регистрируется и автоматически сохранит случайный пароль
- `-spass` - Вводит пароль, если он сохранен

Когда в чате присутствует сообщения `/register <пароль>` или `/login <пароль>`, SmartPassword предложит сохранить пароль:
- `-yes` - Сохранить пароль
- `-no` - Не сохранить пароль

## Хранение паролей
Ваши пароли хранятся в папке `C:\PlintoUtilsCfg\AutoPassword` в файле `AutoPassword.on.top`.
В файле `AutoPassword.on.top` записано примерно следующее: `Username:Server:Password`.
Пароль имеет свой формат шифрования.
По желанию можете изменить папку хранения паролей.

Данная модификация возможно спасет ваш аккаунт на сервере (подразумывается если вы скачали вредоносный мод до того как будете авторизироватся на сервере), но если злоумышленник украдет файл, тогда если вы не изменили метод шифрования, вы потеряете аккаунт/аккаунты :(

## Оффициальные ссылки для скачивания:
https://modrinth.com/mod/autopassword


## Не я выкладывал
https://www.modpackindex.com/mod/60256/smartautopassword

https://openeye.openmods.info/mod/smartpassword

https://minecraft-club.ru/?mod=autopassword

https://sodamc.com/59714-10092401.html (ух эти китайцы)

https://www.zitbbs.com/thread-21180-1-1.html

