package com.ScriptingTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

import com.Pseminar.Logger;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.BuiltIn.Tilemap;
import com.Pseminar.ECS.Entity;
import com.Pseminar.ECS.BuiltIn.BaseComponent;
import com.Pseminar.ECS.BuiltIn.TilemapComponent;
import com.Pseminar.Physics.IContactListener;
import com.Pseminar.Physics.Physics2D;
import com.Pseminar.Physics.PhysicsBody;
import com.Pseminar.Physics.Collider.BoxCollider;
import com.Pseminar.Physics.PhysicsBody.BodyType;
import com.ScriptingTest.OpeningInfo.Opening;

public class DungeonGenerator extends BaseComponent implements IContactListener {
    private transient RoomInfo[] roomInfos;

    /*
    Das ganze benutzt nicht wirkliche collision testing das heißt eigentlich könne levl überlappen
    wenn man drüber nachdenkt aber glaub wärhend dem spielen wird mans ned bemerken und es ist sowiso 
    immer nur ein level geladen also eier lecken sag ich
    */

    private transient Room currentRoom;
    private transient boolean isInitialized;

    private transient TilemapComponent tilemapComponent;
    private transient List<PhysicsBody> triggerCollider;

    private transient Entity player;
    private transient OpeningInfo lastEntrance;

    @Override
    public void OnStart() {
        // alle anfangs daten müssen hier spezifiziert werden weil die runtime ned den 
        // constructor called
        isInitialized = false;
        roomInfos = new RoomInfo[] {
            new RoomInfo(new Vector2f(), ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(1115220569), new OpeningInfo[] { new OpeningInfo(Opening.EAST, new Vector2f(15f, 8.5f))}),
            new RoomInfo(new Vector2f(), ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(-106776724), new OpeningInfo[] { new OpeningInfo(Opening.WEST, new Vector2f(-1,4.5f))}),
        };
        triggerCollider = new ArrayList<>();

        GenNewDungeon();
        Physics2D.GetInstance().AddNewPhysicsListener(this);

    }

    //#region Dungeon Genration

    private static final int MAX_WIDTH_MAP = 8;
    private static final int MAX_HEIGHT_MAP = 8;

    private transient RoomInfo[] generationBuffer;

    public void GenNewDungeon() {
        generationBuffer = new RoomInfo[MAX_WIDTH_MAP * MAX_HEIGHT_MAP];
        alreadyGeneratedRooms = new HashMap<>();

        RoomInfo startRoom = roomInfos[new Random().nextInt(roomInfos.length)];
        generationBuffer[(MAX_HEIGHT_MAP / 2) * MAX_WIDTH_MAP + MAX_WIDTH_MAP/2] = startRoom;
        for (OpeningInfo o : startRoom.opening) {
            GenNextRoom(o, MAX_WIDTH_MAP / 2 , MAX_HEIGHT_MAP/2);
        }

        // das eingentlich echt dumm gemacht grad das man das ganze so in 2 schritten macht
        // aber ich bin bei echter collisions abfrage gerage quitted und machs jetzt einfach so
        // weils am einfachsten ist der code wird sowiso nur ein mal gerunnt also is jucke

        currentRoom = TurnArrayIntoTree((MAX_WIDTH_MAP / 2), MAX_HEIGHT_MAP/2);
        lastEntrance = startRoom.opening[0];
        alreadyGeneratedRooms.clear();
    }

    // Digga das ist ja mal so unglaublich aids wtf
    private transient Map<RoomInfo, Room> alreadyGeneratedRooms;

    private Room TurnArrayIntoTree(int x, int y) {
        RoomInfo info = generationBuffer[x + y * MAX_WIDTH_MAP];
        if(alreadyGeneratedRooms.get(info) != null) {
            return alreadyGeneratedRooms.get(info);
        }
        
        Map<Opening, Room> connectedRooms = new HashMap<>();
        
        Room genRoom = new Room(info, connectedRooms);
        alreadyGeneratedRooms.put(info, genRoom);
        
        for (OpeningInfo openingInfo : info.opening) {
            Room adjastenRoom = TurnArrayIntoTree(x + openingInfo.Direction.GetOffsetX(), y + openingInfo.Direction.GetOffsetY());
            connectedRooms.put(openingInfo.Direction, adjastenRoom);
        }

        return genRoom;
    }

    public void GenNextRoom(OpeningInfo direction, int startingX, int startingY) {
        // TODO: einfach lists benuten
        Object[] possibleRooms = Arrays.stream(this.roomInfos).filter(t -> {
            return Arrays.stream(t.opening).anyMatch(y -> y.Direction == direction.Direction.GetOpposite());
        }).toArray();

        RoomInfo possibleRoom = (RoomInfo)possibleRooms[new Random().nextInt(possibleRooms.length)];
        for (OpeningInfo openingInfo : possibleRoom.opening) {
            if(openingInfo.Direction != direction.Direction.GetOpposite()) {
                if(generationBuffer[startingY * MAX_WIDTH_MAP + startingX + openingInfo.Direction.GetOffset(MAX_WIDTH_MAP, MAX_HEIGHT_MAP)] != null) {
                    GenNextRoom(direction, startingX, startingY);
                    return;
                }
            }
        }

        generationBuffer[startingY * MAX_WIDTH_MAP + startingX + direction.Direction.GetOffset(MAX_WIDTH_MAP, MAX_HEIGHT_MAP)] = possibleRoom;

        for (OpeningInfo openingInfo : possibleRoom.opening) {
            if(openingInfo.Direction != direction.Direction.GetOpposite()) {
                GenNextRoom(openingInfo, startingX + direction.Direction.GetOffsetX(), startingY + direction.Direction.GetOffsetY());
            }
        }
    }

//#endregion

    @Override
    public void OnUpdate(float dt) {
        if(player == null)
            player = this.GetEntity().GetScene().GetEntityByName("Player");
        else if(!isInitialized) {
            UpdateTileMap();

            isInitialized = true;
        }
    }

    private void UpdateTileMap() {
        Logger.info("Updating Tile map from DUngeon Gen");

        for (PhysicsBody body : triggerCollider) {
            body.Destroy();
        }

        triggerCollider.clear();

        tilemapComponent = this.GetEntity().GetComponent(TilemapComponent.class);
        tilemapComponent.SetTilemap(currentRoom.roomInfo.tilemap);
        tilemapComponent.GetTilemap().InitPhysics(this.GetEntity().transform.GetPosition());

        for (OpeningInfo openingInfo : currentRoom.roomInfo.opening) {
            Vector2f position = openingInfo.position;

            PhysicsBody body = new PhysicsBody(BodyType.STATIC);
            body.AddCollider(new BoxCollider(1, 2), true);
            body.SetPosition(position.add(this.GetEntity().transform.GetPosition(), new Vector2f()));

            body.SetUserData(openingInfo);

            triggerCollider.add(body);
        }

        this.player.ForceSetPosition(lastEntrance.position.add(this.GetEntity().transform.GetPosition(), new Vector2f()).add(lastEntrance.Direction.GetOpposite().GetOffsetX() * 2, lastEntrance.Direction.GetOpposite().GetOffsetY() * 2));
    }

    @Override
    public void OnContactBegin(Contact contact) {
        Object a = contact.getFixtureA().m_body.getUserData();
        Object b = contact.getFixtureB().m_body.getUserData();
    
        if(a instanceof OpeningInfo && b instanceof TestComponent) {
            OpeningInfo WhereToGo = (OpeningInfo)a;

            currentRoom = currentRoom.connectedRooms.get(WhereToGo.Direction);
            for (OpeningInfo openingInfo : currentRoom.roomInfo.opening) {
                if(openingInfo.Direction == WhereToGo.Direction.GetOpposite()) {
                    lastEntrance = openingInfo;
                }
            }
            isInitialized = false;
        } else if (b instanceof OpeningInfo && a instanceof TestComponent){
            OpeningInfo WhereToGo = (OpeningInfo)b;

            currentRoom = currentRoom.connectedRooms.get(WhereToGo.Direction);
            for (OpeningInfo openingInfo : currentRoom.roomInfo.opening) {
                if(openingInfo.Direction == WhereToGo.Direction.GetOpposite()) {
                    lastEntrance = openingInfo;
                }
            }
            isInitialized = false;
        }
    }

    @Override
    public void OnContactEnd(Contact contact) {

    }

    @Override
    public void OnDispose() {
        Physics2D.GetInstance().RemovePhysicsListener(this);
    }
}

class OpeningInfo {
    public enum Opening {
        NORTH, SOUTH, EAST, WEST;

        public Opening GetOpposite() {
            switch (this) {
                case EAST:
                    return WEST;
                case WEST:
                    return EAST;
                case NORTH:
                    return SOUTH;
                case SOUTH:
                    return NORTH;
                default:
                    return null;
            }
        }

        public int GetOffset(int width, int heigth) {
            switch (this) {
                case EAST:
                    return 1;
                case WEST:
                    return -1;
                case NORTH:
                    return width;
                case SOUTH:
                    return -width;
                default:
                    return 0;
            }
        }

        public int GetOffsetX() {
            switch (this) {
                case EAST:
                    return 1;
                case WEST:
                    return -1;
                default:
                    return 0;
            }
        }

        public int GetOffsetY() {
            switch (this) {
                case NORTH:
                    return 1;
                case SOUTH:
                    return -1;
                default:
                    return 0;
            }
        }
    };

    Opening Direction;
    Vector2f position;

    public OpeningInfo(Opening opening, Vector2f position) {
        this.position = position;
        Direction = opening;
    }
}

class RoomInfo {
    public Vector2f startPosition;
    public Tilemap tilemap;

    public OpeningInfo[] opening;

    public RoomInfo(Vector2f startPosition, Tilemap tilemap, OpeningInfo[] openings) {
        this.startPosition = startPosition;
        this.tilemap = tilemap;
        this.opening = openings;
    }
}

class Room {
    public RoomInfo roomInfo;
    public Map<Opening, Room> connectedRooms;

    public Room(RoomInfo roomInfo, Map<Opening, Room> connectedRooms) {
        this.roomInfo = roomInfo;
        this.connectedRooms = connectedRooms;
    }
}