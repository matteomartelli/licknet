/*
 * Copyright (C) 2014 Matteo Martelli matteomartelli3@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package licknet.view;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Matteo Martelli matteosartelli3@gmail.com
 */
public class Frame extends JFrame {
	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 400;
	
	public Frame() {
		JLabel jlbHelloWorld = new JLabel("Hello World");
		add(jlbHelloWorld);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		pack();
		setVisible(true);
	}
}
