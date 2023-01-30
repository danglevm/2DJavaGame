package adventureGame2D;

import entity.NPC_OldDude;
import object.Obj_boots;
import object.Obj_chest;
import object.Obj_door;
import object.Obj_key;

public class AssetPlacement {
	//Class managed object placement
	GamePanel gp;
	
	public AssetPlacement (GamePanel gp) {
		this.gp = gp;
	}
	
	public void setObject() {
		
	
	
	}
	
	public void setNPCs() {
		gp.npcs[0] = new NPC_OldDude(gp);
		gp.npcs[0].WorldX = 112*gp.tileSize;
		gp.npcs[0].WorldY = 135*gp.tileSize;
	}
}
