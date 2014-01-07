package com.example.techcampteam01;

import android.graphics.Point;

/**
 * 
 * 
 * @author ティエプ
 * 
 */
public class Rectangle {

	private int x, y;
	private int width, height;

	public Rectangle(int x, int y, int width, int height) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWith() {
		return width;
	}

	public void setWith(int with) {
		this.width = with;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean checkOverlap(Rectangle rect2) {
		Point P1 = new Point(x, y);
		Point P2 = new Point(x + width, y + height);
		Point P3 = new Point(x + width, y);
		Point P4 = new Point(x, y + height);

		if (checkPointInRectangle(P1)) {

			return true;
		}

		return false;

	}

	public boolean checkPointInRectangle(Point p) {

		if (p.x > x && p.x < x + width && p.y > y && p.y < y + height)
			return true;
		return false;
	}

}
