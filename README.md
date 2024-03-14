## SmartAutoPassword

# RU
 SmartAutoPassword (AutoLogin) 1.12.2 + сохранение паролей и 'шифрование' (используя замену и Base64)

## Команды
- `-ap` - Включить авто ввод пароля
- `-sreg <длина пароля>` - Регистрируется и автоматически сохранит случайный пароль
- `-spass` - Вводит пароль, если он сохранен

## Взаимодействие с SmartPassword
Когда в чате присутствует сообщения `/register <пароль>` или `/login <пароль>`, SmartPassword предложит сохранить пароль:
- `-yes` - Сохранить пароль
- `-no` - Не сохранить пароль

## Хранение паролей
Ваши пароли хранятся в папке `C:\PlintoUtilsCfg\AutoPassword` в файле `AutoPassword.on.top`.
В файле `AutoPassword.on.top` записано примерно следующее: `Username:Server:Password`.
Пароль имеет свой формат шифрования.
По желанию можете изменить папку хранения паролей.

# ENG
 SmartAutoPassword (AutoLogin) 1.12.2 with password saving and 'encryption' (using replace and Base64)

## Commands
- `-ap` - Enable automatic password entry
- `-sreg <password length>` - Register and automatically store a random password
- `-spass` - Enter password if it is saved

## Interacting with SmartPassword
When the chat contains the messages `/register <password>` or `/login <password>`, SmartPassword will offer to store the password:
- `-yes` - Store the password
- `-no` - Do not store the password

## Password storage
Your passwords are stored in the directory `C:\PlintoUtilsCfg\AutoPassword` in the file `AutoPassword.on.top`.
The file `AutoPassword.on.top` contains something along the lines of: `Username:Server:Password`.
The password has its own encryption format.
Moreover, you have the ability to modify the password storage directory.
