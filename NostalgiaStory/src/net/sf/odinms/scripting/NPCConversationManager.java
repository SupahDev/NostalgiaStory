/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@odinms.de>
                       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation. You may not use, modify
    or distribute this program under any other version of the
    GNU Affero General Public License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.odinms.scripting;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.sf.odinms.client.IItem;
import net.sf.odinms.client.Item;
import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.client.MapleInventory;
import net.sf.odinms.client.MapleInventoryType;
import net.sf.odinms.client.MapleJob;
import net.sf.odinms.client.MapleQuestStatus;
import net.sf.odinms.client.MapleStat;
import net.sf.odinms.client.SkillFactory;
import net.sf.odinms.server.life.MapleLifeFactory;
import net.sf.odinms.server.life.MapleMonster;
import net.sf.odinms.server.life.MapleMonsterStats;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.server.MapleInventoryManipulator;
import net.sf.odinms.server.MapleItemInformationProvider;
import net.sf.odinms.server.MapleShopFactory;
import net.sf.odinms.server.maps.MapleMap;
import net.sf.odinms.server.maps.MapleMapObject;
import net.sf.odinms.server.maps.MapleMapObjectType;
import net.sf.odinms.server.maps.MapleReactor;
import net.sf.odinms.server.quest.MapleQuest;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.server.MaplePortal;
import net.sf.odinms.net.world.MapleParty;

/**
 *
 * @author Matze
 */
public class NPCConversationManager {

	private MapleClient c;
	private int npc;
	private String getText;

	public NPCConversationManager(MapleClient c, int npc) {
		this.c = c;
		this.npc = npc;
	}

	public void dispose() {
		NPCScriptManager.getInstance().dispose(this);
	}

	public void sendNext(String text) {
		c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01"));
	}

	public void sendPrev(String text) {
		c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00"));
	}

	public void sendNextPrev(String text) {
		c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01"));
	}

	public void sendOk(String text) {
		c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00"));
	}

	public void sendYesNo(String text) {
		c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, ""));
	}

	public void sendAcceptDecline(String text) {
		c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 2, text, ""));
	}

	public void sendSimple(String text) {
		c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 5, text, ""));
	}

	public void sendStyle(String text, int styles[]) {
		c.getSession().write(MaplePacketCreator.getNPCTalkStyle(npc, text, styles));
	}

	public void sendGetNumber(String text, int def, int min, int max) {
		c.getSession().write(MaplePacketCreator.getNPCTalkNum(npc, text, def, min, max));
	}

	public void sendGetText(String text) {
		c.getSession().write(MaplePacketCreator.getNPCTalkText(npc, text));
	}

	public void setGetText(String text) {
		this.getText = text;
	}

	public String getText() {
		return this.getText;
	}

	public void warp(int map) {
		MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(map);
		c.getPlayer().changeMap(target, target.getPortal(0));
	}

	public void warp(int map, int portal) {
		MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(map);
		c.getPlayer().changeMap(target, target.getPortal(portal));
	}

	public void warp(int map, String portal) {
		MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(map);
		c.getPlayer().changeMap(target, target.getPortal(portal));
	}

	public void openShop(int id) {
		MapleShopFactory.getInstance().getShop(id).sendShop(c);
	}

	public boolean haveItem(int itemid) {
		return haveItem(itemid, 1);
	}
	
	public boolean haveItem(int itemid, int quantity) {
		return haveItem(itemid, quantity, false, true);
	}
	
	public boolean haveItem(int itemid, int quantity, boolean checkEquipped, boolean greaterOrEquals) {
		MapleInventoryType type = MapleItemInformationProvider.getInstance().getInventoryType(itemid);
		MapleInventory iv = c.getPlayer().getInventory(type);
		int possesed = iv.countById(itemid);
		if (checkEquipped) {
			possesed += c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).countById(itemid);
		}
		if (greaterOrEquals) {
			return possesed >= quantity;
		} else {
			return possesed == quantity;
		}
	}
	
	public int itemQuantity(int itemid, int whee) {
		MapleInventoryType type = MapleItemInformationProvider.getInstance().getInventoryType(itemid);
		MapleInventory iv = c.getPlayer().getInventory(type);
		int possesed = iv.countById(itemid);
		if (whee == 0)
		possesed += c.getPlayer().getInventory(MapleInventoryType.EQUIP).countById(itemid);
		else if (whee == 1)
			possesed += c.getPlayer().getInventory(MapleInventoryType.USE).countById(itemid);
		else if (whee == 2)
			possesed += c.getPlayer().getInventory(MapleInventoryType.SETUP).countById(itemid);
		else if (whee == 3)
			possesed += c.getPlayer().getInventory(MapleInventoryType.ETC).countById(itemid);
		else if (whee == 4)
			possesed += c.getPlayer().getInventory(MapleInventoryType.CASH).countById(itemid);
		return possesed;
	}

	public MapleQuestStatus.Status getQuestStatus(int id) {
		return c.getPlayer().getQuest(MapleQuest.getInstance(id)).getStatus();
	}

	/**
	 * Gives item with the specified id or takes it if the quantity is negative. Note that this does NOT take items from the equipped inventory.
	 * @param id
	 * @param quantity
	 */
	public void gainItem(int id, short quantity) {
		if (quantity >= 0) {
			StringBuilder logInfo = new StringBuilder(c.getPlayer().getName());
			logInfo.append(" received ");
			logInfo.append(quantity);
			logInfo.append(" from a NPC conversation with NPC id ");
			logInfo.append(npc);
			MapleInventoryManipulator.addById(c, id, quantity, logInfo.toString());
		} else {
			MapleInventoryManipulator.removeById(c, MapleItemInformationProvider.getInstance().getInventoryType(id), id, -quantity, true, false);
		}
		c.getSession().write(MaplePacketCreator.getShowItemGain(id,quantity, true));
	}

	public void changeJob(MapleJob job) {
		c.getPlayer().changeJob(job);
	}

	public MapleJob getJob() {
		return c.getPlayer().getJob();
	}

	public void startQuest(int id) {
		MapleQuest.getInstance(id).start(c.getPlayer(), npc);
	}

	public void completeQuest(int id) {
		MapleQuest.getInstance(id).complete(c.getPlayer(), npc);
	}

	public void forfeitQuest(int id) {
		MapleQuest.getInstance(id).forfeit(c.getPlayer());
	}

	public int getMeso() {
		return c.getPlayer().getMeso();
	}

	public void gainMeso(int gain) {
		c.getPlayer().gainMeso(gain, true, false, true);
	}

	public void gainExp(int gain) {
		c.getPlayer().gainExp(gain, true, true);
	}

	public int getNpc() {
		return npc;
	}

	public int getLevel() {
		return c.getPlayer().getLevel();
	}

	public void unequipEverything() {
		MapleInventory equipped = getChar().getInventory(MapleInventoryType.EQUIPPED);
		MapleInventory equip = getChar().getInventory(MapleInventoryType.EQUIP);
		List<Byte> ids = new LinkedList<Byte>();
		for (IItem item : equipped.list()) {
			ids.add(item.getPosition());
		}
		for (byte id : ids) {
			MapleInventoryManipulator.unequip(getC(), id, equip.getNextFreeSlot());
		}
	}

	public void teachSkill(int id, int level, int masterlevel) {
		c.getPlayer().changeSkillLevel(SkillFactory.getSkill(id), level, masterlevel);
	}

	public MapleCharacter getChar() {
		return c.getPlayer();
	}

	public MapleClient getC() {
		return c;
	}

	public void rechargeStars() {
		MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		IItem stars = getChar().getInventory(MapleInventoryType.USE).getItem((byte) 1);
		if (ii.isThrowingStar(stars.getItemId())) {
			stars.setQuantity(ii.getSlotMax(stars.getItemId()));
			getC().getSession().write(MaplePacketCreator.updateInventorySlot(MapleInventoryType.USE, (Item) stars));
		}
	}

	public EventManager getEventManager(String event) {
		return c.getChannelServer().getEventSM().getEventManager(event);
	}
	
	public void showEffect(String effect) {
		c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.showEffect(effect));
	}
	
	public void playSound(String sound) {
		c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.playSound(sound));
	}
        
        public void setHair(int hair) {
	        c.getPlayer().setHair(hair);
	        c.getPlayer().updateSingleStat(MapleStat.HAIR, hair);
	        c.getPlayer().equipChanged();
	}
	
	public void setFace(int face) {
	        c.getPlayer().setFace(face);
	        c.getPlayer().updateSingleStat(MapleStat.FACE, face);
	        c.getPlayer().equipChanged();
	}
	
	public void setSkin(int color) {            
	        c.getPlayer().setSkinColor(c.getPlayer().getSkinColor().getById(color));
	        c.getPlayer().updateSingleStat(MapleStat.SKIN, color);
	        c.getPlayer().equipChanged();
        }
        
	public void updateJQ() {
                c.getPlayer().updateJQ();
        }
        
        public boolean grabJQ () {
                return c.getPlayer().grabJQ();
        }	
	
	public int PlayerCount(int mapid) {
		ChannelServer cserv = c.getChannelServer();
		MapleMap map = cserv.getMapFactory().getMap(mapid);
		int count = map.getCharacters().size();
		return count;
	}
	
	public void PortalOpen(int mapid, int portalid, boolean open) {
		ChannelServer cserv = c.getChannelServer();
		MapleMap map = cserv.getMapFactory().getMap(mapid);
		MaplePortal portal = map.getPortal(portalid);
		portal.setOpen(open);
	}
	
	public void setPortal(int mapid, int portalid, boolean open) {
		ChannelServer cserv = c.getChannelServer();
		MapleMap map = cserv.getMapFactory().getMap(mapid);
		MaplePortal portal = map.getPortal(portalid);
		portal.setOpen(open);
	}
	
	public MapleParty getParty() {
		return c.getPlayer().getParty();
	}
	
	public void spawnMob(int mapid, int mid, int xpos, int ypos) {
		ChannelServer cserv = c.getChannelServer();
		MapleMap map = cserv.getMapFactory().getMap(mapid);
		MapleMonster mob = MapleLifeFactory.getMonster(mid);
		Point spawnpoint = new Point(xpos, ypos);
		map.spawnMonsterOnGroudBelow(mob, spawnpoint);
	}
	
	public boolean isPartyLeader() {
			return c.getPlayer().isPartyLeader();
	}
}
