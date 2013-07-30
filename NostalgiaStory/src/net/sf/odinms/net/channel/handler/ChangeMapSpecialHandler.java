/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
					   Matthias Butz <matze@odinms.de>
					   Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.sf.odinms.net.channel.handler;

import net.sf.odinms.client.MapleClient;
import net.sf.odinms.net.AbstractMaplePacketHandler;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.server.MaplePortal;
import net.sf.odinms.server.maps.MapleMap;
import net.sf.odinms.server.maps.SavedLocationType;
import net.sf.odinms.tools.data.input.SeekableLittleEndianAccessor;

public class ChangeMapSpecialHandler extends AbstractMaplePacketHandler {
	@Override
	public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
		MapleMap to;
		MaplePortal pto;
		slea.readByte();
		String startwp = slea.readMapleAsciiString();
		slea.readByte();
		byte sourcefm = slea.readByte();
		slea.readByte();

		MaplePortal portal = c.getPlayer().getMap().getPortal(startwp);
		if (portal.getType() == 7) {
			if (c.getPlayer().getMapId() == 230040410) {
				to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(230040420);
				pto = to.getPortal("out00"); // or st00?
			}
		else if (c.getPlayer().getMapId() == 211042300) {
				to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(211042400);
				pto = to.getPortal("west00"); // or st00?
			}
			else if (c.getPlayer().getMapId() == 240020400) {
				to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(240020401);
				pto = to.getPortal("out00"); // or st00?
			}
			else if (c.getPlayer().getMapId() == 240020100) {
				to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(240020101);
				pto = to.getPortal("out00"); // or st00?
			}
			else if (sourcefm != -1) {
				to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(910000000);
				c.getPlayer().saveLocation(SavedLocationType.FREE_MARKET);
				pto = to.getPortal("out00"); // or st00?
			}
		   else {
				int returnMap = c.getPlayer().getSavedLocation(SavedLocationType.FREE_MARKET);
				if (returnMap < 0) {
					returnMap = 102000000; // oh well..., to fix people stuck in fm
				}
				to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(returnMap);
				c.getPlayer().clearSavedLocation(SavedLocationType.FREE_MARKET);
				pto = to.getPortal("market00");
				if(pto == null) {
				    pto = to.getPortal(0);
				}
			}
			c.getPlayer().changeMap(to, pto);
		}
		else if (portal.getType() == 8){
			if (c.getPlayer().getMapId() == 240010500) {
				to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(240010501);
				pto = to.getPortal("out00"); // or st00?
				c.getPlayer().changeMap(to, pto);				
				}
			else if (c.getPlayer().getMapId() == 220080000)	 {
				to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(220080001);
				pto = to.getPortal("st00");
				c.getPlayer().changeMap(to, pto);
			}		
			}
		c.getPlayer().updateStatsEmpty(); // avoid clientlock
	}
}