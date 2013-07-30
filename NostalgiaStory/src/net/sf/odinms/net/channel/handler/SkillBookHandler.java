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

package net.sf.odinms.net.channel.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.odinms.client.IItem;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.client.MapleInventoryType;
import net.sf.odinms.net.AbstractMaplePacketHandler;
import net.sf.odinms.server.MapleInventoryManipulator;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.tools.data.input.SeekableLittleEndianAccessor;
import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.Skill;
import net.sf.odinms.client.ISkill;
import net.sf.odinms.client.SkillFactory;
	
public class SkillBookHandler extends AbstractMaplePacketHandler {	
	private static Logger log = LoggerFactory.getLogger(ScrollHandler.class);
	
	// Create a new instance
	public SkillBookHandler() {
	}
	public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c){
		if (!c.getPlayer().isAlive()) {
			c.getSession().write(MaplePacketCreator.enableActions());
			return;
		} 
		slea.readInt(); // teh ignored.
		byte slot = (byte)slea.readShort();
		int itemId = slea.readInt();
		MapleCharacter player = c.getPlayer();
		int skill = 0;
		int maxlevel = 0;
		//List<IItem> existing = c.getPlayer().getInventory(MapleInventoryType.USE).listById(itemId);
		IItem toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
		if (toUse != null && toUse.getQuantity() == 1) {
			if (toUse.getItemId() != itemId) {
				log.info("[h4x] Player {} is using a SkillBook not in the slot: {}", c.getPlayer().getName(), Integer.valueOf(itemId));
			}
			MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
			switch (toUse.getItemId()) {
			//level 10 books
			case 2280000 : skill = 2121003; maxlevel = 10; break;
			case 2280001 : skill = 4120002; maxlevel = 10; break;
			case 2280002 : switch (c.getPlayer().getJob()) { // Maple Warrior
			case HERO : skill = 1121000; maxlevel = 10; break; 
			case PALADIN : skill = 1221000; maxlevel = 10; break; 
			case DARKKNIGHT : skill = 1321000; maxlevel = 10; break;
			case FP_ARCHMAGE : skill = 2121000; maxlevel = 10; break;
			case IL_ARCHMAGE : skill = 2221000; maxlevel = 10; break;
			case BISHOP : skill = 2321000; maxlevel = 10; break;
			case BOWMASTER : skill = 3121000; maxlevel = 10; break;
			case CROSSBOWMASTER : skill = 3221000; maxlevel = 10; break;
			case NIGHTLORD : skill = 4121000; maxlevel = 10; break;
			case SHADOWER : skill = 4221000; maxlevel = 10; break;
			}
			case 2280003 : switch (c.getPlayer().getJob()) { //Hero's Will
			case HERO : skill = 1121011; maxlevel = 1; break; 
			case PALADIN : skill = 1221012; maxlevel = 1; break; 
			case DARKKNIGHT : skill = 1321010; maxlevel = 1; break;
			case FP_ARCHMAGE : skill = 2121008; maxlevel = 1; break;
			case IL_ARCHMAGE : skill = 2221008; maxlevel = 1; break;
			case BISHOP : skill = 2321009; maxlevel = 1; break;
			case BOWMASTER : skill = 3121009; maxlevel = 1; break;
			case CROSSBOWMASTER : skill = 3221008; maxlevel = 1; break;
			case NIGHTLORD : skill = 4121009; maxlevel = 1; break;
			case SHADOWER : skill = 4221008; maxlevel = 1; break;
			}
			case 2280004 : switch (c.getPlayer().getJob()) {
			case FP_ARCHMAGE : skill = 2121004; maxlevel = 10; break;
			case IL_ARCHMAGE : skill = 2221004; maxlevel = 10; break;
			case BISHOP : skill = 2321004; maxlevel = 10; break;
			}
			case 2280005 : switch (c.getPlayer().getJob()) {
			case BOWMASTER : skill = 3121003; maxlevel = 10; break;
			case CROSSBOWMASTER :  skill = 3221003; maxlevel = 10; break;
			}
			case 2280006 : switch (c.getPlayer().getJob()) {
			case NIGHTLORD :  skill = 4121003; maxlevel = 10; break;
			case SHADOWER : skill = 4221003; maxlevel = 10; break;
			}
			case 2280007 : skill = 1120003; maxlevel = 10; break; //Advanced Combo
			case 2280008 : skill = 1220010; maxlevel = 10; break;
			case 2280009 : skill = 2321007; maxlevel = 10; break;
			case 2280010 : skill = 4121007; maxlevel = 10; break;
			case 2280011 : skill = 2221003; maxlevel = 10; break;
			case 2280012 : switch (c.getPlayer().getJob()) {
			case HERO : skill = 1121006; maxlevel = 10; break;
			case PALADIN : skill = 1221007; maxlevel = 10; break;
			case DARKKNIGHT : skill = 1321003; maxlevel = 10; break;
			}
			//level 20 books
			case 2290000 : switch (c.getPlayer().getJob()) { //Monster Magnet
			case HERO : skill = 1121001; maxlevel = 20; break;
			case PALADIN : skill = 1221001; maxlevel = 20; break;
			case DARKKNIGHT : skill = 1321001; maxlevel = 20; break;
			}
			case 2290002 : switch (c.getPlayer().getJob()) { // Achilles
			case HERO : skill = 1120004; maxlevel = 20; break;
			case PALADIN : skill = 1220005; maxlevel = 20; break;
			case DARKKNIGHT : skill = 1320005; maxlevel = 20; break;			
			}
			case 2290004 : switch (c.getPlayer().getJob()) { // Rush
			case HERO : skill = 1121006; maxlevel = 20; break;
			case PALADIN : skill = 1221007; maxlevel = 20; break;
			case DARKKNIGHT : skill = 1321003; maxlevel = 20; break;
			}
			case 2290006 : switch (c.getPlayer().getJob()) { // Power stance
			case HERO : skill = 1121002; maxlevel = 20; break;
			case PALADIN : skill = 1221002; maxlevel = 20; break;
			case DARKKNIGHT : skill = 1321003; maxlevel = 20; break;
			}
			case 2290008 : skill = 1120003; maxlevel = 20; break; //Advanced Combo
			case 2290010 : skill = 1121008; maxlevel = 20; break; // Brandish
			case 2290012 : skill = 1221009; maxlevel = 20; break; // Blast
			case 2290014 : skill = 1120005; maxlevel = 20; break; //Guardian
			case 2290016 : skill = 1121010; maxlevel = 20; break; // Enrage
			case 2290018 : skill = 1221003; maxlevel = 20; break; // Holy Charge : Sword
			case 2290019 : skill = 1221004; maxlevel = 20; break; // Divine Charge : BW
			case 2290020 : skill = 1221011; maxlevel = 20; break; // Heaven's Hammer
			case 2290022 : skill = 1320006; maxlevel = 20; break; // Berserk
			
			//Magician Skills Start here with Mana Reflection
			case 2290024 : switch (c.getPlayer().getJob()) {
			case FP_ARCHMAGE : skill = 2121002; maxlevel = 20; break;
			case IL_ARCHMAGE : skill = 2221002; maxlevel = 20; break;
			case BISHOP : skill = 2321002; maxlevel = 20; break;
			}
			case 2290026 :  switch (c.getPlayer().getJob()) { // Big Bang
			case FP_ARCHMAGE : skill = 2121001; maxlevel = 20; break;
			case IL_ARCHMAGE : skill = 2221001; maxlevel = 20; break;
			case BISHOP : skill = 2321001; maxlevel = 20; break;
			}
			case 2290028 : switch (c.getPlayer().getJob()) {//Infinity
			case FP_ARCHMAGE : skill = 2121004; maxlevel = 20; break;
			case IL_ARCHMAGE : skill = 2221004; maxlevel = 20; break;
			case BISHOP : skill = 2321004; maxlevel = 20; break;
			}
			case 2290030 : skill = 2121006; maxlevel = 20; break; // Paralyze
			case 2290032 : skill = 2221006; maxlevel = 20; break; // Chain Lightning
			case 2290034 : skill = 2321005; maxlevel = 20; break; // Holy Shield
			case 2290036 : skill = 2121003; maxlevel = 20; break; // Fire Demon
			case 2290038 : skill = 2121005; maxlevel = 20; break; // Elquines
			case 2290040 : skill = 2121007; maxlevel = 20; break; //meteor Shower
			case 2290042 : skill = 2221003; maxlevel = 20; break; // Ice Demon
			case 2290044 : skill = 2221005; maxlevel = 20; break; // Ifrit
			case 2290046 : skill = 2221007; maxlevel = 20; break; // Blizzard 
			case 2290048 : skill = 2321008; maxlevel = 20; break; // Genesis
			case 2290050 : skill = 2321007; maxlevel = 20; break; // Angel Ray
			
			//Bowman Skills Start Here
			case 2290052 : switch (c.getPlayer().getJob()) { //Sharp Eyes
			case BOWMASTER : skill = 3121002; maxlevel = 20; break;
			case CROSSBOWMASTER : skill = 3221002; maxlevel = 20; break;
			}
			case 2290054 : switch (c.getPlayer().getJob()) { // Dragon's Breath
			case BOWMASTER : skill = 3121003; maxlevel = 20; break;
			case CROSSBOWMASTER : skill = 3221003; maxlevel = 20; break;
			}
			case 2290056 : skill = 3120005; maxlevel = 20; break; // Bow Expert
			case 2290058 : skill = 3121007; maxlevel = 20; break; // Hamstring Shot
			case 2290060 : skill = 3121004; maxlevel = 20; break; // Hurricane
			case 2290062 : skill = 3121006; maxlevel = 20; break; // Phoenix
			case 2290064 : skill = 3121008; maxlevel = 20; break; // Concentrate
			case 2290066 : skill = 3220004; maxlevel = 20; break; // Marksman Boost
			case 2290068 : skill = 3221006; maxlevel = 20; break; // Blind
			case 2290070 : skill = 3221001; maxlevel = 20; break; // Piercing Arrows
			case 2290072 : skill = 3221005; maxlevel = 20; break; // Frostprey
			case 2290074 : skill = 3221007; maxlevel = 20; break; // Snipe
			
			//Thief skills start here
			case 2290076 : switch (c.getPlayer().getJob()) { // Shadow Shift
			case NIGHTLORD : skill = 4120002; maxlevel = 20; break;
			case SHADOWER : skill = 4220002; maxlevel = 20; break;
			}
			case 2290078 : switch (c.getPlayer().getJob()) { // Venomous Stab / Star
			case NIGHTLORD : skill = 4120005; maxlevel = 20; break;
			case SHADOWER : skill = 4220005; maxlevel = 20; break;
			}
			case 2290080 : switch (c.getPlayer().getJob()) { // Taunt
			case NIGHTLORD : skill = 4121003; maxlevel = 20; break;
			case SHADOWER : skill = 4221003; maxlevel = 20; break;
			}
			case 2290082 : skill = 4121004; maxlevel = 20; break; // Ninja Ambush
			case 2290084 : skill = 4121007; maxlevel = 20; break; // Triple Throw
			case 2290086 : skill = 4121008; maxlevel = 20; break; // Ninja Storm
			case 2290088 : skill = 4121006; maxlevel = 20; break; // Shadow Claw
			case 2290090 : skill = 4221007; maxlevel = 20; break; // Boomerang Shot
			case 2290092 : skill = 4221001; maxlevel = 20; break; // Assassinate
			case 2290094: skill = 4221006; maxlevel = 20; break; // Smokescreen
			
			//level 30 books
			case 2290001 : switch (c.getPlayer().getJob()) { //Monster Magnet
			case HERO : skill = 1121001; maxlevel = 30; break;
			case PALADIN : skill = 1221001; maxlevel = 30; break;
			case DARKKNIGHT : skill = 1321001; maxlevel = 30; break;
			}
			case 2290003 : switch (c.getPlayer().getJob()) { // Achilles
			case HERO : skill = 1120004; maxlevel = 30; break;
			case PALADIN : skill = 1220005; maxlevel = 30; break;
			case DARKKNIGHT : skill = 1320005; maxlevel = 30; break;			
			}
			case 2290005 : switch (c.getPlayer().getJob()) { // Rush
			case HERO : skill = 1121006; maxlevel = 30; break;
			case PALADIN : skill = 1221007; maxlevel = 30; break;
			case DARKKNIGHT : skill = 1321003; maxlevel = 30; break;
			}
			case 2290007 : switch (c.getPlayer().getJob()) { // Power stance
			case HERO : skill = 1121002; maxlevel = 30; break;
			case PALADIN : skill = 1221002; maxlevel = 30; break;
			case DARKKNIGHT : skill = 1321003; maxlevel = 30; break;
			}
			case 2290009 : skill = 1120003; maxlevel = 30; break; //Advanced Combo
			case 2290011 : skill = 1121008; maxlevel = 30; break; // Brandish
			case 2290013 : skill = 1221009; maxlevel = 30; break; // Blast
			case 2290015 : skill = 1120005; maxlevel = 30; break; //Guardian
			case 2290017 : skill = 1121010; maxlevel = 30; break; // Enrage
			case 2290021 : skill = 1221011; maxlevel = 30; break; // Heaven's Hammer
			case 2290023 : skill = 1320006; maxlevel = 30; break; // Berserk
			
			//Magician Skills Start here with Mana Reflection
			case 2290025 : switch (c.getPlayer().getJob()) {
			case FP_ARCHMAGE : skill = 2121002; maxlevel = 30; break;
			case IL_ARCHMAGE : skill = 2121002; maxlevel = 30; break;
			case BISHOP : skill = 2321002; maxlevel = 30; break;
			}
			case 2290027 : switch (c.getPlayer().getJob()) { // Big Bang
			case FP_ARCHMAGE : skill = 2121001; maxlevel = 30; break;
			case IL_ARCHMAGE : skill = 2221001; maxlevel = 30; break;
			case BISHOP : skill = 2321001; maxlevel = 30; break;
			}
			case 2290029 : switch (c.getPlayer().getJob()) {//Infinity
			case FP_ARCHMAGE : skill = 2121004; maxlevel = 30; break;
			case IL_ARCHMAGE : skill = 2221004; maxlevel = 30; break;
			case BISHOP : skill = 2321004; maxlevel = 30; break;
			}
			case 2290031 : skill = 2121006; maxlevel = 30; break; // Paralyze
			case 2290033 : skill = 2221006; maxlevel = 30; break; // Chain Lightning
			case 2290035 : skill = 2321005; maxlevel = 30; break; // Holy Shield
			case 2290037 : skill = 2121003; maxlevel = 30; break; // Fire Demon
			case 2290039 : skill = 2121005; maxlevel = 30; break; // Elquines
			case 2290041 : skill = 2121007; maxlevel = 30; break; // Meteor Shower
			case 2290043 : skill = 2221003; maxlevel = 30; break; // Ice Demon
			case 2290045 : skill = 2221005; maxlevel = 30; break; // Ifrit
			case 2290047 : skill = 2221007; maxlevel = 30; break; // Blizzard 
			case 2290049 : skill = 2321008; maxlevel = 30; break; // Genesis
			case 2290051 : skill = 2321007; maxlevel = 30; break; // Angel Ray

			//Bowman Skills Start Here
			case 2290053 : switch (c.getPlayer().getJob()) { //Sharp Eyes
			case BOWMASTER : skill = 3121002; maxlevel = 30; break;
			case CROSSBOWMASTER : skill = 3221002; maxlevel = 30; break;
			}
			case 2290055 : switch (c.getPlayer().getJob()) { // Dragon's Breath
			case BOWMASTER : skill = 3121003; maxlevel = 30; break;
			case CROSSBOWMASTER : skill = 3221003; maxlevel = 30; break;
			}
			case 2290057 : skill = 3120005; maxlevel = 30; break; // Bow Expert
			case 2290059 : skill = 3121007; maxlevel = 30; break; // Hamstring Shot
			case 2290061 : skill = 3121004; maxlevel = 30; break; // Hurricane
			case 2290063 : skill = 3121006; maxlevel = 30; break; // Phoenix
			case 2290065 : skill = 3121008; maxlevel = 30; break; // Concentrate
			case 2290067 : skill = 3220004; maxlevel = 30; break; // Marksman Boost
			case 2290069 : skill = 3221006; maxlevel = 30; break; // Blind
			case 2290071 : skill = 3221001; maxlevel = 30; break; // Piercing Arrows
			case 2290073 : skill = 3221005; maxlevel = 30; break; // Frostprey
			case 2290075 : skill = 3221007; maxlevel = 30; break; // Snipe
			
			//Thief skills start here
			case 2290077 : switch (c.getPlayer().getJob()) { // Shadow Shift
			case NIGHTLORD : skill = 4120002; maxlevel = 30; break;
			case SHADOWER : skill = 4220002; maxlevel = 30; break;
			}
			case 2290079 : switch (c.getPlayer().getJob()) { // Venomous Stab / Star
			case NIGHTLORD : skill = 4120005; maxlevel = 30; break;
			case SHADOWER : skill = 4220005; maxlevel = 30; break;
			}
			case 2290081 : switch (c.getPlayer().getJob()) { // Taunt
			case NIGHTLORD : skill = 4121003; maxlevel = 30; break;
			case SHADOWER : skill = 4221003; maxlevel = 30; break;
			}
			case 2290083 : skill = 4121004; maxlevel = 30; break; // Ninja Ambush
			case 2290085 : skill = 4121007; maxlevel = 30; break; // Triple Throw
			case 2290087 : skill = 4121008; maxlevel = 30; break; // Ninja Storm
			case 2290089 : skill = 4121006; maxlevel = 30; break; // Shadow Claw
			case 2290091 : skill = 4221007; maxlevel = 30; break; // Boomerang Shot
			case 2290093 : skill = 4221001; maxlevel = 30; break; // Assassinate
			case 2290095 : skill = 4221006; maxlevel = 30; break; // Smokescreen
			}
			ISkill skill2 = SkillFactory.getSkill(skill);
			int curlevel = player.getSkillLevel(skill2);
			player.changeSkillLevel(skill2, curlevel, maxlevel);
		} else {
			log.info("[h4x] Player {} is using a SkillBook they does not have: {}", c.getPlayer().getName(), Integer.valueOf(itemId));
		}
	}
}
