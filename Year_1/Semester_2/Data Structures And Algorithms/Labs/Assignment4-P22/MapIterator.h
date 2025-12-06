#pragma once
#include "Map.h"

class MapIterator {
	friend class Map;

private:
	const Map& map;
	int currentBucket;
	Map::Node* currentNode;

	MapIterator(const Map& m);

public:
	void first();
	void next();
	TElem getCurrent();
	bool valid() const;
};
