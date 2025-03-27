Remove-Item -Path "ScriptingTest/build/libs/ScriptingTest.jar" -ErrorAction SilentlyContinue

.\gradlew.bat :ScriptingTest:jar
.\gradlew.bat runImGuiTest