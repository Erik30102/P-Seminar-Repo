# Wie Starten
---
zum starten brauchst du:
- [JDK 23](https://www.oracle.com/de/java/technologies/downloads/#jdk23-windows)

Die Engien kann entweder als runtime oder als Editor geöffnet werden
- Runtime: das run.ps1 script ausführen
- Editor: das runEditor.ps1 script ausführen

Die Runtime wird zuerst nur funktionieren wenn bereits ein Asset Pack erstellt ist, dieses kann man im editor aktuell unter Generall Settings erstellen. Sobald dieses erstellt wurde sollte ein file test.assetPack im Editor ordner zu finden sein und die runtime sollte das spiel starten. **Aktuell kann mann weder das test.assetPack sowie andere Projekte erstellen also nur den ExampleProject ordner benutzen der automatisch geöffnet wird da alles hardcoded ist** soll aber noch gefixxed werden

## Wie kann mann Scenen verändern
---
Eine scene kann man verändern indem man im editor ein Entity erstellt oder auswählt und dann im Inspector(Sobald dieser fertig ist) diesem Components hinzufügen oder die Paramter dieser Components verändern

Ein Neues Component erstellt man indem man in dem Project ScriptingTest eine neue klasse hizufügt die von der klasse BaseComponent erbt diese wird dann automatisch von Editor sowie Runtime geladen und sollte im Inspect auswehlbar sein zu hinzufügen. Zusätzlich ist zu Erwähnen dass alle Variabeln die public sind im inspector bearbeitbar sind solang sie primitve oder spezielle(Assets, etc.) sind und sie werden auch nur serialized solang sie public sind.

***Für Genauere Dokumentationen zu unterschiedlichen Frameworks kann man in den Documentation ordner schauen oder in die JavaDoc***