FOR /F "tokens=5 delims= " %%P IN ('netstat -a -n -o ^| findstr :%1.*LISTENING') DO taskkill /F /PID %%P

