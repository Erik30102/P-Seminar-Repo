# Die Scripting Engine
die Scripting Engine ist dafür da das man später seinen eigenen code mit seinen selbst erstellten Componenten in einem seperaten projet erstellen kann(bie uns ScriptingTest) und dieser dann einfach in der Engine direkt geladen wird damit kann der enduser am ende erstenst nicht alles in der engine selbst zerstören weil er keinen zugriff auf diese hat und man kann wärend man in der engine ist aktive den code veränder und er kann live gereloeded werden.

### Componente mit der Scripting Engine laden
```java
ScriptingEngine scriptEngine = new ScriptingEngine("path/to/script.jar");
// das lädt dann alle klassen die von dem BaseComponent erben und läd die in ein Dictionary

// um jetzt ein Script aus diesem Dictionary raus zu bekommen schreibt mann einfach
BaseComponent newComponent = scriptEngine.GetNewComponent("com.Lib.Path.To.Class");
// und schon hat man die klasse geladen und erstellt
```
> Reload funktion ist noch nicht einporgramiert aber das kann man auch erst richtig implemintierne wenn der editor erstellt ist