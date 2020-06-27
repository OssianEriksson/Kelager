package main.collision;

import main.Vec;

public interface CollisionObject {
	
	CollisionData test(Vec x);
	
}
