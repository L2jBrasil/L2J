/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jbr.gameserver.serverpackets;

import com.l2jbr.gameserver.model.L2ItemInstance;
import com.l2jbr.gameserver.model.TradeList;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;


/**
 * This class ...
 * @version $Revision: 1.3.2.1.2.4 $ $Date: 2005/03/27 15:29:40 $
 */
public class PrivateStoreManageListBuy extends L2GameServerPacket
{
	private static final String _S__D0_PRIVATESELLLISTBUY = "[S] b7 PrivateSellListBuy";
	private final L2PcInstance _activeChar;
	private final int _playerAdena;
	private final L2ItemInstance[] _itemList;
	private final TradeList.TradeItem[] _buyList;
	
	public PrivateStoreManageListBuy(L2PcInstance player)
	{
		_activeChar = player;
		_playerAdena = _activeChar.getAdena();
		_itemList = _activeChar.getInventory().getUniqueItems(false, true);
		_buyList = _activeChar.getBuyList().getItems();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeByte(0xb7);
		// section 1
		writeInt(_activeChar.getObjectId());
		writeInt(_playerAdena);
		
		// section2
		writeInt(_itemList.length); // inventory items for potential buy
		for (L2ItemInstance item : _itemList)
		{
			writeInt(item.getItemId());
			writeShort(0); // show enchant lvl as 0, as you can't buy enchanted weapons
			writeInt(item.getCount());
			writeInt(item.getReferencePrice());
			writeShort(0x00);
			writeInt(item.getItem().getBodyPart().getId());
			writeShort(item.getItem().getType2().getId());
		}
		
		// section 3
		writeInt(_buyList.length); // count for all items already added for buy
		for (TradeList.TradeItem item : _buyList)
		{
			writeInt(item.getItem().getId());
			writeShort(0);
			writeInt(item.getCount());
			writeInt(item.getItem().getPrice());
			writeShort(0x00);
			writeInt(item.getItem().getBodyPart().getId());
			writeShort(item.getItem().getType2().getId());
			writeInt(item.getPrice());// your price
			writeInt(item.getItem().getPrice());// fixed store price
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jbr.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__D0_PRIVATESELLLISTBUY;
	}
}
