package com.discobeard.spriter.draw;

import com.discobeard.spriter.Reference;

public interface Drawer {
	void draw(Reference ref,float x,float y, float pivot_x, float pivot_y,float angle);
}
