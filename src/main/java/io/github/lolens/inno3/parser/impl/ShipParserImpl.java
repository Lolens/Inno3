package io.github.lolens.inno3.parser.impl;

import io.github.lolens.inno3.entity.Ship;
import io.github.lolens.inno3.parser.ShipParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShipParserImpl implements ShipParser {

  @Override
  public Ship parse(String line) {
    String[] tokens = line.split(",");
    return new Ship(
        Integer.parseInt(tokens[0]),
        Integer.parseInt(tokens[1]),
        Integer.parseInt(tokens[2]),
        Integer.parseInt(tokens[3]),
        Integer.parseInt(tokens[4])
    );
  }

  @Override
  public List<Ship> parse(List<String> lines) {
    return lines.stream()
        .map(this::parse)
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
