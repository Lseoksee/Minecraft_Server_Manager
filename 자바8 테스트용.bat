@echo off
for %%a in (*.jar) do for /f "tokens=1,2 delims=-" %%b in ("%%a") do (
if "%%b"=="build" "C:\Program Files\Java\jre1.8.0_351\bin\java.exe" -jar %%b-%%c
)