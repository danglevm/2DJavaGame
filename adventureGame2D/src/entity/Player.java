package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import adventureGame2D.GamePanel;
import adventureGame2D.KeyHandler;
import adventureGame2D.UI;
import adventureGame2D.UtilityTool;

public class Player extends Entity {
	
	//Variables
	KeyHandler keyH;
	BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
	

	//Where the player is drawn on the screen - camera 
	public final int screenX;
	public final int screenY;
	
	private boolean switchOpacity = false;
	private int switchOpacityCounter = 0, attackCooldownPeriod = 60, attackStamina = 0;
	public boolean playerAttack = false;
	
	
	
	//-------------------------------CONSTRUCTORS------------------
	public Player (GamePanel gp, KeyHandler keyH) {
		super(gp);
		this.keyH = keyH;
		
		//Places the character at the center of the screen
		//Subtract half of the tile length to be at the center of the player character
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		this.setDefaultPlayerValues();
		this.getPlayerImage();
		this.getPlayerAttackImage();
	}
	
	//-------------------------------CLASS METHODS------------------
	private final void getPlayerImage() {
		
		attackUp1 = setupCharacter("boy_attack_up_1", "/player_attack/", gp.tileSize, gp.tileSize*2);
		attackUp2 = setupCharacter("boy_attack_up_2", "/player_attack/", gp.tileSize, gp.tileSize * 2);
		attackDown1 = setupCharacter("boy_attack_down_1", "/player_attack/", gp.tileSize, gp.tileSize * 2);
		attackDown2 = setupCharacter("boy_attack_down_2", "/player_attack/", gp.tileSize, gp.tileSize * 2);
		attackLeft1 = setupCharacter("boy_attack_left_1", "/player_attack/", gp.tileSize * 2, gp.tileSize);
		attackLeft2 = setupCharacter("boy_attack_left_2", "/player_attack/", gp.tileSize * 2, gp.tileSize);
		attackRight1 = setupCharacter("boy_attack_right_1", "/player_attack/", gp.tileSize * 2, gp.tileSize);
		attackRight2 = setupCharacter("boy_attack_right_2", "/player_attack/", gp.tileSize * 2, gp.tileSize);
		
		
	}
	
	private final void getPlayerAttackImage() {
		
		up1 = setupCharacter("boy_up_1", "/player/", gp.tileSize, gp.tileSize);
		up2 = setupCharacter("boy_up_2", "/player/", gp.tileSize, gp.tileSize);
		down1 = setupCharacter("boy_down_1", "/player/", gp.tileSize, gp.tileSize);
		down2 = setupCharacter("boy_down_2", "/player/", gp.tileSize, gp.tileSize);
		left1 = setupCharacter("boy_left_1", "/player/", gp.tileSize, gp.tileSize);
		left2 = setupCharacter("boy_left_2", "/player/", gp.tileSize, gp.tileSize);
		right1 = setupCharacter("boy_right_1", "/player/", gp.tileSize, gp.tileSize);
		right2 = setupCharacter ("boy_right_2", "/player/", gp.tileSize, gp.tileSize);
		
	}
	
	
	private final void setDefaultPlayerValues() {
		//Default player values
		WorldX = gp.tileSize * 122;
		WorldY= gp.tileSize * 132;
		speed = 3;
		entityType = 0;
		direction = "down";
		maxLife = 8;
		life = maxLife;
		
		//x, y, width, length
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 8;
		//Default values so x and y values of the rectangle can be changed later
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 24;
		//attack cooldown period has not 
		solidArea.height = 36;
		
	}
	
	
	
	public void update() 
	{
							
		if (playerAttack && (attackStamina == attackCooldownPeriod)) 
		{
		
			++spriteCounter;
			
			//4 frames of short attack animation
			if (spriteCounter < 6) {
				spriteNum = true;
			//next 20 frames of long attack animation
			} else if (spriteCounter < 25) {
				spriteNum = false;
			//4 frames of receding attack animation
			} else {
				spriteNum = true;
				spriteCounter = 0;
				playerAttack = false;
				attackStamina -= attackCooldownPeriod;
				System.out.println(attackStamina);
				
			}
			
		} else if (keyH.upPressed||keyH.downPressed||
				keyH.leftPressed||keyH.rightPressed || keyH.dialoguePressed)
		{
			//X and Y values increase as the player moves right and down
			//Check tile collision
			collisionOn = false;
			
			//monster collision
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monsters);
			//this might return 9999
			if (monsterIndex != 9999) {
				gp.monsters.get(monsterIndex).damageContact(this);
			}
			
			//Collision checker receive subclass
			gp.cChecker.CheckTile(this);
			
			//Check event collision
			gp.eHandler.checkEvent();
			
			//Check object collision
			int objIndex = gp.cChecker.checkObject(this, true);
			ObjectPickUp(objIndex);
			
			
			//check NPC collision
			int npcIndex = gp.cChecker.checkEntity(this, gp.NPCs);
			collisionNPC(npcIndex);
			
	
			if (keyH.upPressed) {
				this.direction = "up";
				if (!collisionOn && !keyH.dialoguePressed) {this.WorldY -= speed;} 
			} 
			if (keyH.downPressed) {
				this.direction = "down";
				if (!collisionOn && !keyH.dialoguePressed) {this.WorldY += speed;}
			} 
			if (keyH.leftPressed) {
				this.direction = "left";
				if (!collisionOn && !keyH.dialoguePressed) {this.WorldX -= speed;}
			} 
			if (keyH.rightPressed) {
				this.direction = "right";
				if (!collisionOn && !keyH.dialoguePressed) {this.WorldX += speed;}
			}
		
			
			
			
			
			
			++spriteCounter;
			//Player image changes every 12 frames
			if (spriteCounter > 12) {
				if (spriteNum) {
					spriteNum = false;
				}
				
				else if (!spriteNum) {
					spriteNum = true;
				}
				spriteCounter = 0;
			}
			
		}//keypress loop
		
		//Call superclass invincibility time
		this.checkInvincibilityTime();
		
	}//update
	
	public void ObjectPickUp(int index) {
		
		if (index != 9999) {
			

	}
	}
	
	private final void collisionNPC (int i) {
		if (i != 9999) {
			//player touching npc
			if (keyH.dialoguePressed) {
				gp.gameState = gp.dialogueState;
				gp.NPCs.get(i).speak();
				keyH.dialoguePressed = false; 
			}
		}
	
	}
	
	public void draw(Graphics2D g2) {
		/*
		g2.setColor(Color.yellow);
		g2.fillRect(x, y, gp.tileSize,gp.tileSize );
		*/
		
		//attack cooldown period has not 
		BufferedImage image = null;
		int tempScreenX = screenX, tempScreenY = screenY;
		if (attackStamina < attackCooldownPeriod) {
			++attackStamina;
		}
	
		//attack cool down period is on
		if (!playerAttack || attackStamina < attackCooldownPeriod) {
		switch (direction) {
		case "up":
			if (spriteNum) {image = up1;} else {image = up2;} break;
		case "down":
			if (spriteNum) {image = down1;} else {image = down2;} break;
		case "left":
			if (spriteNum) {image = left1;} else {image = left2;} break;
		case "right":
			if (spriteNum) {image = right1;} else {image = right2;} break;
			}
		
		
		} //attack cool down period is over - stamina
			else if (playerAttack && (attackStamina == attackCooldownPeriod) )
		{
		
		switch (direction) {
		case "up":
			tempScreenY = screenY - gp.tileSize;
			if (spriteNum) {image = attackUp1;} else {image = attackUp2;} break;
		case "down":
			if (spriteNum) {image = attackDown1;} else {image = attackDown2;} break;
		case "left":
			tempScreenX = screenX - gp.tileSize;
			if (spriteNum) {image = attackLeft1;} else {image = attackLeft2;} break;
		case "right":
			if (spriteNum) {image = attackRight1;} else {image = attackRight2;} break;
			}	
		} 
		
		//Draw effect when player gets damaged
		if (this.invincibility) {
			++this.switchOpacityCounter;
			if (!switchOpacity && switchOpacityCounter > 3) {
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));	
				switchOpacity = true;
				switchOpacityCounter = 0;
			} else {
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));	
				switchOpacity = false;
			}
		}
		//16 pixels
		g2.drawImage(image, tempScreenX, tempScreenY, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		
		
			
	}
	
	@Override
	protected final void checkInvincibilityTime() {

		if (this.invincibility) {
		++this.invincibilityCounter;
		if (this.invincibilityCounter > 90) {
			this.invincibility = false;
			this.invincibilityCounter = 0;
			
			}
		}
	}
	
}
