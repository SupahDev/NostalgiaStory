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

/* The Ticket Gate
	- Subway Ticketing Booth (103000100)
*/

importPackage(net.sf.odinms.client);

var status = 0;
var valid = 0;
var check = 0;
var itemid = Array(4031036, 4031037, 4031038);
var maps = Array(103000900, 103000903, 103000906);

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0) {
			cm.sendOk("Alright, see you next time.");
			cm.dispose();
			return;
		}
		if (mode == 1) {
			status++;
		}
		else {
			status--;
		}
		if (status == 0) {
			var cal = java.util.Calendar.getInstance();
			var time = cal.getTimeInMillis();
			var lastJQ = cm.grabJQ();
			if (lastJQ == true) {
				if (cm.getLevel() < 15) {
					cm.sendOk("You must be a higher level to enter the construction sites.");
					cm.dispose();
					check = 1;
				}
				else {
					var selStr = "Which construction site would you like to enter?";
					if (cm.getLevel() >= 40) {
						valid = 3;
					}
					else if (cm.getLevel() >= 30) {
						valid = 2;
					}
					else if (cm.getLevel() >= 20) {
						valid = 1;
					}
					for (var i = 0; i < valid; i++) {
						selStr += "\r\n#L" + i + "##m" + maps[i] + "##l";
					}
					cm.sendSimple(selStr);
				}
			}
			else{
				cm.sendOk("You can only do a jump quest once every 5 minutes.");
				cm.dispose();
				check = 1;
			}
		} else if (status == 1) {
			if (check != 1) {
				if (!cm.haveItem(itemid[selection])) {
					cm.sendOk("You need to buy a ticket before you can enter here!")
					cm.dispose();
				} else {
					selected = selection;
					cm.sendYesNo("Are you sure you want to enter the construction sites?");
				}
			}
		} else if (status == 2) {
			if (check != 1) {
					cm.gainItem(itemid[selected], -1);
					cm.warp(maps[selected], 0);
					cm.dispose();
			}
		}
	}
}	

