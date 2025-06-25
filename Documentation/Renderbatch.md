# Render Batch Documentation und alles was man dafür braucht
---
***Man sollte eigetnlich immer einfach enties mit spriteComponents Rendern da diese automatisch von der Engine übernommen wird***

## was ist ein Renderbatch
ein RenderBatcher fässt eigentlich nur viele verschiede sprites, also sachen die man zeichen will, welche vorher viele verschiedene draw calls sind in einen zusammen um performance erheblich zu verbessern. braucht mann wenn man auch nur mehr als 100 sprites auf einmal zeichnen will weil ne drawcall an den gpu zu senden insane aufwendig ist von performace her zusätzlich vereinfacht es den gesamten render process weil alles was man sonst mauell machen muss wie memory layout erstellen für einen gemacht wird.

## Rendern mit einem Renderbatch
### 1. Schritt: Renderbatch Camera Shader und Sprite Erstellen
> Erstellen von dem dem Shader. hierbei ist zu beachten das der try catch bald nichtmehr benötigt ist weil wir die exception dann einfach anders handeln. rest ist ziemlich selbsterklärend
```java
Shader shader;

try {
    shader = new Shader();
    shader.createVertexShader(vertexSourceString);
    shader.createFragmentShader(fragmentSourceString);
    shader.link();

}catch(Exception e) {
    e.printStackTrace();
}
```
> Erstllen vom eingentlichen Renderbatch
```java
RenderBatch renderBatch = new RenderBatch(SizeOfRenderbatch, shader);
```
> die SizeOfRenderBatch ist einfach wie viele sprites der Renderbatch beinhalten kann bevor er voll ist und man eien neune erstellen muss und der Shader ist einfach der Shader den der Renderbatch zum rendern benutzt dann. Beim Shader ist das vorprogrammierte Layout der Memory zu beachten. (Layout=0) vec3 pos; (layout=1) vec2 texCoords; (layout=2) int texId; wobei dabei auch noch die drei uniforms an den shader gesendet werden textures[8] welche alle Texturen beinhalten und mit der texId ausgewählt werden. mat4 projectionMatrix und mat4 viewMatrix welche die matrixen für die perspective und die camera verschiebung sind.

> jetzt letztlich das erstllen der Camera und des Sprites
```java
OrthographicCamera camera = new OrthographicCamera();
camera.Resize(widthDesScreens, heightDesScreens);
```
> auserdem muss die Camera immer wenn das window gerizied wird auch geresized werden das geht einfach indem man in die OnResize methode der Application class einfach ```camera.Resize(neueWidthDesScreens, neueHeightDesScreens);``` schreibt
```java
Texture testTexture = new Texture("path/to/texture.png");

Sprite sprite = new Sprite(testTexture, new float[] {
    0,1,
    1,0,
    0,0,
    1,1,
});
```
> hier ist das texture loden relativ selbsterklärend aber der zweite parameter des Sprites sind einfach die Texture coords was heißt von wo bis wo man die textur ladne soll also wenn mann jetzt alle 1 er mit 0.5 austauchen würde würde nurnoch ein 4tel der texture geladen werden und der rest nicht eigentlich jetzt noch relativ egal und einach die texture coords hernehmen aber später wenn wir große tilemaps haben wo viele verschieden sprites in einer texutre sind wird das benötigt aber da wird das auch alles automatisch ausgerechnet.

### 2. Schritt: der eignetliche Render Process

> zuerst wird erstmal der letzt frame gecleard vom screen damit wir nicht noch teile von dem frame davor auf dem screen haben
```java
RenderApi.clear();
RenderApi.setClearColor(0.1f, 0.1f, 0.1f);
```
> der erste clear command clear den screen und der zweite änder die hintergrund farbe das nicht alles komplett schwarz ist.

> dannach kann auch schon gezeichnet werden
```java
// zeigen das jetzt angefanen wird zu zeichnen
renderBatch.begin();

// einen sprite hochladen der zweitet paramteter ist einfach die position roation und scale des objectes
renderBatch.AddSprite(sprite, new Transfrom());

// läd alles auf den gpu und rendert aus perspective der angegebene camera
renderBatch.UpdateAndRender(camera);
```
> die kommentare erklären alles glaub relativ gut wenn wer fragen hat soll er mich(erik) einfach anschreiben