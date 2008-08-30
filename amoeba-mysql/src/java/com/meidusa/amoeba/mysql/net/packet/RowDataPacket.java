/*
 * 	This program is free software; you can redistribute it and/or modify it under the terms of 
 * the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, 
 * or (at your option) any later version. 
 * 
 * 	This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details. 
 * 	You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.meidusa.amoeba.mysql.net.packet;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.meidusa.amoeba.net.packet.AbstractPacketBuffer;

/**
 * 
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 *
 */
public class RowDataPacket extends AbstractPacket {
	public List<String> columns;
	
	@Override
	public void init(AbstractPacketBuffer myBuffer){
		super.init(myBuffer);
		MysqlPacketBuffer buffer = (MysqlPacketBuffer)myBuffer;
		if(columns == null){
			columns = new ArrayList<String>();
		}
		
		while(buffer.getPosition()<this.packetLength + HEADER_SIZE){
			columns.add(buffer.readLengthCodedString(CODE_PAGE_1252));
		}
		
	}
	
	@Override
	public void write2Buffer(AbstractPacketBuffer myBuffer) throws UnsupportedEncodingException{
		super.write2Buffer(myBuffer);
		MysqlPacketBuffer buffer = (MysqlPacketBuffer)myBuffer;
		if(columns == null)return;
		Iterator<String> it = columns.iterator();
		while(it.hasNext()){
			buffer.writeLengthCodedString(it.next(), CODE_PAGE_1252);
		}
	}
	
	
}
