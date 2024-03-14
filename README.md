# SmartAutoPassword
 SmartAutoPassword (AutoLogin) 1.12.2 + save passwords and 'encrypt' (with replace + Base64)

# Команды
- `-ap` - Включить авто ввод пароля
- `-sreg <длина пароля>` - Регистрируется и автоматически сохранит случайный пароль
- `-spass` - Вводит пароль, если он сохранен

## Взаимодействие с SmartPassword
Когда в чате присутствует сообщения `/register <пароль>` или `/login <пароль>`, SmartPassword предложит сохранить пароль:
- `-yes` - Сохранить пароль
- `-no` - Не сохранить пароль

## Хранение паролей
Ваши пароли хранятся в папке `C:\PlintoUtilsCfg\AutoPassword` в файле `AutoPassword.on.top`.
В файле `AutoPassword.on.top` записано примерно следующее: "Username:Server:Password".
Пароль имеет свой формат шифрования.
По желанию можете изменить папку хранения паролей.
