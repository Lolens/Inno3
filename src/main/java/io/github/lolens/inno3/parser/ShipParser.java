package io.github.lolens.inno3.parser;

import io.github.lolens.inno3.entity.Ship;

import java.util.List;

public interface ShipParser {

  Ship parse(String line);

  List<Ship> parse(List<String> lines);

}
