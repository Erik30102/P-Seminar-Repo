param (
    [switch]$noRecompileProject
)

if ($noRecompileProject) {
    .\gradlew.bat :Editor:runEditor
} else {
    Remove-Item -Path "ScriptingTest/build/libs/ScriptingTest.jar" -ErrorAction SilentlyContinue
    .\gradlew.bat :ScriptingTest:jar
    .\gradlew.bat :Editor:runEditor
}