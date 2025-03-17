
Remove-Item -Path "ScriptingTest/build/libs/ScriptingTest.jar" -ErrorAction SilentlyContinue
Start-Process -FilePath ".\gradlew.bat" -ArgumentList ":ScriptingTest:jar" -NoNewWindow -Wait
Start-Process -FilePath ".\gradlew.bat" -ArgumentList ":Sandbox:run" -NoNewWindow
Start-Process -FilePath ".\gradlew.bat" -ArgumentList "runImGuiTest" -NoNewWindow

