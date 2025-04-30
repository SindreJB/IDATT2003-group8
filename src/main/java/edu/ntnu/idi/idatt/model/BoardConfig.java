package edu.ntnu.idi.idatt.model;

import java.util.ArrayList;
import java.util.List;

public class BoardConfig {
    private String name;
    private String description;
    private int rows;
    private int columns;
    private List<Integer> snakeHeads;
    private List<Integer> snakeTails;
    private List<Integer> ladderStarts;
    private List<Integer> ladderEnds;

    public BoardConfig() {
        this.snakeHeads = new ArrayList<>();
        this.snakeTails = new ArrayList<>();
        this.ladderStarts = new ArrayList<>();
        this.ladderEnds = new ArrayList<>();
    }

    public BoardConfig(String name, String description, int rows, int columns) {
        this();
        this.name = name;
        this.description = description;
        this.rows = rows;
        this.columns = columns;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }
    public void setColumns(int columns) {
        this.columns = columns;
    }

    public List<Integer> getSnakeHeads() {
        return snakeHeads;
    }

    public void setSnakeHeads(List<Integer> snakeHeads) {
        this.snakeHeads = snakeHeads;
    }

    public List<Integer> getSnakeTails() {
        return snakeTails;
    }

    public void setSnakeTails(List<Integer> snakeTails) {
        this.snakeTails = snakeTails;
    }

    public List<Integer> getLadderStarts() {
        return ladderStarts;
    }

    public void setLadderStarts(List<Integer> ladderStarts) {
        this.ladderStarts = ladderStarts;
    }

    public List<Integer> getLadderEnds() {
        return ladderEnds;
    }

    public void setLadderEnds(List<Integer> ladderEnds) {
        this.ladderEnds = ladderEnds;
    }


    // Helper methods
    public void addSnake(int head, int tail) {
        snakeHeads.add(head);
        snakeTails.add(tail);
    }

    public void addLadder(int start, int end) {
        ladderStarts.add(start);
        ladderEnds.add(end);
    }
}